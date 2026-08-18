package com.example.quizapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.models.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private List<Subject> subjects = new ArrayList<>();
    private OnSubjectClickListener listener;

    public interface OnSubjectClickListener {
        void onSubjectClick(Subject subject);
    }

    public SubjectAdapter(OnSubjectClickListener listener) {
        this.listener = listener;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject subject = subjects.get(position);
        holder.bind(subject);
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectName, tvSubjectDescription;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
            tvSubjectDescription = itemView.findViewById(R.id.tvSubjectDescription);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onSubjectClick(subjects.get(position));
                }
            });
        }

        public void bind(Subject subject) {
            tvSubjectName.setText(subject.getName());
            tvSubjectDescription.setText(subject.getDescription());
        }
    }
}
