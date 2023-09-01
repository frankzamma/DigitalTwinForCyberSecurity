package it.unisa.zammarrelli.digitaltwinforcybersecurity.settings;

import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.Language;

public class PluginSettingsState {
    private String gptToken;
    private Language language;

    private boolean dynamicAnalysis;

    public PluginSettingsState() {
        this.language = Language.ITALIAN;

        this.gptToken = "";
        this.dynamicAnalysis = false;
    }

    public String getGptToken() {
        return gptToken;
    }

    public void setGptToken(String gptToken) {
        this.gptToken = gptToken;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public boolean isDynamicAnalysis() {
        return dynamicAnalysis;
    }

    public void setDynamicAnalysis(boolean dynamicAnalysis) {
        this.dynamicAnalysis = dynamicAnalysis;
    }
}
