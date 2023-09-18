package it.unisa.zammarrelli.digitaltwinforcybersecurity.actions;

import com.github.hypfvieh.util.FileIoUtil;
import com.intellij.ide.highlighter.HtmlFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;

import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.theokanning.openai.OpenAiHttpException;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.Vulnerability;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.gui.PluginToolWindowContentVulnerabilitiesAnalysis;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.settings.PluginSettingsStateService;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.util.FileAnalyzer;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.util.ReportGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class VulnerabilitiesAnalysisAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        System.out.println("Azione Avviata!");
        Project p =  e.getProject();

        ToolWindow window = ToolWindowManager.getInstance(e.getProject())
                .getToolWindow("DigitalTwinForCyberSecurity");
        PluginToolWindowContentVulnerabilitiesAnalysis content =
                (PluginToolWindowContentVulnerabilitiesAnalysis) window.getContentManager().getContent(0).getComponent();


        if(p != null){
            VirtualFile vf = e.getData(PlatformDataKeys.VIRTUAL_FILE);
            if (vf != null){
                String token = PluginSettingsStateService.getInstance().getState().getGptToken();

                if(token.length() < 10){
                    content.displayError("Inserire GPT API keys nelle impostazioni!");
                }else {
                    Document document = FileDocumentManager.getInstance().getDocument(vf);
                    content.setLoading();
                    FileAnalyzer analyzer = new FileAnalyzer(document.getText(), token, vf);
                    FileEditorManager fileEditorManager = FileEditorManager.getInstance(p);

                    Thread thread = new Thread(()->{

                        try {
                            List<Vulnerability> vulnerabilities;
                            try{
                                vulnerabilities = analyzer.analyze(false);
                            }catch (OpenAiHttpException exception){
                                if(exception.code.equals("context_length_exceeded")){
                                    vulnerabilities = analyzer.analyze(true);
                                }else{
                                    vulnerabilities = analyzer.analyze(false);
                                }
                            }

                            if (vulnerabilities.size() == 0) {
                                content.displayInformation("Non sono presenti vulnerabilit\u00E0!");
                            } else {
                                content.addVulnerabilitiesContent(vulnerabilities, fileEditorManager);
                                String html =   ReportGenerator.generate(vulnerabilities);

                                try {
                                    ProjectRootManager projectRootManager = ProjectRootManager.getInstance(p);
                                    VirtualFile[] root = projectRootManager.getContentRoots();
                                    File file = new File(root[0].getCanonicalPath()
                                            + File.separator +"reports" + File.separator+"report-"+
                                            LocalDateTime.now().toString()
                                                    .replaceAll("T", "_")
                                                    .replaceAll(":", "")
                                                    .split("\\.")[0] +".html");

                                    FileUtil.writeToFile(file, html);

                                    System.out.println(file.getAbsolutePath());
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        } catch (OpenAiHttpException exception) {
                            content.displayError(exception.getMessage());
                        }
                    });
                    thread.start();

                }
            }else {
                content.displayError("Insert cursor in a file to use this feature!");
            }
        }else {
            content.displayError("Open a project to use this feature");
        }
    }
}
