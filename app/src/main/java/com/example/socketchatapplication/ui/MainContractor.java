package com.example.socketchatapplication.ui;

import com.example.socketchatapplication.models.MessageModel;

import java.util.List;

public interface MainContractor  {

interface View{
    void setDataToView(List<MessageModel> listMessages);
}

interface Presenter{

    void attachView(View view);

    void detachView();

    void sendOwnMessage(String ownMessage);
}
}
