package com.example.quizapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.responses.ExamResultResponse;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<ExamResultResponse> attempts = new ArrayList<>();
    private OnAttemptClickListener listener;

    public interface OnAttemptClickListener {
        void onAttemptClick(ExamResultResponse attempt);
    }

    public HistoryAdapter(OnAttemptClickListener listener) {
        this.listener = listener;
    }

    public void setAttempts(List<ExamResultResponse> attempts) {
        this.attempts = attempts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        ExamResultResponse attempt = attempts.get(position);
        holder.bind(attempt);
    }

    @Override
    public int getItemCount() {
        return attempts.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvAttemptId, tvScore, tvCorrect, tvDate;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAttemptId = itemView.findViewById(R.id.tvAttemptId);
            tvScore = itemView.findViewById(R.id.tvScore);
            tvCorrect = itemView.findViewById(R.id.tvCorrect);
            tvDate = itemView.findViewById(R.id.tvDate);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onAttemptClick(attempts.get(position));
                }
            });
        }

        public void bind(ExamResultResponse attempt) {
            tvAttemptId.setText("Attempt #" + attempt.getAttemptId());
            tvScore.setText("Score: " + attempt.getScore() + "/" + attempt.getTotalQuestions());
            tvCorrect.setText(attempt.getCorrectCount() + " Correct");
            tvDate.setText("Attempted on: " + attempt.getStartedAt());
        }
    }
}
