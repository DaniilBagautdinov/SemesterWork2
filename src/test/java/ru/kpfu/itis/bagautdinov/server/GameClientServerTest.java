package ru.kpfu.itis.bagautdinov.server;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

public class GameClientServerTest {
    @Test
    @DisplayName("Тестирование работы программы")
    public void testGame() {
        Thread thread = new Thread(() -> {
            new JFXPanel();
            Platform.runLater(() -> {
                try {
                    new Server();
                    new GameClient().start(new Stage());
                    new GameClient().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
