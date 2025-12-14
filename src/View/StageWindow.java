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
    public static Stage stage1 = new Stage("Grass", u.initializeStage1Pokemon(), true);
    public static Stage stage2 = new Stage("Cave", u.initializeStage2Pokemon(), false);
    public static Stage stage3 = new Stage("Ocean", u.initializeStage3Pokemon(), false);
    public static Stage stage4 = new Stage("Lava", u.initializeStage4Pokemon(), false);
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
}

