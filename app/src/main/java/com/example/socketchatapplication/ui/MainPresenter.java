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

    public MainPresenter(String userName) {
        this.userName = userName;
        this.listMessages = new ArrayList<>();
        this.chatManager = new ChatManager(this.userName, this);
    }

    // chatListener methods
    @Override
    public void onOwnMessage(String userName, String message) {
        Log.i(TAG, "onOwnMessage: " + userName + " ownMessage: " + message);
        this.addMessageToList(userName, message);
    }

    @Override
    public void onNewMessage(String userName, String message) {
        Log.i(TAG, "onNewMessage: " + userName + " newMessage: " + message);
        this.addMessageToList(userName, message);
    }

    private void addMessageToList(String userName, String message) {
        if (listMessages != null)
            listMessages.add(new MessageModel(userName, message));
        if (mainView != null)
            mainView.setDataToView(listMessages);
    }

    @Override
    public void onConnect() {
        Log.i(TAG, "onConnect: ");
        chatManager.sendMessage(userName, "Hi! My name is " + userName + " I am connected now!");
    }

    @Override
    public void onDisconnect() {
        Log.i(TAG, "onDisconnect: ");
        chatManager.sendMessage(userName, "My name is " + userName + " I am disconnected now!");
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
        mainView = view;
        mainView.setDataToView(listMessages);
        if (chatManager != null)
            chatManager.subscribe();
    }

    @Override
    public void detachView() {
        Log.i(TAG, "detachView: ");
        if (chatManager != null)
            chatManager.unsubscribe();
    }

    @Override
    public void sendOwnMessage(String message) {
        chatManager.sendMessage(userName, message);
    }
}
