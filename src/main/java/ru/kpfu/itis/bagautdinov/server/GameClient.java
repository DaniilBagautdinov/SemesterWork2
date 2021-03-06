package ru.kpfu.itis.bagautdinov.server;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.Socket;
import java.util.ArrayList;

public class GameClient extends Application implements ConnectionStatus {
    private Button[][] buttons;
    public Label winnerLabel;
    public Label resultLabel;
    private String[][] btns;
    private static final char crossSymbol = 'X';
    private static final char zeroSymbol = 'O';

    public int turnCount = 0;

    public int XTurnCount;
    public int OTurnCount;

    private static final ArrayList<Character> turns = new ArrayList<>();
    private ThreadConnection connection;

    public static void main(String[] args) {
        turns.add(crossSymbol);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Тут нужно указать айпишник
        Socket socket = new Socket("192.168.1.35", 8080);
        connection = new ThreadConnection(GameClient.this, socket);

        resultLabel = new Label();
        winnerLabel = new Label();
        btns = new String[3][3];
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500),
                event -> {
                    for (int i = 0; i < btns.length; i++) {
                        for (int j = 0; j < btns[i].length; j++) {
                            if (btns[i][j] != null) {
                                buttons[i][j].setText(btns[i][j]);
                                turnCount++;
                                String winner = check(buttons);
                                if (!winner.equals("")) {
                                    winnerLabel.setId("winner");
                                    winnerLabel.setText("Victory");
                                    resultLabel.setId(winner);
                                    resultLabel.setText(winner);
                                } else if (XTurnCount == 5 || OTurnCount == 5) {
                                    winnerLabel.setId("winner");
                                    winnerLabel.setText("Draw");
                                    resultLabel.setId(winner);
                                    resultLabel.setText(winner);
                                }
                            }
                        }
                    }
                });

        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(-1);
        timeline.play();

        createWindow(primaryStage);
    }

    public void createWindow(Stage primaryStage) {
        VBox root = new VBox();
        root.setId("root");
        HBox top = new HBox();
        top.setId("top");
        Button quit = new Button("Quit");
        quit.setId("quit");

        GridPane grid = new GridPane();
        grid.setId("grid");

        buttons = new Button[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int x = i, y = j;
                buttons[i][j] = new Button("");
                grid.add(buttons[i][j], j, i);
                buttons[i][j].setPrefWidth(70);
                buttons[i][j].setPrefHeight(70);

                buttons[i][j].setOnAction(e -> {
                    if (resultLabel.getText().equals("")) {
                        if (resultLabel.getText().equals("")) {
                            if (buttons[x][y].getText().equals("")) {
                                connection.sendObject(x * 3 + y);
                            }
                        }
                    }
                });


            }
        }


        quit.setOnAction(e -> System.exit(0));

        quit.setPrefWidth(100);
        HBox.setMargin(quit, new Insets(10, 0, 10, 70));
        VBox.setMargin(grid, new Insets(20, 20, 20, 20));
        top.getChildren().addAll(quit);
        winnerLabel.setAlignment(Pos.CENTER);
        winnerLabel.setPrefWidth(70);
        VBox.setMargin(winnerLabel, new Insets(0, 0, 0, 90));
        resultLabel.setPrefWidth(70);
        resultLabel.setPrefHeight(70);
        resultLabel.setAlignment(Pos.CENTER);
        VBox.setMargin(resultLabel, new Insets(0, 0, 0, 90));
        root.getChildren().addAll(top, grid, winnerLabel, resultLabel);
        primaryStage.setTitle("Tictactoe");
        Scene scene = new Scene(root, 250, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static String check(Button[][] buttons) {
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(buttons[i][1].getText())
                    && buttons[i][1].getText().equals(buttons[i][2].getText())
                    && !buttons[i][2].getText().equals(""))
                return (buttons[i][0].getText());
            else if (buttons[0][i].getText().equals(buttons[1][i].getText())
                    && buttons[1][i].getText().equals(buttons[2][i].getText()) &&
                    !buttons[2][i].getText().equals(""))
                return (buttons[0][i].getText());
        }
        if ((buttons[0][0].getText().equals(buttons[1][1].getText())
                && buttons[1][1].getText().equals(buttons[2][2].getText()))
                || (buttons[0][2].getText().equals(buttons[1][1].getText())
                && buttons[1][1].getText().equals(buttons[2][0].getText()))
                && !buttons[1][1].getText().equals(""))
            return (buttons[1][1].getText());
        return ("");
    }

    @Override
    public void onReceiveObject(Object object) {
        Integer a = (Integer) object;
        int readInt = a % 10;
        int i = readInt / 3;
        int j = readInt % 3;

        if (a / 10 == 1) {
            btns[i][j] = (String.valueOf(crossSymbol));
            XTurnCount++;

        } else {
            btns[i][j] = (String.valueOf(zeroSymbol));
            OTurnCount++;
        }
    }

    @Override
    public void onDisconnect(ThreadConnection connection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onException(ThreadConnection connection, Exception e) {
        throw new UnsupportedOperationException();
    }
}
