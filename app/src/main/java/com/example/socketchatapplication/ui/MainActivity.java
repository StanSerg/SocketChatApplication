package com.example.socketchatapplication.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.socketchatapplication.R;
import com.example.socketchatapplication.ui.adapters.MessagesAdapter;
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
        initViewElements();
        attachPresenter();
        initListeners();
        initAdapters();
    }


    private void initViewElements() {
        tvUserName = findViewById(R.id.tv_user);
        etvOwnMessage = findViewById(R.id.etv_msg);
        btnSendOwnMessage = findViewById(R.id.btn_send);
        rvChat = findViewById(R.id.rv_chat);
    }

    private void attachPresenter() {
        userName = tvUserName.getText().toString();
        presenter = (MainContractor.Presenter) getLastCustomNonConfigurationInstance();
        if (presenter == null) {
            presenter = new MainPresenter(userName);
        }
        presenter.attachView(this);
    }

    private void initListeners() {
        btnSendOwnMessage.setOnClickListener(v -> {
            if (presenter != null && etvOwnMessage != null) {
                presenter.sendOwnMessage(etvOwnMessage.getText().toString());
                etvOwnMessage.setText("");
            }
        });
    }

    private void initAdapters() {
        messagesAdapter = new MessagesAdapter(new ArrayList<>());
        linearLayoutManager = new LinearLayoutManager(this);
        rvChat.setLayoutManager(linearLayoutManager);
        rvChat.setAdapter(messagesAdapter);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }


    // MainContractor.View methods
    @Override
    public void setDataToView(List<MessageModel> listMessages) {
        runOnUiThread(() -> {
            if (messagesAdapter != null) {
                messagesAdapter.setMessageList(listMessages);
                linearLayoutManager.scrollToPosition(listMessages.size() - 1);
            }
        });
    }


}
