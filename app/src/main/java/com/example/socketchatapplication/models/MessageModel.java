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

    public void setUserName(String userName) {
        this.user = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
