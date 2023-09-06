package it.unisa.zammarrelli.digitaltwinforcybersecurity.gui;


import com.intellij.ui.AnimatedIcon;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;

import it.unisa.zammarrelli.digitaltwinforcybersecurity.util.WindowListerForDynamicAnalysisFrame;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;

public class FrameDynamicAnalysisGuide extends JFrame {
    private JPanel content;
    public FrameDynamicAnalysisGuide() throws HeadlessException {
        this.content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));

        JBLabel labelIcon = new JBLabel();
        AnimatedIcon icon = new AnimatedIcon.Big();
        labelIcon.setIcon(icon);
        content.add(labelIcon);

        JBLabel labelLoadingText = new JBLabel("Loading...");
        content.add(labelLoadingText);
        super.add(content);
        super.pack();
        super.setLocationRelativeTo(null);
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.addWindowListener(new WindowListerForDynamicAnalysisFrame());
    }

    public void createPanel(String guideHtml){

        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
        Document document = htmlEditorKit.createDefaultDocument();
        editorPane.setDocument(document);
        editorPane.setContentType("text/html");
        editorPane.setText(guideHtml);

        JBScrollPane scrollPane = new JBScrollPane(editorPane);
        super.remove(content);
        super.add(scrollPane);
        super.pack();
        super.setLocationRelativeTo(null);
    }

    public void setThread(Thread thread) {
        super.addWindowListener(new WindowListerForDynamicAnalysisFrame(thread));
    }
}
