package it.unisa.zammarrelli.digitaltwinforcybersecurity.gui;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DynamicActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.UIUtil;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.actions.DynamicAnalysisGuideActionListner;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.actions.DynamicAnalysisGuideAlternative;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.actions.DynamicAnalysisHelpAction;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.Language;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.util.WindowListerForDynamicAnalysisFrame;

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
        super.addWindowListener(new WindowListerForDynamicAnalysisFrame());
        createPanel();
    }

    private void createPanel(){
        JPanel panelMain = new JPanel(new BorderLayout());
        JPanel panelLanguage = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JBLabel labelLanguage = new JBLabel("<html>The project you are working is based on Java>" + sdkLanguage + "</b><html>");
        panelLanguage.add(labelLanguage);
        panelMain.add(panelLanguage, BorderLayout.NORTH);

        if(tools.size() > 0){
            JPanel centerMain = new JPanel(new BorderLayout());

            JBLabel labelNorth = new JBLabel("You can use one of this instruments:");
            JPanel panelNorth = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelNorth.add(labelNorth);
            centerMain.add(panelNorth,BorderLayout.NORTH);

            JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JFrame thisReference = this;


            for(int i = 0; i < Math.min(tools.size(), 5); i++){
                JButton btn = new JButton(tools.get(i));

                btn.addActionListener(new DynamicAnalysisGuideActionListner(tools.get(i), sdkLanguage));
                btn.addActionListener(e -> thisReference.setVisible(false));
                center.add(btn);
            }

            panelMain.add(centerMain, BorderLayout.CENTER);
            centerMain.add(center, BorderLayout.CENTER);

            JPanel southPanel = new JPanel(new FlowLayout());
            southPanel.add(new JBLabel("If you don't know what is dynamic analysis"));
            JBLabel cliccaQuiLabel = new JBLabel("<html>click here</html>");
            cliccaQuiLabel.setForeground(Color.MAGENTA);
            cliccaQuiLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            cliccaQuiLabel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                   thisReference.setVisible(false);
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e){
                    cliccaQuiLabel.setText("<html><u>click here</u></html>");
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    cliccaQuiLabel.setForeground(Color.MAGENTA);
                    cliccaQuiLabel.setText("<html>click here</html>");
                }
            });

            cliccaQuiLabel.addMouseListener(new DynamicAnalysisGuideAlternative(sdkLanguage));
            southPanel.add(cliccaQuiLabel);
            panelMain.add(southPanel, BorderLayout.SOUTH);
        }else{
            JPanel panelNothing = new JPanel();

            panelNothing.setLayout(new BoxLayout(panelNothing, BoxLayout.Y_AXIS));
            JBLabel labelError = new JBLabel("Sorry, it seems there was an error in the analysis of the possible tools to use");
            JBLabel labelSolution = new JBLabel("Please, try again later");

            panelNothing.add(labelError);
            labelSolution.add(labelSolution);
            panelMain.add(panelNothing, BorderLayout.CENTER);
        }

        this.add(panelMain);
        super.pack();
        super.setLocationRelativeTo(null);
        super.setResizable(false);
    }

}
