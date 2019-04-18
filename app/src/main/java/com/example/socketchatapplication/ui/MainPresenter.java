package com.example.socketchatapplication.ui;

import android.util.Log;

import com.example.socketchatapplication.models.MessageModel;
import com.example.socketchatapplication.network.ChatListener;
import com.example.socketchatapplication.network.ChatManager;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter implements MainContractor.Presenter, ChatListener {
    private static final String TAG = "MainPresenter";

    private MainContractor.View mainView;
    private List<MessageModel> listMessages;
    private ChatManager chatManager;
    private String userName;

    public MainPresenter(MainContractor.View mainView, String userName) {
        this.mainView = mainView;
        this.userName = userName;
        this.listMessages = new ArrayList<>();
        this.chatManager = new ChatManager(this.userName, this);
    }

    // chatListener methods
    @Override
    public void onOwnMessage(String userName, String message) {
        Log.i(TAG, "onOwnMessage: " +userName + " ownMessage: " + message);
    }

    @Override
    public void onNewMessage(String userName, String message) {
        Log.i(TAG, "onNewMessage: " +userName + " newMessage: " + message);
    }

    @Override
    public void onConnect() {
        Log.i(TAG, "onConnect: ");
        this.chatManager.sendMessage(this.userName, "Hi! My name is " + this.userName + " I am connected now!" );
    }

    @Override
    public void onDisconnect() {
        Log.i(TAG, "onDisconnect: ");
        this.chatManager.sendMessage(this.userName, "My name is " + this.userName + " I am disconnected now!" );
    }

    @Override
    public void onConnectError(String errorMessage) {
        Log.i(TAG, "onConnectError: " + errorMessage);
    }

    @Override
    public void onConnectTimeout() {
        Log.i(TAG, "onConnectTimeout: ");
    }

    // presenter methods
    @Override
    public void attachView(MainContractor.View view) {
        Log.i(TAG, "attachView: ");
        this.mainView = view;
        this.mainView.setDataToView(this.listMessages);
        if (this.chatManager != null)
            this.chatManager.subscribe();
    }

    @Override
    public void detachView() {
        Log.i(TAG, "detachView: ");
        if (this.chatManager != null)
            this.chatManager.unsubscribe();


    }

    @Override
    public void sendOwnMessage(String message) {
        this.chatManager.sendMessage(this.userName, message);
    }
}
