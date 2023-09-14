package it.unisa.zammarrelli.digitaltwinforcybersecurity.gui;

import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.Vulnerability;

import javax.swing.tree.DefaultMutableTreeNode;


public class PersonalizedTreeNode extends DefaultMutableTreeNode {
    private Vulnerability vulnerability;

    public PersonalizedTreeNode(Object userObject, Vulnerability vulnerability) {
        super(userObject);
        this.vulnerability = vulnerability;
    }

    public Vulnerability getVulnerability() {
        return vulnerability;
    }

    public void setVulnerability(Vulnerability vulnerability) {
        this.vulnerability = vulnerability;
    }
}
