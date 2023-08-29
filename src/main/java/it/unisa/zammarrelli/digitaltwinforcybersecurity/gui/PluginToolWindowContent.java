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
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ColorChooserService;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.UIUtil;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.Vulnerability;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class PluginToolWindowContent extends SimpleToolWindowPanel {
    private final ActionManager actionManager = ActionManager.getInstance();
    private ActionToolbar actionToolbar;
    public PluginToolWindowContent(boolean vertical, boolean borderless) {
        super(vertical, borderless);
        actionToolbar = actionManager.createActionToolbar("toolbar",
                (ActionGroup) actionManager.getAction("toolbarPlugin"),
                true);
        super.setToolbar(actionToolbar.getComponent());
        super.setContent(new JPanel());
        revalidate();

    }

    public void addVulnerabilitiesContent(List<Vulnerability> vulnerabilities, FileEditorManager manager, VirtualFile vf) {
        JPanel verticalPanel =  new JPanel();
        verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));
        JBScrollPane scrollPane = new JBScrollPane(verticalPanel);
        for(Vulnerability v: vulnerabilities){
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new BorderLayout());
            rowPanel.setPreferredSize(new Dimension(super.getContent().getWidth() -1 , 50));
            rowPanel.setSize(rowPanel.getWidth(), 50);
            JPanel panelNorth =  new JPanel();
            panelNorth.setLayout( new GridLayout(1, 2));

            JBLabel labelName = new JBLabel(v.getName());
            panelNorth.add(labelName);

            rowPanel.add(panelNorth, BorderLayout.NORTH);

            JBLabel labelLine = new JBLabel("Line: " + Integer.toString( v.getLine()));
            panelNorth.add(labelLine);

            JButton button = new JButton("Dettagli");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                  JPanel panelDetails = new JPanel(new GridLayout(3, 1));

                  JPanel panelName =  new JPanel(new FlowLayout(FlowLayout.LEFT));
                  panelName.add(new JBLabel("Name:"));
                  panelName.add(new JBLabel(v.getName()));

                  panelDetails.add(panelName);

                  JPanel panelDescription = new JPanel(new FlowLayout(FlowLayout.LEFT));
                  panelDescription.add(new JBLabel("Description:"));
                  panelDescription.add(new JBLabel(v.getDescription()));

                  panelDetails.add(panelDescription);

                  JPanel panelLine = new JPanel(new FlowLayout(FlowLayout.LEFT));
                  panelLine.add(new JBLabel("Line:"));
                  panelLine.add(new JBLabel(Integer.toString(v.getLine())));

                  panelDetails.add(panelLine);

                  JBPopup popup = JBPopupFactory.getInstance()
                          .createComponentPopupBuilder(panelDetails, null).createPopup();

                  popup.setCaption("Vulnerability " + v.getName());

                  popup.showInFocusCenter();

                }
            });
            JPanel panelEast =  new JPanel();
            panelEast.add(button);
            rowPanel.add(panelEast, BorderLayout.EAST);


            verticalPanel.add(rowPanel);

            rowPanel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    final FileEditor[] editor = manager.openFile(
                            vf, true);

                    if (editor.length > 0 && editor[0] instanceof TextEditor) {
                        final LogicalPosition problemPos = new LogicalPosition(
                                Math.max(v.getLine() - 1, 0), Math.max(1, 0));

                        final Editor textEditor = ((TextEditor) editor[0]).getEditor();
                        textEditor.getCaretModel().moveToLogicalPosition(problemPos);
                        textEditor.getScrollingModel().scrollToCaret(ScrollType.CENTER);
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
                    panelNorth.setBackground(UIManager.getColor("ActionButton.hoverBackground"));
                    panelEast.setBackground(UIManager.getColor("ActionButton.hoverBackground"));

                }

                @Override
                public void mouseExited(MouseEvent e) {
                    rowPanel.setBackground(UIManager.getColor("Panel.background"));
                    panelNorth.setBackground(UIManager.getColor("Panel.background"));
                    panelEast.setBackground(UIManager.getColor("Panel.background"));
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
}
