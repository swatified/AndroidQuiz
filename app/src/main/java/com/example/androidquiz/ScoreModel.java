package com.example.androidquiz;

import java.util.Date;

public class ScoreModel {
    private String userId;
    private String userEmail;
    private int score;
    private long timeInSeconds;
    private Date timestamp;

    public ScoreModel() {} // Required for Firestore

    public ScoreModel(String userId, String userEmail, int score, long timeInSeconds, Date timestamp) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.score = score;
        this.timeInSeconds = timeInSeconds;
        this.timestamp = timestamp;
    }

    public Object getTimeInSeconds() {
    }

    // Add getters and setters
}