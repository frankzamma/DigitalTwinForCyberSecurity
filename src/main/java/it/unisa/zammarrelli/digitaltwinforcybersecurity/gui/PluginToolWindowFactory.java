package it.unisa.zammarrelli.digitaltwinforcybersecurity.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class PluginToolWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        PluginToolWindowContent toolWindowContent = new PluginToolWindowContent(true, false);
        Content content = ContentFactory.SERVICE.getInstance().createContent(toolWindowContent, "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
