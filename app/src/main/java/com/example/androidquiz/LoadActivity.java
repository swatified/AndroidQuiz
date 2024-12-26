package com.example.androidquiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoadActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private int currentProgress = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        progressBar = findViewById(R.id.progressBar);
        startLoading();
    }

    private void startLoading() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentProgress += 5;
                progressBar.setProgress(currentProgress);

                if (currentProgress < 100) {
                    handler.postDelayed(this, 100);
                } else {
                    Intent scoreIntent = new Intent(LoadActivity.this, ScoreActivity.class);
                    scoreIntent.putExtras(getIntent().getExtras());
                    startActivity(scoreIntent);
                    finish();
                }
            }
        }, 100);
    }
}