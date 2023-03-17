package com.example.twisterfinger.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.twisterfinger.activities.views.GameView;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));
    }
}