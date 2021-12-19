package ru.kpfu.itis.bagautdinov.client;


import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

public class GameTest {

    @Test
    @DisplayName("Тестирование работы программы")
    public void testGame() throws InterruptedException {
        Thread thread = new Thread(() -> {
            new JFXPanel();
            Platform.runLater(() -> {
                try {
                    new Main().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        thread.start();
        Thread.sleep(60000);
    }
}
