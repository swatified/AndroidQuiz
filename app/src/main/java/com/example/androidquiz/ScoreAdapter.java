package com.example.androidquiz;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {
    private List<ScoreModel> scores;
    private Context context;

    public ScoreAdapter(Context context) {
        this.context = context;
        this.scores = new ArrayList<>();
    }

    public void setScores(List<ScoreModel> scores) {
        this.scores = scores;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_score, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        ScoreModel score = scores.get(position);

        // Set rank position (1-based)
        holder.rankTextView.setText(String.format("#%d", position + 1));

        // Set user email (show only first part before @)
        String email = score.getUserEmail();
        String displayName = email.substring(0, email.indexOf('@'));
        holder.userEmailTextView.setText(displayName);

        // Set score
        holder.scoreTextView.setText(String.format("Score: %d/10", score.getScore()));

        // Format time as MM:SS
        long minutes = score.getTimeInSeconds() / 60;
        long seconds = score.getTimeInSeconds() % 60;
        holder.timeTextView.setText(String.format("%02d:%02d", minutes, seconds));

        // Highlight current user's score
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if(score.getUserEmail().equals(currentUserEmail)) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.highlight_color));
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    static class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView rankTextView;
        TextView userEmailTextView;
        TextView scoreTextView;
        TextView timeTextView;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            rankTextView = itemView.findViewById(R.id.rankTextView);
            userEmailTextView = itemView.findViewById(R.id.userEmailTextView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }
    }
}
