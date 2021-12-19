package ru.kpfu.itis.bagautdinov.server;

public interface ConnectionStatus {

    void onReceiveObject(Object object);

    void onDisconnect(ThreadConnection connection);

    void onException(ThreadConnection connection, Exception e);

}
