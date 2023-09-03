package it.unisa.zammarrelli.digitaltwinforcybersecurity.gui;

import com.intellij.ui.components.JBLabel;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.VulnerabilityLine;

import javax.swing.*;
import java.awt.*;

public class FramePiuDettagliAnalysisLine extends JFrame {
    private VulnerabilityLine vulnerabilityLine;
    private JPanel associatedPanel;
    private PluginToolWindowLogWritingAnalysis toolWindowLogWritingAnalysis;

    public FramePiuDettagliAnalysisLine(VulnerabilityLine v, JPanel panel, PluginToolWindowLogWritingAnalysis toolWindowLogWritingAnalysis) throws HeadlessException {
        super("Vulnerability at row " + v.getLine());

        this.vulnerabilityLine = v;
        this.associatedPanel = panel;
        this.toolWindowLogWritingAnalysis = toolWindowLogWritingAnalysis;
        super.setLayout(new FlowLayout(FlowLayout.LEFT));
        super.setLocationRelativeTo(null);
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createPanel();
    }


    private void createPanel(){
        JPanel panel = new JPanel(new GridLayout(4, 1));

        JPanel panelLine = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelLine.add(new JBLabel("Riga: " + vulnerabilityLine.getLine()));
        panel.add(panelLine);

        JPanel severityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        severityPanel.add(new JBLabel("Severity: " + vulnerabilityLine.getSeverity()));
        panel.add(severityPanel);

        JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        descriptionPanel.add(new JBLabel("<html>"+vulnerabilityLine.getDescription()+"</html>"));
        panel.add(descriptionPanel);

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton eliminaButton = new JButton("Elimina");
        JButton vediNelCodiceButton = new JButton("Visualizza nel codice");

        panelButtons.add(vediNelCodiceButton);
        panelButtons.add(eliminaButton);

        vediNelCodiceButton.addActionListener(e ->this.setVisible(false));
        eliminaButton.addActionListener(e->toolWindowLogWritingAnalysis.removeVulnerability(associatedPanel));

        super.add(panel);
        super.pack();
    }
}
