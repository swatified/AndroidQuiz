package com.example.androidquiz;

import java.util.Date;

public class ScoreModel {
    private String userId;
    private String userEmail;
    private int score;
    private long timeInSeconds;
    private Date timestamp;
    private String displayName;

    public ScoreModel() {}

    public ScoreModel(String userId, String userEmail, String displayName, int score, long timeInSeconds, Date timestamp) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.score = score;
        this.timeInSeconds = timeInSeconds;
        this.timestamp = timestamp;
        this.displayName = displayName;
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getScore() {
        return score;
    }

    public long getTimeInSeconds() {
        return timeInSeconds;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTimeInSeconds(long timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    //getter setter for display name
    public String getDisplayName() {
        if (displayName != null && !displayName.trim().isEmpty()) {
            return displayName;
        }

        // If no displayName, get complete local part of email
        if (userEmail != null && !userEmail.trim().isEmpty()) {
            String[] parts = userEmail.split("@");
            if (parts.length > 0) {
                return parts[0].toLowerCase();
            }
        }

        return "---"; // Fallback if neither displayName nor email is available
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}