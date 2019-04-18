package com.example.socketchatapplication.network;

public interface ChatListener {

    void onOwnMessage(String userName, String message);

    void onNewMessage(String userName, String message);

    void onConnect();

    void onDisconnect();

    void onConnectError(String errorMessage);

    void onConnectTimeout();

}
