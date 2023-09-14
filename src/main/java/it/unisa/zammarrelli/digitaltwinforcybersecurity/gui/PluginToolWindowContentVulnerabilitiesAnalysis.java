package it.unisa.zammarrelli.digitaltwinforcybersecurity.gui;


import com.intellij.icons.AllIcons;
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
import com.intellij.ui.AnimatedIcon;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBDimension;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.Vulnerability;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class PluginToolWindowContentVulnerabilitiesAnalysis extends SimpleToolWindowPanel {
    private final ActionManager actionManager = ActionManager.getInstance();
    private ActionToolbar actionToolbar;

    private final JBLabel fileCountLabel =  new JBLabel();
    public PluginToolWindowContentVulnerabilitiesAnalysis(boolean vertical, boolean borderless) {
        super(vertical, borderless);
        actionToolbar = actionManager.createActionToolbar("toolbar",
                (ActionGroup) actionManager.getAction("toolbarPlugin"),
                true);
        super.setToolbar(actionToolbar.getComponent());
        super.setContent(new JPanel());
        revalidate();

    }

    public void addVulnerabilitiesContent(List<Vulnerability> vulnerabilities, FileEditorManager manager) {
        JPanel verticalPanel =  new JPanel();
        verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));
        JBScrollPane scrollPane = new JBScrollPane(verticalPanel);
        for(Vulnerability v: vulnerabilities){
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
            rowPanel.setPreferredSize(new Dimension(super.getContent().getWidth()  , 50));
            rowPanel.setSize(super.getContent().getWidth(), 50);
            rowPanel.setMaximumSize(new JBDimension(10000, 50));

            JPanel panelName = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JBLabel labelName = new JBLabel(v.getName());
            panelName.add(labelName);
            rowPanel.add(panelName);

            JPanel panelLine = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JBLabel labelLine = new JBLabel("Line: " +  v.getLine());
            panelLine.add(labelLine);
            rowPanel.add(panelLine);

            JPanel panelSeverity = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JBLabel labelSeverity = new JBLabel("Severity: " + v.getSeverity());
            panelSeverity.add(labelSeverity);
            rowPanel.add(panelSeverity);

            JPanel panelFile = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelFile.add(new JBLabel("File: " + v.getVirtualFile().getName()));
            rowPanel.add(panelFile);


            verticalPanel.add(rowPanel);
            rowPanel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    FileEditor[] editor = manager.openFile(v.getVirtualFile(), true);

                    if (editor.length > 0 && editor[0] instanceof TextEditor) {
                        LogicalPosition problemPos = new LogicalPosition(
                                Math.max(v.getLine() - 1, 0), Math.max(1, 0));

                        Editor textEditor = ((TextEditor) editor[0]).getEditor();
                        textEditor.getCaretModel().moveToLogicalPosition(problemPos);
                        textEditor.getScrollingModel().scrollToCaret(ScrollType.CENTER);
                        FrameDetailsVulnerabilityAnlysis frameDetailsVulnerabilityAnlysis = new FrameDetailsVulnerabilityAnlysis(v);
                        frameDetailsVulnerabilityAnlysis.setVisible(true);
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
                    panelSeverity.setBackground(UIManager.getColor("ActionButton.hoverBackground"));
                    panelLine.setBackground(UIManager.getColor("ActionButton.hoverBackground"));
                    panelName.setBackground(UIManager.getColor("ActionButton.hoverBackground"));
                    panelFile.setBackground(UIManager.getColor("ActionButton.hoverBackground"));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    rowPanel.setBackground(UIManager.getColor("Panel.background"));
                    panelSeverity.setBackground(UIManager.getColor("Panel.background"));
                    panelLine.setBackground(UIManager.getColor("Panel.background"));
                    panelName.setBackground(UIManager.getColor("Panel.background"));
                    panelFile.setBackground(UIManager.getColor("Panel.background"));

                }
            });
        }

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        super.setContent(scrollPane);
    }

    public void displayError(String error){
        JPanel panel = new JPanel();

        JBLabel icon = new JBLabel(UIManager.getIcon("OptionPane.errorIcon"));
        JBLabel errorLabel = new JBLabel( "Errore: " + error);
        errorLabel.setCopyable(true);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(icon);
        panel.add(errorLabel);
        super.setContent(panel);
    }

    public void displayInformation(String information){
        JPanel panel = new JPanel();

        JBLabel icon = new JBLabel(UIManager.getIcon("OptionPane.informationIcon"));
        JBLabel errorLabel = new JBLabel( "Informazione: " + information);

        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(icon);
        panel.add(errorLabel);
        super.setContent(panel);
    }


    public void setLoading(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        AnimatedIcon icon = new AnimatedIcon(
                250,
                AllIcons.Process.Step_1,
                AllIcons.Process.Step_2,
                AllIcons.Process.Step_3,
                AllIcons.Process.Step_4,
                AllIcons.Process.Step_5,
                AllIcons.Process.Step_6,
                AllIcons.Process.Step_7,
                AllIcons.Process.Step_8
                );

        JBLabel label = new JBLabel();
        label.setIcon(icon);
        JBLabel labelMessage = new JBLabel("I'm analyzing");
        panel.add(label);
        panel.add(labelMessage);

        super.setContent(panel);
    }

    public void setLoading(int files){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        AnimatedIcon icon = new AnimatedIcon(
                250,
                AllIcons.Process.Step_1,
                AllIcons.Process.Step_2,
                AllIcons.Process.Step_3,
                AllIcons.Process.Step_4,
                AllIcons.Process.Step_5,
                AllIcons.Process.Step_6,
                AllIcons.Process.Step_7,
                AllIcons.Process.Step_8
        );

        JBLabel label = new JBLabel();
        label.setIcon(icon);
        JBLabel labelMessage = new JBLabel("I'm analyzing");
        JBLabel labelCount =  new JBLabel("of " + files);
        fileCountLabel.setText(" 1 ");
        panel.add(label);

        panel.add(labelMessage);
        panel.add(fileCountLabel);
        panel.add(labelCount);
        super.setContent(panel);
    }

    public void changeCount(int newCount){
        fileCountLabel.setText(" " + newCount + " ");
    }
}
