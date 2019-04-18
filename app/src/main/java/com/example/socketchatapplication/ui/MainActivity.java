package com.example.socketchatapplication.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.socketchatapplication.R;

public class MainActivity extends AppCompatActivity implements MainContractor.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
