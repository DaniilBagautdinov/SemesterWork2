package ru.kpfu.itis.bagautdinov.server;

public interface ConnectionStatus {

    void onConnectionReady(ThreadConnection connection);

    void onReceiveObject(ThreadConnection connection, Object object);

    void onDisconnect(ThreadConnection connection);

    void onException(ThreadConnection connection, Exception e);

}
