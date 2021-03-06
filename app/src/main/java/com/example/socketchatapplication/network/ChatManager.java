package com.example.socketchatapplication.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

import static com.example.socketchatapplication.utils.Configuration.BASE_URL;
import static com.example.socketchatapplication.utils.Configuration.KEY_USER_MESSAGE;
import static com.example.socketchatapplication.utils.Configuration.KEY_USER_NAME;
import static io.socket.client.Socket.EVENT_CONNECT;
import static io.socket.client.Socket.EVENT_CONNECT_ERROR;
import static io.socket.client.Socket.EVENT_CONNECT_TIMEOUT;
import static io.socket.client.Socket.EVENT_DISCONNECT;
import static io.socket.client.Socket.EVENT_MESSAGE;

public class ChatManager {
    private static final String TAG = "MainChatManager";
    private String userName;
    private Socket clientSocket;
    private ChatListener chatListener;

    public ChatManager(String userName, ChatListener chatListener) {
        this.userName = userName;
        this.chatListener = chatListener;
    }

    private void initClientSocket() {
        try {
            clientSocket = IO.socket(BASE_URL, new IO.Options());
            clientSocket.connect();
            clientSocket.emit("join", userName);
            Log.i(TAG, "initClientSocket: ");
        } catch (URISyntaxException e) {
            Log.e(TAG, "initClientSocket: ERROR");
            e.printStackTrace();
        }
    }

    public void subscribe() {
        initClientSocket();

        clientSocket.on(EVENT_CONNECT, args -> {
            Log.i(TAG, "call: EVENT_CONNECT");
            chatListener.onConnect();
        });
        clientSocket.on(EVENT_DISCONNECT, args -> {
            Log.i(TAG, "call: EVENT_DISCONNECT");
            chatListener.onDisconnect();
        });
        clientSocket.on(EVENT_MESSAGE, args -> {
            Log.i(TAG, "call: EVENT_MESSAGE");
            JSONObject data = (JSONObject) args[0];
            String nickname = "";
            String message = "";
            if (data != null)
                try {
                    nickname = data.getString(KEY_USER_NAME);
                    message = data.getString(KEY_USER_MESSAGE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            if (!userName.equals(nickname))
                chatListener.onNewMessage(nickname, message);

        });
        clientSocket.on(EVENT_CONNECT_ERROR, args -> {
            Log.i(TAG, "call: EVENT_CONNECT_ERROR");
            Exception exception = (Exception) args[0];
            chatListener.onConnectError(exception.getMessage());
        });
        clientSocket.on(EVENT_CONNECT_TIMEOUT, args -> {
            Log.i(TAG, "call: EVENT_CONNECT_TIMEOUT");
            chatListener.onConnectTimeout();
        });
        Log.i(TAG, "subscribe: ");
    }

    public void unsubscribe() {
        if (clientSocket != null) {
            clientSocket.disconnect();
            Log.i(TAG, "unsubscribe: ");
        }
    }

    public void sendMessage(String userName, String message) {
        this.userName = userName;
        if (message.length() == 0) {
            Log.e(TAG, "sendMessage: message is empty");
            return;
        }
        JSONObject messageJsonObject = new JSONObject();
        try {
            messageJsonObject.put(KEY_USER_NAME, this.userName);
            messageJsonObject.put(KEY_USER_MESSAGE, message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        clientSocket.emit(EVENT_MESSAGE, messageJsonObject);
        chatListener.onOwnMessage(this.userName, message);
    }
}
