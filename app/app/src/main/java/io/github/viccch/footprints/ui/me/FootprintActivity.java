package io.github.viccch.footprints.ui.me;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import io.github.viccch.footprints.R;

public class FootprintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footprint);

        setTitle("我的足迹");
    }
}