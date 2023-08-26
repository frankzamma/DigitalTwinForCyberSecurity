package it.unisa.zammarrelli.digitaltwinforcybersecurity.gui;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.ColorChooserService;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.UIUtil;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.Vulnerability;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class PluginToolWindowContent extends SimpleToolWindowPanel {
    private static final JPanel content = new JPanel();
    private final ActionManager actionManager = ActionManager.getInstance();
    private ActionToolbar actionToolbar;
    public PluginToolWindowContent(boolean vertical, boolean borderless) {
        super(vertical, borderless);
        actionToolbar = actionManager.createActionToolbar("toolbar",
                (ActionGroup) actionManager.getAction("toolbarPlugin"),
                true);
        super.setToolbar(actionToolbar.getComponent());
        super.setContent(content);
        revalidate();

    }

    public JPanel getContent() {
        return content;
    }
    public void addVulnerabilitiesContent(List<Vulnerability> vulnerabilities) {
        JPanel verticalPanel =  new JPanel();
        verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));
        JBScrollPane scrollPane = new JBScrollPane(verticalPanel);
        content.add(scrollPane);
        for(Vulnerability v: vulnerabilities){
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new BorderLayout());
            rowPanel.setPreferredSize(new Dimension(super.getContent().getWidth() -1 , 50));

            JPanel panelNorth =  new JPanel();
            panelNorth.setLayout( new GridLayout(1, 2));

            JBLabel labelName = new JBLabel(v.getName());
            panelNorth.add(labelName);

            rowPanel.add(panelNorth, BorderLayout.NORTH);

            JBLabel labelLine = new JBLabel("Line: " + Integer.toString( v.getLine()));
            panelNorth.add(labelLine);

            JButton button = new JButton("Dettagli");
            rowPanel.add(button, BorderLayout.EAST);


            verticalPanel.add(rowPanel);

            rowPanel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    rowPanel.setBackground(Color.LIGHT_GRAY);
                    panelNorth.setBackground(Color.LIGHT_GRAY);

                }

                @Override
                public void mouseExited(MouseEvent e) {
                    rowPanel.setBackground(UIManager.getColor("Panel.background"));
                    panelNorth.setBackground(UIManager.getColor("Panel.background"));

                }
            });
        }

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        super.setContent(scrollPane);
    }
}
