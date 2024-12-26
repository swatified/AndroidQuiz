package com.example.androidquiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Date;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.AlertDialog;
import android.view.LayoutInflater;


public class QuizActivity extends AppCompatActivity {
    private TextView timerText;
    private TextView questionText;
    private RadioGroup optionsGroup;
    private RadioButton option1, option2, option3, option4;
    private Button submitButton;
    private TextView scoreText;
    private FirebaseFirestore db;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference scoresRef;

    private int currentQuestionIndex = 0;
    private int score = 0;
    private long startTime = 0;
    private CountDownTimer timer;

    // Question class to structure our quiz questions
    private static class Question {
        String question;
        List<String> options;
        int correctAnswerIndex;
        String explanation;

        Question(String question, List<String> options, int correctAnswerIndex, String explanation) {
            this.question = question;
            this.options = options;
            this.correctAnswerIndex = correctAnswerIndex;
            this.explanation = explanation;
        }
    }

    // List of questions
    private List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialize views
        initializeViews();

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        scoresRef = firebaseDatabase.getReference("scores");

        // Initialize questions
        initializeQuestions();

        startTime = System.currentTimeMillis();
        setupTimer();
        loadQuestion();

        submitButton.setOnClickListener(v -> checkAnswer());
    }

    private void initializeViews() {
        timerText = findViewById(R.id.timerText);
        questionText = findViewById(R.id.questionText);
        optionsGroup = findViewById(R.id.optionsGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        submitButton = findViewById(R.id.submitButton);
        scoreText = findViewById(R.id.scoreText);
    }

    private void initializeQuestions() {
        questions = new ArrayList<>();

        // Question 1
        questions.add(new Question(
                "From which version did Android stop naming its versions after desserts/sweets?",
                Arrays.asList(
                        "Android Version 9 (Pie)",
                        "Android Version 10",
                        "Android Version 11",
                        "Android Version 8 (Oreo)"
                ),
                1,
                "Starting from Android 10, Google switched to a simpler numerical naming convention."
        ));

        // Question 2
        questions.add(new Question(
                "Which of these devices typically doesn't have Android as its operating system?",
                Arrays.asList(
                        "Smart TVs",
                        "MacBook",
                        "Smart Watches",
                        "Tablets"
                ),
                1,
                "MacBooks run on macOS, while Android can be found on various devices including TVs, watches, tablets, and more."
        ));

        // Question 3
        questions.add(new Question(
                "Which of these is NOT an advantage of Android apps over websites?",
                Arrays.asList(
                        "Offline Working",
                        "Push Notifications",
                        "Stable Updates",
                        "No Installation Required"
                ),
                3,
                "Unlike websites, Android apps require installation. However, they offer benefits like offline working, push notifications, and stable updates."
        ));

        // Question 4
        questions.add(new Question(
                "How are Activities different from Views in Android?",
                Arrays.asList(
                        "Activities contain Views, Views contain Activities",
                        "Activities handle the UI logic, Views handle background processes",
                        "Activities are screens that can contain multiple Views, Views are UI elements",
                        "Activities and Views are the same thing"
                ),
                2,
                "Activities are components that represent screens, while Views are UI elements that make up the visual interface."
        ));

        // Question 5
        questions.add(new Question(
                "What are Intents in Android?",
                Arrays.asList(
                        "UI elements that connect activities",
                        "Messaging objects used to request actions between components",
                        "Special Android permissions",
                        "Background services only"
                ),
                1,
                "Intents are messaging objects used to request actions and communicate between components."
        ));

        // Question 6
        questions.add(new Question(
                "Which of these files is NOT likely to be found in your Android Studio project?",
                Arrays.asList(
                        "build.gradle",
                        "AndroidManifest.xml",
                        "Swift.config",
                        "strings.xml"
                ),
                2,
                "Swift.config is related to iOS development, not Android development."
        ));

        // Question 7
        questions.add(new Question(
                "Match the correct definitions: \nA. Compiles code and dependencies\nB. Provides essential app information\nC. Contains app permissions\nD. Build system for Android\n\nFor: 1. Gradle 2. Manifest",
                Arrays.asList(
                        "1-AD, 2-BC",
                        "1-BC, 2-AD",
                        "1-AB, 2-CD",
                        "1-CD, 2-AB"
                ),
                0,
                "Gradle (1) is the build system (D) that compiles code and dependencies (A), while the Manifest (2) provides essential app information (B) and contains permissions (C)."
        ));

        // Question 8
        questions.add(new Question(
                "Which of these has the least priority in the Activity lifecycle?",
                Arrays.asList(
                        "onCreate()",
                        "onStart()",
                        "onResume()",
                        "onDestroy()"
                ),
                3,
                "onDestroy() is called last in the activity lifecycle, when the activity is being shut down."
        ));

        // Question 9
        questions.add(new Question(
                "What are Interfaces in Android development?",
                Arrays.asList(
                        "The visual elements the user sees",
                        "Abstract types that contain method declarations without implementations",
                        "Classes that handle all UI logic",
                        "XML files that define layouts"
                ),
                1,
                "Interfaces are abstract types that define methods that implementing classes must provide."
        ));

        // Question 10
        questions.add(new Question(
                "Which of these is NOT a method for installing your Android app?",
                Arrays.asList(
                        "via USB Cable",
                        "via Cloud Sync",
                        "via Wireless Connection",
                        "By building and exporting APK"
                ),
                1,
                "The three main ways to install an Android app are: via USB Cable, via Wireless Connection, or by building and exporting an APK."
        ));
    }

    private void setupTimer() {
        timer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                long minutes = (elapsedTime / 1000) / 60;
                long seconds = (elapsedTime / 1000) % 60;
                timerText.setText(String.format("Time: %02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {}
        }.start();
    }

    private void loadQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            questionText.setText(currentQuestion.question);

            optionsGroup.clearCheck();
            option1.setText(currentQuestion.options.get(0));
            option2.setText(currentQuestion.options.get(1));
            option3.setText(currentQuestion.options.get(2));
            option4.setText(currentQuestion.options.get(3));

            scoreText.setText(String.format("Score: %d/%d", score, questions.size()));
        } else {
            finishQuiz();
        }
    }

    private void checkAnswer() {
        int selectedId = optionsGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedIndex;
        if (selectedId == R.id.option1) selectedIndex = 0;
        else if (selectedId == R.id.option2) selectedIndex = 1;
        else if (selectedId == R.id.option3) selectedIndex = 2;
        else selectedIndex = 3;

        Question currentQuestion = questions.get(currentQuestionIndex);
        boolean isCorrect = selectedIndex == currentQuestion.correctAnswerIndex;

        if (isCorrect) {
            showCorrectDialog(currentQuestion.explanation);
        } else {
            showIncorrectDialog(currentQuestion.explanation);
        }
    }

    private void showCorrectDialog(String explanation) {
        View dialogView = getLayoutInflater().inflate(R.layout.correct_answer_dialog, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        TextView explanationText = dialogView.findViewById(R.id.correctExplanationText);
        Button continueButton = dialogView.findViewById(R.id.correctContinueButton);
        LottieAnimationView animationView = dialogView.findViewById(R.id.correctAnimation);

        explanationText.setText(explanation);
        animationView.setAnimation(R.raw.correct_animation);

        continueButton.setOnClickListener(v -> {
            dialog.dismiss();
            score++;
            currentQuestionIndex++;
            if (currentQuestionIndex >= questions.size()) {
                finishQuiz();
            } else {
                loadQuestion();
            }
        });

        if (currentQuestionIndex >= questions.size()) {
            finishQuiz();
            return;
        }

        dialog.show();
    }

    private void showIncorrectDialog(String explanation) {
        View dialogView = getLayoutInflater().inflate(R.layout.incorrect_answer_dialog, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        TextView explanationText = dialogView.findViewById(R.id.incorrectExplanationText);
        Button continueButton = dialogView.findViewById(R.id.incorrectContinueButton);
        LottieAnimationView animationView = dialogView.findViewById(R.id.incorrectAnimation);

        explanationText.setText(explanation);
        animationView.setAnimation(R.raw.incorrect_animation);

        continueButton.setOnClickListener(v -> {
            dialog.dismiss();
            currentQuestionIndex++;
            if (currentQuestionIndex >= questions.size()) {
                finishQuiz();
            } else {
                loadQuestion();
            }
        });

        dialog.show();
    }

    private void finishQuiz() {
        if (timer != null) {
            timer.cancel();
        }

        long totalTime = System.currentTimeMillis() - startTime;

        // Create score data
        Map<String, Object> scoreData = new HashMap<>();
        scoreData.put("score", score);
        scoreData.put("totalQuestions", questions.size());
        scoreData.put("timeInSeconds", totalTime / 1000);
        scoreData.put("timestamp", new Date());
        scoreData.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        scoreData.put("userEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        scoreData.put("displayName", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        // Save score to Firestore
        db.collection("quiz_scores")
                .add(scoreData)
                .addOnSuccessListener(documentReference -> {
                    showResults();
                    Toast.makeText(QuizActivity.this,
                            "Score saved successfully!",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(QuizActivity.this,
                            "Error saving score: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    showResults(); // Still show results even if save fails
                });
    }

    private void showResults() {
        if (timer != null) {
            timer.cancel();
        }

        long totalTime = System.currentTimeMillis() - startTime;
        Intent intent = new Intent(QuizActivity.this, LoadActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("currentScore", score);
        intent.putExtra("totalTime", totalTime);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}