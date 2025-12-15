import javax.swing.*;

public class ExampleUsageMapUnlockAndLock {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // 1. Create the ChooseMap window
            ChooseMap chooseMap = new ChooseMap();
            chooseMap.setVisible(true);

            // 2. Create GameProgress manager with the ChooseMap instance
            GameProgress progress = new GameProgress(chooseMap);

            // 3. Unlock ocean and lava maps
            progress.oceanUnlocked = true;
            progress.lavaUnlocked = true;

            // 4. Apply the changes to the GUI
            progress.updateMaps();
        });
    }
}