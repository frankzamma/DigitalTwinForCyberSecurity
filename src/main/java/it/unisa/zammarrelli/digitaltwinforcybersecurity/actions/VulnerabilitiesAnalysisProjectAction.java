package it.unisa.zammarrelli.digitaltwinforcybersecurity.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileVisitor;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.theokanning.openai.OpenAiHttpException;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.Vulnerability;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.gui.PluginToolWindowContentVulnerabilitiesAnalysis;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.settings.PluginSettingsStateService;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.util.FileAnalyzer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VulnerabilitiesAnalysisProjectAction extends AnAction {
    private static final String[] directoryNotAnlyze = {".", "..", "venv"};

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project p  = e.getProject();
        ToolWindow window = ToolWindowManager.getInstance(e.getProject())
                .getToolWindow("DigitalTwinForCyberSecurity");
        PluginToolWindowContentVulnerabilitiesAnalysis content =
                (PluginToolWindowContentVulnerabilitiesAnalysis) window.getContentManager().getContent(0).getComponent();


        if(p != null){
            ProjectRootManager projectRootManager  = ProjectRootManager.getInstance(p);
            VirtualFile[] virtualFilesSource = projectRootManager.getContentSourceRoots();

            if(virtualFilesSource.length > 0){
                String token = PluginSettingsStateService.getInstance().getState().getGptToken();

                if(token.length() < 10){
                    content.displayError("Inserire GPT API keys nelle impostazioni!");
                }else {

                    List<VirtualFile> list = new ArrayList<>();

                    VfsUtilCore.visitChildrenRecursively(virtualFilesSource[0], new VirtualFileVisitor<>() {
                        @Override
                        @NotNull
                        public Result visitFileEx(@NotNull final VirtualFile file) {
                            if(file.getExtension() != null && file.getExtension().equals("java"))
                                list.add(file);
                            return CONTINUE;
                        }
                    });

                    content.setLoading((list.size() + 1));
                    List<Document> documents = new ArrayList<>();

                    for(VirtualFile vf : list){
                        documents.add(FileDocumentManager.getInstance().getDocument(vf));
                    }


                    Thread thread = new Thread(()->{
                        try {
                            List<Vulnerability> vulnerabilities = new ArrayList<>();

                            for(int i = 0 ; i < list.size(); i++){
                                content.changeCount((i+1));
                                FileAnalyzer analyzer = new FileAnalyzer(documents.get(i).getText(), token, list.get(i));
                                vulnerabilities.addAll(analyzer.analyze());
                            }


                            if (vulnerabilities.size() == 0) {
                                content.displayInformation("Non sono presenti vulnerabilit\u00E0!");
                            } else {
                                FileEditorManager fileEditorManager = FileEditorManager.getInstance(p);
                                content.addVulnerabilitiesContent(vulnerabilities, fileEditorManager);
                            }
                        } catch (OpenAiHttpException exception) {
                            content.displayError(exception.getMessage());
                        }


                    });

                    thread.start();
                }

            }else{
                VirtualFile[] virtualFiles = projectRootManager.getContentRoots();
                List<VirtualFile> list = new ArrayList<>();
                VfsUtilCore.visitChildrenRecursively(virtualFiles[0], new VirtualFileVisitor<>() {
                    @Override
                    @NotNull
                    public Result visitFileEx(@NotNull final VirtualFile file) {
                        if(file.getExtension() != null && file.getExtension().equals("py"))
                            list.add(file);
                        return CONTINUE;
                    }
                });

            }
        }


    }
}