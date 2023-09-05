package it.unisa.zammarrelli.digitaltwinforcybersecurity.actions;


import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.ai.GPTWrapper;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.Language;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.gui.FrameDynamicaAnalysisHelp;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.settings.PluginSettingsState;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.settings.PluginSettingsStateService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DynamicAnalysisHelpAction extends AnAction {
    private volatile boolean status = true;


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        ProjectRootManager projectRootManager = ProjectRootManager.getInstance(project);

        String programmingLanguage = projectRootManager.getProjectSdkTypeName()
                .replaceAll("SDK", "")
                .replaceAll("sdk", "")
                .replaceAll(" ", "");

        PluginSettingsState settingsState = PluginSettingsStateService.getInstance().getState();

        String gptToken = settingsState.getGptToken();
        Language language = settingsState.getLanguage();
        GPTWrapper gptWrapper = new GPTWrapper(gptToken, language);

        List<String> tools = gptWrapper.getDynamicAnalysisTool(programmingLanguage);

        FrameDynamicaAnalysisHelp frameDynamicaAnalysisHelp = new FrameDynamicaAnalysisHelp(tools, programmingLanguage, language);

        frameDynamicaAnalysisHelp.setVisible(true);

        this.status = false;

    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(status);
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
