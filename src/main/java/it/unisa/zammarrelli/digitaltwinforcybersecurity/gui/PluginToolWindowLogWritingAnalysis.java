package it.unisa.zammarrelli.digitaltwinforcybersecurity.gui;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBDimension;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.Vulnerability;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.VulnerabilityLine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class PluginToolWindowLogWritingAnalysis extends SimpleToolWindowPanel {
    private final JPanel verticalPanel = new JPanel();
    private final JScrollPane scrollPane = new JBScrollPane(verticalPanel);

    private final ActionManager actionManager = ActionManager.getInstance();
    private ActionToolbar actionToolbar;
    private final JBLabel labelInitialLog =
            new JBLabel( "In this tab there will be the analysis log during writing (if active)");

    public PluginToolWindowLogWritingAnalysis(boolean vertical, boolean borderless) {
        super(vertical, borderless);
        actionToolbar = actionManager.createActionToolbar("toolbar",
                (ActionGroup) actionManager.getAction("small-toolbar"),
                true);
        super.setToolbar(actionToolbar.getComponent());
        verticalPanel.add(labelInitialLog);
        super.setContent(scrollPane);
    }

    public void addVulnerabilityToContent(VulnerabilityLine vulnerability, FileEditorManager manager, VirtualFile vf) {
        verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));

        if(verticalPanel.getComponentCount() == 1 && verticalPanel.getComponent(0).equals(this.labelInitialLog)){
            verticalPanel.remove(labelInitialLog);
        }

        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        rowPanel.setPreferredSize(new Dimension(super.getContent().getWidth()  , 50));
        rowPanel.setMaximumSize(new JBDimension(10000, 50));
        rowPanel.setSize(super.getContent().getWidth(), 50);


        JPanel linePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linePanel.add(new JBLabel("Line: " + vulnerability.getLine()));
        rowPanel.add(linePanel);


        JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        descriptionPanel.add(new JBLabel("<html>"+vulnerability.getDescription()+"...</html>"));
        rowPanel.add(descriptionPanel);


        JPanel severityPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        severityPanel.add(new JBLabel("Severity: " + vulnerability.getSeverity()));
        rowPanel.add(severityPanel);

        verticalPanel.add(rowPanel);

        PluginToolWindowLogWritingAnalysis thisReference = this;
        rowPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                FileEditor[] editor = manager.openFile(vf, true);

                if (editor.length > 0 && editor[0] instanceof TextEditor) {
                    LogicalPosition problemPos = new LogicalPosition(
                            Math.max(vulnerability.getLine() - 1, 0), Math.max(1, 0));

                    Editor textEditor = ((TextEditor) editor[0]).getEditor();
                    textEditor.getCaretModel().moveToLogicalPosition(problemPos);
                    textEditor.getScrollingModel().scrollToCaret(ScrollType.CENTER);

                    FramePiuDettagliAnalysisLine frame = new FramePiuDettagliAnalysisLine(vulnerability, rowPanel, thisReference);

                    frame.setVisible(true);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                rowPanel.setBackground(UIManager.getColor("ActionButton.hoverBackground"));
                descriptionPanel.setBackground(UIManager.getColor("ActionButton.hoverBackground"));
                linePanel.setBackground(UIManager.getColor("ActionButton.hoverBackground"));
                severityPanel.setBackground(UIManager.getColor("ActionButton.hoverBackground"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                rowPanel.setBackground(UIManager.getColor("Panel.background"));
                descriptionPanel.setBackground(UIManager.getColor("Panel.background"));
                linePanel.setBackground(UIManager.getColor("Panel.background"));
                severityPanel.setBackground(UIManager.getColor("Panel.background"));
            }
        });


        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        super.setContent(scrollPane);
    }

    public void removeVulnerability(JPanel panel){
        verticalPanel.remove(panel);
    }

}
