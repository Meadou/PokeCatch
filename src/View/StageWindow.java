package View;

import Logic.Stage;
import Logic.Util;
import ui.PokeGamePanel;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StageWindow {

    public interface StageCompleteListener {
        void onStageCleared(int stageNumber);
    }

    private final int stageNumber;
    private final StageCompleteListener listener;

    public StageWindow(int stageNumber, StageCompleteListener listener) {
        this.stageNumber = stageNumber;
        this.listener = listener;
        
        // Create stage based on stage number
        Stage stage = createStageForNumber(stageNumber);
        
        // Create and configure the game panel
        PokeGamePanel gamePanel = new PokeGamePanel(stage, () -> {
            // Callback when game ends (time up or bomb clicked)
            if (this.listener != null) this.listener.onStageCleared(this.stageNumber);
        });
        
        // Handle window close
        gamePanel.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (StageWindow.this.listener != null) {
                    StageWindow.this.listener.onStageCleared(StageWindow.this.stageNumber);
                }
            }
        });
    }

    private Stage createStageForNumber(int stageNum) {
        Util util = new Util();
        
        switch (stageNum) {
            case 1:
                return new Stage("Grass", util.initializeStage1Pokemon(), true);
            case 2:
                return new Stage("Rock", util.initializeStage2Pokemon(), false);
            case 3:
                return new Stage("Ocean", util.initializeStage3Pokemon(), false);
            case 4:
                return new Stage("Snow", util.initializeStage4Pokemon(), false);
            case 5:
                return new Stage("Swamp", util.initializeStage5Pokemon(), false);
            case 6:
                return new Stage("Lava", util.initializeStage6Pokemon(), false);
            default:
                return new Stage("Grass", util.initializeStage1Pokemon(), true);
        }
    }
}
