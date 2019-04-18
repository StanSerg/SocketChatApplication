package com.example.socketchatapplication.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.Polling;
import io.socket.engineio.client.transports.WebSocket;

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
        this.initClientSocket();
    }

    private void initClientSocket() {
        try {
            this.clientSocket = IO.socket(BASE_URL, new IO.Options());
            this.clientSocket.connect();
            this.clientSocket.emit("join", userName);
            Log.i(TAG, "initClientSocket: ");
        } catch (URISyntaxException e) {
            Log.e(TAG, "initClientSocket: ERROR");
            e.printStackTrace();
        }
    }

    public void subscribe() {
        this.clientSocket.on(EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i(TAG, "call: EVENT_CONNECT");
                chatListener.onConnect();
            }
        });
        this.clientSocket.on(EVENT_DISCONNECT, args -> {
            Log.i(TAG, "call: EVENT_DISCONNECT");
            chatListener.onDisconnect();
        });
        this.clientSocket.on(EVENT_MESSAGE, args -> {
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
            // TODO: 18.04.2019 uncomment 
//            if (!userName.equals(nickname))
                chatListener.onNewMessage(nickname, message);

        });
        this.clientSocket.on(EVENT_CONNECT_ERROR, args -> {
            Log.i(TAG, "call: EVENT_CONNECT_ERROR");
            Exception exception = (Exception) args[0];
            chatListener.onConnectError(exception.getMessage());
        });
        this.clientSocket.on(EVENT_CONNECT_TIMEOUT, args -> {
            Log.i(TAG, "call: EVENT_CONNECT_TIMEOUT");
            chatListener.onConnectTimeout();
        });
        Log.i(TAG, "subscribe: ");
    }

    public void unsubscribe() {
        if (this.clientSocket != null) {
            this.clientSocket.disconnect();
            Log.i(TAG, "unsubscribe: ");
        }
    }

    public void sendMessage(String userName, String message) {
        this.userName = userName;
        if(message.length() == 0) {
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
        this.clientSocket.emit(EVENT_MESSAGE, messageJsonObject);
        this.chatListener.onOwnMessage(this.userName, message);
    }
}
