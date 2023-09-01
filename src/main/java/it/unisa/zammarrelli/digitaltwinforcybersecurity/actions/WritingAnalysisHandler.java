package it.unisa.zammarrelli.digitaltwinforcybersecurity.actions;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.notification.NotificationsManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.theokanning.openai.OpenAiHttpException;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.ai.GPTWrapper;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.VulnerabilityLine;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.settings.PluginSettingsStateService;
import org.jetbrains.annotations.NotNull;


public class WritingAnalysisHandler extends TypedHandlerDelegate {
    private String last = "";

    @Override
    public @NotNull Result charTyped(char c, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        String gptToken = PluginSettingsStateService.getInstance().getState().getGptToken();

        if(gptToken.length() > 10){
            CaretModel caretModel = editor.getCaretModel();
            Document document = editor.getDocument();
            LogicalPosition logicalPosition = caretModel.getLogicalPosition();
            int start = document.getLineStartOffset(logicalPosition.line-1);
            int end = document.getLineEndOffset(logicalPosition.line -1);

            String line = editor.getDocument().getText(new TextRange(start, end));
            if(!line.equals(last)){
                this.last = line;
                System.out.println(line);
                try{
                    GPTWrapper gptWrapper = new GPTWrapper(gptToken,PluginSettingsStateService.getInstance().getState().getLanguage());

                    String response = gptWrapper.analyzeLine(line);
                    Gson parser = new Gson();
                    JsonObject object = parser.fromJson(response, JsonObject.class);
                    VulnerabilityLine vulnerabilityLine = new VulnerabilityLine();
                    vulnerabilityLine.setVulnerable(object.get("vulnerable").getAsString().equalsIgnoreCase("yes"));
                    vulnerabilityLine.setDescription(object.get("description").getAsString());
                    vulnerabilityLine.setSeverity(object.get("severity").getAsString());

                    if(vulnerabilityLine.isVulnerable()){

                        NotificationGroupManager.getInstance().getNotificationGroup("vulnerabilityNotification")
                                .createNotification("Attento hai inserito del codice vulnerabile!",
                                        "Riga: " + logicalPosition.line+"\n"+ vulnerabilityLine.getDescription(), NotificationType.INFORMATION).notify(project);
                    }
                }catch (OpenAiHttpException exception){
                        exception.printStackTrace();
                }

            }

        }

        return Result.STOP;
        }


 }



