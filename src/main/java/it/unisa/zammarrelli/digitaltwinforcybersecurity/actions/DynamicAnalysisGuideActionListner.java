package it.unisa.zammarrelli.digitaltwinforcybersecurity.actions;

import it.unisa.zammarrelli.digitaltwinforcybersecurity.ai.GPTWrapper;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.Language;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.gui.FrameDynamicAnalysisGuide;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.settings.PluginSettingsState;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.settings.PluginSettingsStateService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DynamicAnalysisGuideActionListner implements ActionListener {
    private String tool, programmingLanguage;

    public DynamicAnalysisGuideActionListner(String tool, String programmingLanguage) {
        this.tool = tool;
        this.programmingLanguage = programmingLanguage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PluginSettingsState settingsState = PluginSettingsStateService.getInstance().getState();

        String gptToken = settingsState.getGptToken();
        Language language = settingsState.getLanguage();

        FrameDynamicAnalysisGuide frameDynamicAnalysisGuide = new FrameDynamicAnalysisGuide();

        Thread t = new Thread(()->{
            GPTWrapper gptWrapper = new GPTWrapper(gptToken, language);

            String guide = gptWrapper.getGuideForDynamicAnalysis(tool, programmingLanguage);
            System.out.println(guide);
            frameDynamicAnalysisGuide.createPanel(guide);
        });

        frameDynamicAnalysisGuide.setVisible(true);

        t.start();
    }
}
