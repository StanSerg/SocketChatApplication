package com.example.socketchatapplication.network;

public interface ChatListener {

    void onOwnMessage(String message);

    void onNewMessage(String message);
}
