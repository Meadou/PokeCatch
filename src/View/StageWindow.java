package View;

import Logic.GameState;
import Logic.Stage;
import Logic.Util;
import ui.PokeGamePanel;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

public class StageWindow {
    public static Scanner sc = new Scanner(System.in);
    public PokeGamePanel game;
    public static Util u = new Util();
    public static Stage stage1 = new Stage("grass", u.initializeStage1Pokemon(), true);
    public static Stage stage2 = new Stage("cave", u.initializeStage2Pokemon(), false);
    public static Stage stage3 = new Stage("ocean", u.initializeStage3Pokemon(), false);
    public static Stage stage4 = new Stage("lava", u.initializeStage4Pokemon(), false);
     public static void main(String[] args) {
        new StageSelectionPlaceholder();

        int choice = StageSelectionPlaceholder.choice;

        

    }

    public static void stageSelector(int stageNum) {
            switch(stageNum) {
            case 1 -> {
                GameState.setCurrenStage(stage1);
                new PokeGamePanel(stage1);
            }
            case 2 -> {
                GameState.setCurrenStage(stage2);
                new PokeGamePanel(stage2);
            }
            case 3 -> {
                GameState.setCurrenStage(stage2);
                new PokeGamePanel(stage3);
            }
            case 4 -> {
                GameState.setCurrenStage(stage1);
                new PokeGamePanel(stage4);
            }
            case 5 -> exit();
            default -> exit();
        }
    }

    public static void exit() {
        System.exit(0);
    }

    public static void nextStage(Stage currStage) {
        switch (currStage.stageName) {
            case "grass" -> {
                stage2.isUnlocked = true;
                new PokeGamePanel(stage2);
            }
            case "cave" -> {
                stage3.isUnlocked = true;
                new PokeGamePanel(stage3);
            }
            case "ocean" -> {
                stage4.isUnlocked = true;
                new PokeGamePanel(stage4);
            }
            default -> new PokeGamePanel(stage1);
        }
    }
}

