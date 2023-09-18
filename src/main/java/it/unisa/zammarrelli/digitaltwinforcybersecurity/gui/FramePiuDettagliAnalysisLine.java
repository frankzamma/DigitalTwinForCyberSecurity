package it.unisa.zammarrelli.digitaltwinforcybersecurity.gui;

import com.intellij.ui.components.JBLabel;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.VulnerabilityLine;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class FramePiuDettagliAnalysisLine extends JFrame {
    private VulnerabilityLine vulnerabilityLine;
    private JPanel associatedPanel;
    private PluginToolWindowLogWritingAnalysis toolWindowLogWritingAnalysis;

    public FramePiuDettagliAnalysisLine(VulnerabilityLine v, JPanel panel, PluginToolWindowLogWritingAnalysis toolWindowLogWritingAnalysis) throws HeadlessException {
        super("Vulnerability at line " + v.getLine());

        this.vulnerabilityLine = v;
        this.associatedPanel = panel;
        this.toolWindowLogWritingAnalysis = toolWindowLogWritingAnalysis;
        super.setLayout(new FlowLayout(FlowLayout.LEFT));

        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createPanel();
    }


    private void createPanel(){
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 1;
        constraints.gridwidth =3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        JPanel panelFirst = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFirst.add(new JBLabel("Line: " + vulnerabilityLine.getLine()));
        panelFirst.add(new JBLabel("Severity: " + vulnerabilityLine.getSeverity()));
        panel.add(panelFirst, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridheight = 2;
        constraints.gridwidth =4;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JBLabel descriptionLabel = new JBLabel("<html> Description<br>"
                + vulnerabilityLine.getDescription().replaceAll("\\.", ".<br>")+  "</html>");
        descriptionPanel.add(descriptionLabel);
        panel.add(descriptionPanel,  constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridheight = 2;
        constraints.gridwidth = 4;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        JPanel solutionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JBLabel solutionLabel = new JBLabel("<html>" +
                "Solution: <br>" + vulnerabilityLine.getSolution().replaceAll("\\.", ".<br>")+
                "</html>");
        solutionLabel.setAllowAutoWrapping(true);
        solutionPanel.add(solutionLabel);
        panel.add(solutionPanel, constraints);

        if(vulnerabilityLine.getExampleCode().length() > 0){
            constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 5;
            constraints.gridheight = 3;
            constraints.gridwidth = 4;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            JPanel exampleCodePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JBLabel exampleCodeLabel = new JBLabel( "Example code:<br>"+
                    "<code>" + vulnerabilityLine.getExampleCode().replaceAll("\n", "<br>")  +
                    "</code>");
            exampleCodeLabel.setAllowAutoWrapping(true);
            exampleCodeLabel.setCopyable(true);
            exampleCodePanel.add(exampleCodeLabel);
            panel.add(exampleCodeLabel, constraints);
        }

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = vulnerabilityLine.getExampleCode().length() == 0 ? 5 : 8;
        constraints.gridheight = 1;
        constraints.gridwidth = 4;
        constraints.anchor = GridBagConstraints.LAST_LINE_START;
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));

        if(vulnerabilityLine.getExampleCode().length() > 0){
            JButton copiaCodiceButton = new JButton("Copy code");
            copiaCodiceButton.addActionListener(e ->{
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(vulnerabilityLine.getExampleCode()), null);
                this.setVisible(false);
            });
            panelButtons.add(copiaCodiceButton);
        }


        JButton eliminaButton = new JButton("Delete");
        panelButtons.add(eliminaButton);


        eliminaButton.addActionListener(e->{
            toolWindowLogWritingAnalysis.removeVulnerability(associatedPanel);
            this.setVisible(false);
        });

        panel.add(panelButtons,  constraints);
        super.add(panel);
        super.pack();
        super.setLocationRelativeTo(null);
        super.setResizable(false);
    }
}
