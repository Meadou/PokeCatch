package Logic;

public class GameState {

    private static GameState instance;

    private int globalScore;

    private GameState() {
        globalScore = 0;
    }

    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public void addScore(int points) {
        globalScore += points;
    }

    public int getGlobalScore() {
        return globalScore;
    }

    public void reset() {
        globalScore = 0;
    }
}

