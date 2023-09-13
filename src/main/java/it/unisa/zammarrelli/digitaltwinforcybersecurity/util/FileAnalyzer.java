package it.unisa.zammarrelli.digitaltwinforcybersecurity.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.theokanning.openai.OpenAiHttpException;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.ai.GPTWrapper;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.Vulnerability;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.gui.PluginToolWindowContentVulnerabilitiesAnalysis;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.settings.PluginSettingsStateService;

import java.util.ArrayList;
import java.util.List;

public class FileAnalyzer {
    private String text;
    private String token;

    private VirtualFile file;
    public FileAnalyzer(String text, String token, VirtualFile virtualFile) {
        this.text = text;
        this.token = token;
        this.file = virtualFile;
    }

    public List<Vulnerability> analyze() throws OpenAiHttpException{
        GPTWrapper gptWrapper = new GPTWrapper(token,
                PluginSettingsStateService.getInstance().getState().getLanguage());
        String result = gptWrapper.analyzeFile(text );
        Gson parser = new Gson();
        JsonArray array;
        try {
            array = parser.fromJson(result, JsonArray.class).getAsJsonArray();
        }catch (JsonSyntaxException exception){
            String newJson = gptWrapper.fixJson(result);

            if(newJson.startsWith("{") && newJson.endsWith("}")){
                newJson = "[" + newJson + "]";
            }
            array = parser.fromJson(newJson, JsonArray.class).getAsJsonArray();

        }
        List<Vulnerability> vulnerabilities = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JsonObject vulnerability = array.get(i).getAsJsonObject();
            if(!vulnerability.isEmpty()){
                Vulnerability v = new Vulnerability(vulnerability.get("name").getAsString(),
                        vulnerability.get("description").getAsString(),
                        vulnerability.get("severity").getAsString(),
                        vulnerability.get("solution").getAsString(),
                        vulnerability.get("example_solution_code") == null ?
                                "": vulnerability.get("example_solution_code").getAsString(),
                        vulnerability.get("line").getAsInt(),
                        file);
                vulnerabilities.add(v);
            }


        }
        return vulnerabilities;
    }
}
