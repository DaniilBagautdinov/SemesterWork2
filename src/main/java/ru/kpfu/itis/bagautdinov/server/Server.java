package ru.kpfu.itis.bagautdinov.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server implements ConnectionStatus {

    private boolean yourTurn = true;
    private static ThreadConnection player1;
    private static ThreadConnection player2;

    public static void main(String[] args) {
        new Server();
    }

    public Server() {
        try (ServerSocket serverSocket = new ServerSocket(22222)) {
            try {
                player1 = new ThreadConnection(this, serverSocket.accept());
                player2 = new ThreadConnection(this, serverSocket.accept());
            } catch (IOException e) {
                System.out.println("TCPConnection exception: " + e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onReceiveObject(Object object) {
        Integer a = (Integer) object;
        int turn;
        if (yourTurn) {
            turn = 10 + a;
        } else {
            turn = 20 + a;
        }
        player1.sendObject(turn);
        player2.sendObject(turn);
        yourTurn = !yourTurn;
    }

    @Override
    public void onDisconnect(ThreadConnection tcpConnection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onException(ThreadConnection connection, Exception e) {
        throw new UnsupportedOperationException();
    }
}
