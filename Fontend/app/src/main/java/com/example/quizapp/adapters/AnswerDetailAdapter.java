package com.example.quizapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.models.AnswerDetail;

import java.util.ArrayList;
import java.util.List;

public class AnswerDetailAdapter extends RecyclerView.Adapter<AnswerDetailAdapter.AnswerViewHolder> {

    private List<AnswerDetail> answers = new ArrayList<>();

    public void setAnswerDetails(List<AnswerDetail> answers) {
        this.answers = answers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_answer_detail, parent, false);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        AnswerDetail answer = answers.get(position);
        holder.bind(answer);
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    class AnswerViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionNumber, tvQuestionText, tvYourAnswer, tvCorrectAnswer, tvStatus;

        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
            tvQuestionText = itemView.findViewById(R.id.tvQuestionText);
            tvYourAnswer = itemView.findViewById(R.id.tvYourAnswer);
            tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }

        public void bind(AnswerDetail answer) {
            tvQuestionNumber.setText("Q" + answer.getQuestionId());
            tvQuestionText.setText(answer.getQuestionText());
            tvYourAnswer.setText("Trả lời của bạn: " + answer.getSelectedOptionText());
            tvCorrectAnswer.setText("Trả lời đúng: " + answer.getCorrectOptionText());
            
            if (answer.isCorrect()) {
                tvStatus.setText(itemView.getContext().getString(R.string.correct_mark));
                tvStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.success));
            } else {
                tvStatus.setText(itemView.getContext().getString(R.string.wrong_mark));
                tvStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.error));
            }
        }
    }
}
