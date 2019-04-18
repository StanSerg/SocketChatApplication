package com.example.socketchatapplication.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.socketchatapplication.R;
import com.example.socketchatapplication.adapters.MessagesAdapter;
import com.example.socketchatapplication.models.MessageModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContractor.View {
    private static final String TAG = "MainActivity";
    private MainContractor.Presenter presenter;
    private String userName = "";

    private TextView tvUserName;
    private EditText etvOwnMessage;
    private Button btnSendOwnMessage;

    private RecyclerView rvChat;
    private MessagesAdapter messagesAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initViewElements();
        this.attachPresenter();
        this.initListeners();
        this.initAdapters();
    }


    private void initViewElements() {
        this.tvUserName = (TextView) findViewById(R.id.tv_user);
        this.etvOwnMessage = (EditText) findViewById(R.id.etv_msg);
        this.btnSendOwnMessage = (Button) findViewById(R.id.btn_send);
        this.rvChat = (RecyclerView) findViewById(R.id.rv_chat);
    }

    private void attachPresenter() {
        this.userName = String.valueOf(this.tvUserName.getText());
        this.presenter = (MainContractor.Presenter) getLastCustomNonConfigurationInstance();
        if (this.presenter == null) {
            this.presenter = new MainPresenter(this, this.userName);
        }
        this.presenter.attachView(this);
    }

    private void initListeners() {
        this.btnSendOwnMessage.setOnClickListener(v -> {
            if (this.presenter != null && this.etvOwnMessage != null) {
                this.presenter.sendOwnMessage(this.etvOwnMessage.getText().toString());
                this.etvOwnMessage.setText("");
            }
        });
    }

    private void initAdapters() {
        this.messagesAdapter = new MessagesAdapter(new ArrayList<>());
        this.linearLayoutManager = new LinearLayoutManager(this);
        this.rvChat.setLayoutManager(this.linearLayoutManager);
        this.rvChat.setAdapter(this.messagesAdapter);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return this.presenter;
    }

    @Override
    protected void onDestroy() {
        this.presenter.detachView();
        super.onDestroy();
    }


    // MainContractor.View methods
    @Override
    public void setDataToView(List<MessageModel> listMessages) {
        runOnUiThread(() -> {
            if (this.messagesAdapter != null) {
                this.messagesAdapter.setMessageList(listMessages);
                this.linearLayoutManager.scrollToPosition(listMessages.size() - 1);
            }
        });
    }


}
