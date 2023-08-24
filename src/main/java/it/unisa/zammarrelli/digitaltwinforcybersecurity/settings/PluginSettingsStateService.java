package it.unisa.zammarrelli.digitaltwinforcybersecurity.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "it.unisa.zammarrelli.digitaltwinforcybersecurity.settings.PluginSettingsState",
        storages = @Storage("DigitalTwinForCyberSecurity.xml")
)
public class PluginSettingsStateService implements PersistentStateComponent<PluginSettingsState> {
    private PluginSettingsState state = new PluginSettingsState();

    @Override
    @Nullable
    public PluginSettingsState getState() {
        return this.state;
    }

    @Override
    public void loadState(@NotNull PluginSettingsState state) {
        XmlSerializerUtil.copyBean(state, this.state);
    }

    public static PluginSettingsStateService getInstance() {
        return ApplicationManager.getApplication().getService(PluginSettingsStateService.class);
    }
}
