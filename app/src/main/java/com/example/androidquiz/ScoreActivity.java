package com.example.androidquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {
    private RecyclerView scoresRecyclerView;
    private ScoreAdapter adapter;
    private FirebaseFirestore db;
    private TextView yourScoreText, lead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        db = FirebaseFirestore.getInstance();

        // Initialize views
        scoresRecyclerView = findViewById(R.id.scoresRecyclerView);
        yourScoreText = findViewById(R.id.yourScoreText);
        Button playAgainButton = findViewById(R.id.playAgainButton);
        TextView textView = findViewById(R.id.lead);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Setup RecyclerView
        scoresRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScoreAdapter(this);
        scoresRecyclerView.setAdapter(adapter);

        // Get current score from intent
        int currentScore = getIntent().getIntExtra("currentScore", 0);
        long totalTime = getIntent().getLongExtra("totalTime", 0) / 1000;

        yourScoreText.setText(String.format("Your Score: %d/10 (Time: %02d:%02d)",
                currentScore, totalTime / 60, totalTime % 60));

        // Load scores
        loadScores();

        playAgainButton.setOnClickListener(v -> {
            Intent intent = new Intent(ScoreActivity.this, QuizActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void loadScores() {
        db.collection("quiz_scores")
                .orderBy("score", Query.Direction.DESCENDING)
                .orderBy("timeInSeconds", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error loading scores: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {
                        List<ScoreModel> scoresList = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            ScoreModel score = doc.toObject(ScoreModel.class);
                            scoresList.add(score);
                        }
                        adapter.setScores(scoresList);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        // Prevent going back to QuizActivity
        Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}