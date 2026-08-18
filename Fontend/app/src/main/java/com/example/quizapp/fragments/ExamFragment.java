package com.example.quizapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.activities.QuizActivity;
import com.example.quizapp.activities.CreateCustomExamActivity;
import com.example.quizapp.activities.SubjectListActivity;
import com.example.quizapp.adapters.SubjectAdapter;
import com.example.quizapp.api.ApiClient;
import com.example.quizapp.api.ApiService;
import com.example.quizapp.models.Subject;
import com.example.quizapp.responses.UserDetailedStatisticsResponse;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamFragment extends Fragment {

    private MaterialButton btnCreateCustomExam, btnChooseSubject;
    private RecyclerView recyclerViewSubjects;
    private SubjectAdapter subjectAdapter;
    private List<Subject> subjectList;
    private TextView tvQuickTotalAttempts;
    private TextView tvQuickAvgScore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exam, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCreateCustomExam = view.findViewById(R.id.btnCreateCustomExam);
        btnChooseSubject = view.findViewById(R.id.btnChooseSubject);
        recyclerViewSubjects = view.findViewById(R.id.recyclerViewSubjects);
        tvQuickTotalAttempts = view.findViewById(R.id.tvQuickTotalAttempts);
        tvQuickAvgScore = view.findViewById(R.id.tvQuickAvgScore);

        setupRecyclerView();
        setupButtonListeners();
        loadStatistics();
        loadSubjects();
    }

    private void setupRecyclerView() {
        recyclerViewSubjects.setLayoutManager(new GridLayoutManager(getContext(), 2));
        subjectList = new ArrayList<>();
        subjectAdapter = new SubjectAdapter(subject -> {
            // Navigate to quiz with selected subject
            Intent intent = new Intent(getContext(), QuizActivity.class);
            intent.putExtra("subject_id", subject.getId());
            startActivity(intent);
        });
        recyclerViewSubjects.setAdapter(subjectAdapter);
    }

    private void setupButtonListeners() {
        btnCreateCustomExam.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CreateCustomExamActivity.class);
            startActivity(intent);
        });

        btnChooseSubject.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), SubjectListActivity.class));
        });
    }

    private void loadSubjects() {
        // TODO: Load subjects from API
        // For now, we'll leave it empty as it will be populated from your API
    }

    private void loadStatistics() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<UserDetailedStatisticsResponse> call = apiService.getUserDetailedStatistics();

        call.enqueue(new Callback<UserDetailedStatisticsResponse>() {
            @Override
            public void onResponse(Call<UserDetailedStatisticsResponse> call, Response<UserDetailedStatisticsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserDetailedStatisticsResponse stats = response.body();

                    if (tvQuickTotalAttempts != null) {
                        tvQuickTotalAttempts.setText(String.valueOf(stats.getTotalAttempts() != null ? stats.getTotalAttempts() : 0));
                    }
                    if (tvQuickAvgScore != null) {
                        double avgScore = stats.getAverageScore() != null ? stats.getAverageScore() : 0;
                        tvQuickAvgScore.setText(String.format("%.0f%%", avgScore));
                    }
                }
            }

            @Override
            public void onFailure(Call<UserDetailedStatisticsResponse> call, Throwable t) {
                // Silently fail for quick stats - not critical
            }
        });
    }
}
