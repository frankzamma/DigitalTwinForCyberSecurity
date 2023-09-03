package it.unisa.zammarrelli.digitaltwinforcybersecurity.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.gui.FramePiuDettagliAnalysisLine;
import org.jetbrains.annotations.NotNull;

public class NotifyAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(e.getProject()).getToolWindow("DigitalTwinForCyberSecurity");

        toolWindow.activate(null);
        toolWindow.getContentManager().setSelectedContent(toolWindow.getContentManager().getContent(1));


    }
}
