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
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.ai.GPTWrapper;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.Vulnerability;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.gui.PluginToolWindowContent;
import net.minidev.json.JSONObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VulnerabilitiesAnalysisAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        System.out.println("Azione Avviata!");
        Project p =  e.getProject();

        if(p != null){
            VirtualFile vf = e.getData(PlatformDataKeys.VIRTUAL_FILE);
            if (vf != null){
                Document document = FileDocumentManager.getInstance().getDocument(vf);
                GPTWrapper gptWrapper = new GPTWrapper();

                String result =  gptWrapper.analyze(document.getText());

                Gson parser =  new Gson();
                JsonArray array =  parser.fromJson(result, JsonArray.class).getAsJsonArray();
                List<Vulnerability> vulnerabilities = new ArrayList<>();
                for(int i = 0; i < array.size(); i++){
                    JsonObject employee = array.get(i).getAsJsonObject();
                    vulnerabilities.add(new Vulnerability(employee.get("name").getAsString(),
                            employee.get("description").getAsString(),
                            employee.get("line").getAsInt()));
                }
                ToolWindow window = ToolWindowManager.getInstance(e.getProject())
                        .getToolWindow("DigitalTwinForCyberSecurity");
                PluginToolWindowContent content =
                        (PluginToolWindowContent) window.getContentManager().getContent(0).getComponent();

                content.addVulnerabilitiesContent(vulnerabilities);
                }
        }
    }
}
