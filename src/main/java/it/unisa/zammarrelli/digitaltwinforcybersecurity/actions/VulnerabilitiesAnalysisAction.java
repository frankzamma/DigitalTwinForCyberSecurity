package it.unisa.zammarrelli.digitaltwinforcybersecurity.actions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.theokanning.openai.OpenAiHttpException;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.ai.GPTWrapper;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.Vulnerability;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.gui.PluginToolWindowContent;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.settings.PluginSettingsState;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.settings.PluginSettingsStateService;
import net.minidev.json.JSONObject;
import org.jetbrains.annotations.NotNull;
import retrofit2.HttpException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VulnerabilitiesAnalysisAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        System.out.println("Azione Avviata!");
        Project p =  e.getProject();

        ToolWindow window = ToolWindowManager.getInstance(e.getProject())
                .getToolWindow("DigitalTwinForCyberSecurity");
        PluginToolWindowContent content =
                (PluginToolWindowContent) window.getContentManager().getContent(0).getComponent();


        if(p != null){
            VirtualFile vf = e.getData(PlatformDataKeys.VIRTUAL_FILE);
            if (vf != null){
                Document document = FileDocumentManager.getInstance().getDocument(vf);
                String token = PluginSettingsStateService.getInstance().getState().getGptToken();

                if(token.length() < 10){
                    content.displayError("Inserire GPT API keys nelle impostazioni!");
                }else {
                    content.setLoading();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                GPTWrapper gptWrapper = new GPTWrapper(token,
                                        PluginSettingsStateService.getInstance().getState().getLanguage());
                                String result = gptWrapper.analyze(document.getText());

                                Gson parser = new Gson();
                                JsonArray array = parser.fromJson(result, JsonArray.class).getAsJsonArray();
                                if (array.size() == 0) {
                                    content.displayInformation("Non sono presenti vulnerabilit\u00E0!");
                                } else {

                                    List<Vulnerability> vulnerabilities = new ArrayList<>();
                                    for (int i = 0; i < array.size(); i++) {
                                        JsonObject vulnerability = array.get(i).getAsJsonObject();
                                        Vulnerability v = new Vulnerability(vulnerability.get("name").getAsString(),
                                                vulnerability.get("description").getAsString(),
                                                vulnerability.get("level").getAsString(),
                                                vulnerability.get("code").getAsString(),
                                                vulnerability.get("line").getAsInt());

                                        List<String> lines = document.getText().lines().collect(Collectors.toList());
                                        String firstLineOfCode = v.getCode().lines().collect(Collectors.toList()).get(0);
                                        if (!lines.get(v.getLine()).equals(firstLineOfCode)) {
                                            for (int j = v.getLine(); j < lines.size(); j++) {
                                                if (lines.get(v.getLine()).equals(firstLineOfCode)) {
                                                    v.setLine(j);
                                                    break;
                                                }
                                            }
                                        }
                                        vulnerabilities.add(v);
                                    }

                                    FileEditorManager fileEditorManager = FileEditorManager.getInstance(e.getProject());


                                    content.addVulnerabilitiesContent(vulnerabilities, fileEditorManager, vf);
                                }
                            } catch (OpenAiHttpException exception) {
                                content.displayError(exception.getMessage());

                            }
                        }


                    });

                    thread.start();
                }
            }else {
                content.displayError("Open and insert a cursor in a file to use this function!");
            }
        }else {
            content.displayError("Open a project to use this function");
        }
    }
}
