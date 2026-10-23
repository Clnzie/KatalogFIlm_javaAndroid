package com.example.katalogfilm.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.katalogfilm.MainActivity;
import com.example.katalogfilm.R;

public class splash extends AppCompatActivity {

    private final Handler handler =
            new Handler(Looper.getMainLooper());

    private final Runnable openHome = () -> {
        startActivity(
                new Intent(
                        splash.this,
                        MainActivity.class
                )
        );
        finish();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler.postDelayed(openHome, 2000);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(openHome);
        super.onDestroy();
    }
}