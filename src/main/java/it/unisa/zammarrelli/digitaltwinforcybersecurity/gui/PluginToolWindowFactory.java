package it.unisa.zammarrelli.digitaltwinforcybersecurity.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.settings.PluginSettingsStateService;
import org.jetbrains.annotations.NotNull;

public class PluginToolWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        PluginToolWindowContentVulnerabilitiesAnalysis toolWindowContentVulnerabilitiesAnalysis = new PluginToolWindowContentVulnerabilitiesAnalysis(true, false);
        Content contentVulnerabilitiesAnalysis = ContentFactory.getInstance().createContent(toolWindowContentVulnerabilitiesAnalysis, "Vulnerability Detector", false);
        toolWindow.getContentManager().addContent(contentVulnerabilitiesAnalysis);

        PluginToolWindowLogWritingAnalysis toolWindowLogWritingAnalysis = new PluginToolWindowLogWritingAnalysis(true, false);
        Content contentWritingAnalysis = ContentFactory.getInstance().createContent(toolWindowLogWritingAnalysis, "Writing Analysis Log", false);
        toolWindow.getContentManager().addContent(contentWritingAnalysis);

    }


}
