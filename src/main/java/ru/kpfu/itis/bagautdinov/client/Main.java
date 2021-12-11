package ru.kpfu.itis.bagautdinov.client;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Game game = new Game();
        game.createWindow(stage);
    }
}
