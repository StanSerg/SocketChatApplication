package com.example.socketchatapplication.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.socketchatapplication.R;
import com.example.socketchatapplication.models.MessageModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContractor.View {
    private static final String TAG = "MainActivity";
    private MainContractor.Presenter presenter;
    private String userName = "";

    private TextView tvUserName;
    private EditText etvOwnMessage;
    private Button btnSendOwnMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initViewElements();
        this.attachPresenter();
        this.initListeners();
    }

    private void initViewElements() {
        this.tvUserName = (TextView) findViewById(R.id.tv_user);
        this.etvOwnMessage = (EditText) findViewById(R.id.etv_msg);
        this.btnSendOwnMessage = (Button) findViewById(R.id.btn_send);
    }

    private void attachPresenter() {
        this.userName = String.valueOf(this.tvUserName.getText());
        presenter = (MainContractor.Presenter) getLastCustomNonConfigurationInstance();
        if (presenter == null) {
            presenter = new MainPresenter(this, this.userName);
        }
        presenter.attachView(this);
    }

    private void initListeners() {
        this.btnSendOwnMessage.setOnClickListener(v -> {
            if (this.presenter != null && this.etvOwnMessage != null) {
                this.presenter.sendOwnMessage(this.etvOwnMessage.getText().toString());
                this.etvOwnMessage.setText("");
            }
        });
    }

    @Override
    public void setDataToView(List<MessageModel> listMessages) {

    }

    @Override
    protected void onDestroy() {
        this.presenter.detachView();
        super.onDestroy();
    }
}
