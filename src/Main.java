public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to PokeCatch!");
        System.out.println("marjhun gaming");

        var introScreen = new Intro();
        var music = new MusicPlayer();

        music.playLoop("/Music/pallet_town_theme.wav");

        introScreen.setStarterSelectionListener(starterId -> {
            System.out.println("Starter ID selected after intro: " + starterId);
            music.stop();

            /*
            insert shi diria aron ma play after sa Intro
            call the class and methods after sa Intro diri
            */

        });

        introScreen.launchIntro();

        // task 1 marjhun pamilar 
        // task 1 marjhun
        // task 2 Charmander Shiny
        // task 3 Shiny Magicarp
        // task 4 bulbasaur
    }
}