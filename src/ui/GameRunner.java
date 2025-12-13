package ui;

import java.util.Scanner;

import Logic.*;

public class GameRunner {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PokeGamePanel game;
        Util u = new Util();
        Stage stage1 = new Stage("Grass", u.initializeStage1Pokemon(), true);
        Stage stage2 = new Stage("Rock", u.initializeStage2Pokemon(), false);
        Stage stage3 = new Stage("Ocean", u.initializeStage3Pokemon(), false);
        Stage stage4 = new Stage("Snow", u.initializeStage4Pokemon(), false);
        Stage stage5 = new Stage("Swamp", u.initializeStage5Pokemon(), false);
        Stage stage6 = new Stage("Lava", u.initializeStage6Pokemon(), false);

        System.out.println("""
                Pick a stage:
                [1] Grass
                [2] Rock
                [3] Ocean
                [4] Snow
                [5] Swamp
                [6] Lava
                [7] Exit
                """);
        System.out.print("> ");
        int choice = sc.nextInt();

        switch(choice) {
            case 1 -> new PokeGamePanel(stage1);
            case 2 -> new PokeGamePanel(stage2);
            case 3 -> new PokeGamePanel(stage3);
            case 4 -> new PokeGamePanel(stage4);
            case 5 -> new PokeGamePanel(stage5);
            case 6 -> new PokeGamePanel(stage6);
            case 7 -> exit();
            default -> exit();
        }

    }

    public static void exit() {
        System.exit(0);
    }
}
