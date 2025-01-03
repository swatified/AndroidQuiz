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

public class LeaderboardActivity extends AppCompatActivity {
    private RecyclerView scoresRecyclerView;
    private ScoreAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        db = FirebaseFirestore.getInstance();

        // Initialize views
        scoresRecyclerView = findViewById(R.id.scoresRecyclerView);
        TextView textView = findViewById(R.id.lead);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Button backButton = findViewById(R.id.backButton);

        // Setup RecyclerView
        scoresRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScoreAdapter(this);
        scoresRecyclerView.setAdapter(adapter);

        // Load scores
        loadScores();

        // Back button functionality
        backButton.setOnClickListener(v -> {
            finish(); // This will return to the previous activity
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
        finish(); // This will return to the previous activity
    }
}