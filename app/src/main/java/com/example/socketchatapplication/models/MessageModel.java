package com.example.socketchatapplication.models;

public class MessageModel {

    private String user;
    private String message;

    public MessageModel(String userName, String message) {
        this.user = userName;
        this.message = message;
    }

    public String getUserName() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}
