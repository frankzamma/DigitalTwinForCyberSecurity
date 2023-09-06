package it.unisa.zammarrelli.digitaltwinforcybersecurity.util;

import com.intellij.openapi.actionSystem.ActionManager;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.actions.DynamicAnalysisHelpAction;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WindowListerForDynamicAnalysisFrame implements WindowListener {
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {
        DynamicAnalysisHelpAction dynamicActionGroup = (DynamicAnalysisHelpAction) ActionManager.getInstance().getAction("HelpAnalisiDinamica");

        dynamicActionGroup.setStatus(true);
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
