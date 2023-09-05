package it.unisa.zammarrelli.digitaltwinforcybersecurity.gui;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DynamicActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.UIUtil;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.actions.DynamicAnalysisHelpAction;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class FrameDynamicaAnalysisHelp extends JFrame {
    private List<String> tools;
    private String sdkLanguage;


    private Language language;

    public FrameDynamicaAnalysisHelp(List<String> tools, String sdkLanguange, Language language) throws HeadlessException {
        this.tools = tools;
        this.sdkLanguage = sdkLanguange;
        this.language = language;
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                DynamicAnalysisHelpAction dynamicActionGroup = (DynamicAnalysisHelpAction) ActionManager.getInstance().getAction("HelpAnalisiDinamica");

                dynamicActionGroup.setStatus(true);
            }

            @Override
            public void windowClosed(WindowEvent e) {

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
        });
        createPanel();
    }

    private void createPanel(){
        JPanel panelMain = new JPanel(new BorderLayout());
        JBLabel labelLanguage = new JBLabel("<html>Il progetto a cui sta lavorando \u00E8 basato su <b>" + sdkLanguage + "</b><html>");

        panelMain.add(labelLanguage, BorderLayout.NORTH);
        JPanel centerMain = new JPanel(new BorderLayout());

        JBLabel labelNorth = new JBLabel("Puoi usare uno dei seguenti strumenti per l'analisi dinamica:");
        centerMain.add(labelNorth,BorderLayout.NORTH);
        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT));

        for(int i = 0; i < Math.min(tools.size(), 5); i++){
            JButton btn = new JButton(tools.get(i));

            int index = i;
            btn.addActionListener(e-> System.out.println("Hai selezionato" + tools.get(index)));
            center.add(btn);
        }

        panelMain.add(centerMain, BorderLayout.CENTER);
        centerMain.add(center, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new FlowLayout());
        southPanel.add(new JBLabel("Se non conosci nessuno di questi strumenti"));
        JBLabel cliccaQuiLabel = new JBLabel("<html>clicca qui</html>");
        cliccaQuiLabel.setForeground(Color.MAGENTA);
        cliccaQuiLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cliccaQuiLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Click Ulteriori informazioni");
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e){
                cliccaQuiLabel.setText("<html><u>clicca qui</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cliccaQuiLabel.setForeground(Color.MAGENTA);
                cliccaQuiLabel.setText("<html>clicca qui</html>");
            }
        });
        southPanel.add(cliccaQuiLabel);
        panelMain.add(southPanel, BorderLayout.SOUTH);
        this.add(panelMain);
        super.pack();
        super.setLocationRelativeTo(null);
        super.setResizable(false);

    }

}
