package ru.kpfu.itis.bagautdinov.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ThreadConnection {
    private final Socket socket;
    private final Thread thread;
    private final ConnectionStatus eventListener;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public ThreadConnection(ConnectionStatus eventListener, Socket socket) throws IOException {
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.eventListener = eventListener;
        this.socket = socket;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!thread.isInterrupted()) {
                        eventListener.onReceiveObject(in.readObject());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    eventListener.onException(ThreadConnection.this, e);
                } finally {
                    eventListener.onDisconnect(ThreadConnection.this);
                }
            }
        });
        thread.start();
    }

    public synchronized void disconnect() {
        thread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onException(ThreadConnection.this, e);
        }
    }

    public synchronized void sendObject(Object object) {
        try {
            out.writeObject(object);
            out.flush();
        } catch (IOException e) {
            eventListener.onException(ThreadConnection.this, e);
            disconnect();
        }
    }
}
