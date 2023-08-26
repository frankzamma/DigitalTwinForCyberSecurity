package it.unisa.zammarrelli.digitaltwinforcybersecurity.settings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.Language;

import javax.swing.*;

public class PluginSettingsComponent {

    private final JPanel mainPanel;
    private final ComboBox<Language> languageComboBox = new ComboBox<>(Language.values());
    private final JBTextField tokenGptTextField = new JBTextField();
    private JLabel labelLanguage, labelToken;


    public PluginSettingsComponent(){
        languageComboBox.setSelectedItem(Language.ITALIAN);
        labelLanguage =  new JLabel();
        labelToken =  new JLabel();
        updateLabel();

        mainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(labelLanguage, languageComboBox,false)
                .addLabeledComponent(labelToken, tokenGptTextField,false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
        languageComboBox.addActionListener(a -> updateLabel());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JComponent getPreferredFocusedComponent(){
        return languageComboBox;
    }

    public Language getLanguage(){
        return Language.values()[languageComboBox.getSelectedIndex()];
    }
    public String getGptToken(){
        return tokenGptTextField.getText();
    }

    private void updateLabel(){
        switch (this.getLanguage()){
            case ITALIAN:
                labelLanguage.setText("Scegliere Lingua");
                labelToken.setText("Inserire Token GPT");
                break;
            case ENGLISH:
                labelLanguage.setText("Select Language");
                labelToken.setText("Insert GPT Token");
                break;
            case FRENCH:
                labelLanguage.setText("Choisir la langue");
                labelToken.setText("Ins\u00E9rer un jeton GPT");
                break;
        }
    }

    public void setLanguage(Language l) {
        languageComboBox.setSelectedItem(l);
    }

    public void setGptToken(String token){
        tokenGptTextField.setText(token);
    }
}
