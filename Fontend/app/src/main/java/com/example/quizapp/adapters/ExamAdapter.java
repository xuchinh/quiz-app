package com.example.quizapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.models.Exam;

import java.util.ArrayList;
import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {

    private List<Exam> exams = new ArrayList<>();
    private OnExamClickListener listener;

    public interface OnExamClickListener {
        void onExamClick(Exam exam);
    }

    public ExamAdapter(OnExamClickListener listener) {
        this.listener = listener;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exam, parent, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        Exam exam = exams.get(position);
        holder.bind(exam);
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

    class ExamViewHolder extends RecyclerView.ViewHolder {
        TextView tvExamTitle, tvExamDescription, tvDuration, tvQuestions;

        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExamTitle = itemView.findViewById(R.id.tvExamTitle);
            tvExamDescription = itemView.findViewById(R.id.tvExamDescription);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvQuestions = itemView.findViewById(R.id.tvQuestions);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onExamClick(exams.get(position));
                }
            });
        }

        public void bind(Exam exam) {
            tvExamTitle.setText(exam.getTitle());
            tvExamDescription.setText(exam.getDescription());
            tvDuration.setText(exam.getDurationMinutes() + " minutes");
            tvQuestions.setText(exam.getTotalQuestions() + " questions");
        }
    }
}
