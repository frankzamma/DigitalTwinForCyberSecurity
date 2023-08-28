package it.unisa.zammarrelli.digitaltwinforcybersecurity.settings;

import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.Language;

public class PluginSettingsState {
    private String gptToken;
    private Language language;

    public PluginSettingsState() {
        this.language = Language.ITALIAN;

        this.gptToken = "";
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
}
