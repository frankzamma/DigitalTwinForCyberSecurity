package it.unisa.zammarrelli.digitaltwinforcybersecurity.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PluginSettingsConfigurable implements Configurable {

    private PluginSettingsComponent pluginSettingsComponent;

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Digital Twin For CyberSecurity";
    }

    @Override
    public @Nullable JComponent createComponent() {
        pluginSettingsComponent = new PluginSettingsComponent();
        return pluginSettingsComponent.getMainPanel();
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return pluginSettingsComponent.getPreferredFocusedComponent();
    }

    @Override
    public boolean isModified() {
        PluginSettingsState settingsState = PluginSettingsStateService.getInstance().getState();

        return (settingsState.getLanguage() != pluginSettingsComponent.getLanguage())
                || (!settingsState.getGptToken().equals(pluginSettingsComponent.getGptToken()));
    }
    @Override
    public void apply() throws ConfigurationException {
        PluginSettingsState settingsState = PluginSettingsStateService.getInstance().getState();

        settingsState.setLanguage(pluginSettingsComponent.getLanguage());
        settingsState.setGptToken(pluginSettingsComponent.getGptToken());
    }

    @Override
    public void reset() {
        PluginSettingsState settingsState =  PluginSettingsStateService.getInstance().getState();

        pluginSettingsComponent.setGptToken(settingsState.getGptToken());
        pluginSettingsComponent.setLanguage(settingsState.getLanguage());
    }
}
