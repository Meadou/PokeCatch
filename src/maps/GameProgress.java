package maps;

public class GameProgress {

    private final ChooseMap chooseMap;

    public boolean grassUnlocked = true;
    public boolean caveUnlocked  = false;
    public boolean oceanUnlocked = false;
    public boolean lavaUnlocked  = false;

    public GameProgress(ChooseMap mapScreen) {
        this.chooseMap = mapScreen;
    }

    public void updateMaps() {
        for (ChooseMap.ImagePanel panel : chooseMap.getMapPanels()) {
            switch (panel.getName()) {
                case "grass" -> panel.setUnlocked(grassUnlocked);
                case "cave"  -> panel.setUnlocked(caveUnlocked);
                case "ocean" -> panel.setUnlocked(oceanUnlocked);
                case "lava"  -> panel.setUnlocked(lavaUnlocked);
            }
        }
    }
}
