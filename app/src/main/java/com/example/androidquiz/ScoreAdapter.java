package com.example.androidquiz;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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
        holder.rankTextView.setText(String.valueOf(position + 1));
        holder.userEmailTextView.setText(score.getUserEmail());
        holder.scoreTextView.setText(String.format("Score: %d/10", score.getScore()));
        holder.timeTextView.setText(String.format("%02d:%02d",
                score.getTimeInSeconds() / 60,
                score.getTimeInSeconds() % 60));
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    static class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView rankTextView, userEmailTextView, scoreTextView, timeTextView;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            rankTextView = itemView.findViewById(R.id.rankTextView);
            userEmailTextView = itemView.findViewById(R.id.userEmailTextView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }
    }
}
