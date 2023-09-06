package it.unisa.zammarrelli.digitaltwinforcybersecurity.gui;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.HTMLEditorKitBuilder;
import com.intellij.util.ui.JBHtmlEditorKit;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.util.WindowListerForDynamicAnalysisFrame;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class FrameDynamicAnalysisGuide extends JFrame {
    private String guideHtml;
    public FrameDynamicAnalysisGuide(String guideHtml) throws HeadlessException {
        this.guideHtml = guideHtml;

        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.addWindowListener(new WindowListerForDynamicAnalysisFrame());
        createPanel();
    }

    private void createPanel(){
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
        Document document = htmlEditorKit.createDefaultDocument();
        editorPane.setDocument(document);
        editorPane.setContentType("text/html");
        editorPane.setText(guideHtml);

        JBScrollPane scrollPane = new JBScrollPane(editorPane);
        this.add(scrollPane);
        super.pack();
        super.setLocationRelativeTo(null);

    }
}
