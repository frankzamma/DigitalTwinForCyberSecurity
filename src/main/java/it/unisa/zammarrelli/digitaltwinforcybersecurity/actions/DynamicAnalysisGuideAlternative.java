package it.unisa.zammarrelli.digitaltwinforcybersecurity.actions;

import it.unisa.zammarrelli.digitaltwinforcybersecurity.ai.GPTWrapper;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.Language;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.gui.FrameDynamicAnalysisGuide;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.settings.PluginSettingsState;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.settings.PluginSettingsStateService;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DynamicAnalysisGuideAlternative extends MouseAdapter {
    private String programmingLanguage;

    public DynamicAnalysisGuideAlternative(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);

        PluginSettingsState settingsState = PluginSettingsStateService.getInstance().getState();

        String gptToken = settingsState.getGptToken();
        Language language = settingsState.getLanguage();

        FrameDynamicAnalysisGuide frameDynamicAnalysisGuide = new FrameDynamicAnalysisGuide();

        Thread t = new Thread(()->{
            GPTWrapper gptWrapper = new GPTWrapper(gptToken, language);

            String guide = gptWrapper.getGuideForDynamicAnalysis(programmingLanguage);
            System.out.println(guide);
            frameDynamicAnalysisGuide.createPanel(guide);
        });

        frameDynamicAnalysisGuide.setVisible(true);
        frameDynamicAnalysisGuide.setThread(t);

        t.start();
    }
}
