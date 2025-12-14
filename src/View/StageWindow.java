package View;

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
        

        System.out.println("""
                Pick a stage:
                [1] Grass
                [2] Rock
                [3] Ocean
                [4] Lava
                [5] Exit
                """);
        System.out.print("> ");
        int choice = sc.nextInt();

        switch(choice) {
            case 1 -> new PokeGamePanel(stage1);
            case 2 -> new PokeGamePanel(stage2);
            case 3 -> new PokeGamePanel(stage3);
            case 4 -> new PokeGamePanel(stage4);
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

