package com.example.quizapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quizapp.R;
import com.example.quizapp.api.ApiClient;
import com.example.quizapp.api.ApiService;
import com.example.quizapp.responses.UserDetailedStatisticsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticsFragment extends Fragment {

    private ProgressBar progressBar;
    private TextView tvTotalExams;
    private TextView tvTotalQuestions;
    private TextView tvAccuracy;
    private TextView tvCorrectAnswers;
    private TextView tvWrongAnswers;
    private TextView tvAverageScore;
    private TextView tvHighestScore;
    private TextView tvLowestScore;
    private LinearLayout subjectPerformanceContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        progressBar = view.findViewById(R.id.progressBar);
        tvTotalExams = view.findViewById(R.id.tvTotalExams);
        tvTotalQuestions = view.findViewById(R.id.tvTotalQuestions);
        tvAccuracy = view.findViewById(R.id.tvAccuracy);
        subjectPerformanceContainer = view.findViewById(R.id.subjectPerformanceContainer);

        loadDetailedStatistics();
    }

    private void loadDetailedStatistics() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<UserDetailedStatisticsResponse> call = apiService.getUserDetailedStatistics();

        call.enqueue(new Callback<UserDetailedStatisticsResponse>() {
            @Override
            public void onResponse(Call<UserDetailedStatisticsResponse> call, Response<UserDetailedStatisticsResponse> response) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }

                if (response.isSuccessful() && response.body() != null) {
                    displayDetailedStatistics(response.body());
                } else {
                    Toast.makeText(getContext(), "Lỗi khi tải thống kê", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDetailedStatisticsResponse> call, Throwable t) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayDetailedStatistics(UserDetailedStatisticsResponse stats) {
        try {
            // Update overall stats
            if (tvTotalExams != null) {
                tvTotalExams.setText(String.valueOf(stats.getTotalAttempts() != null ? stats.getTotalAttempts() : 0));
            }
            if (tvTotalQuestions != null) {
                tvTotalQuestions.setText(String.valueOf(stats.getTotalQuestions() != null ? stats.getTotalQuestions() : 0));
            }
            if (tvAccuracy != null) {
                double accuracy = stats.getAccuracy() != null ? stats.getAccuracy() : 0;
                tvAccuracy.setText(String.format("%.1f%%", accuracy));
            }

            // Display subject performance if available
            if (subjectPerformanceContainer != null && stats.getSubjectPerformance() != null) {
                displaySubjectPerformance(stats.getSubjectPerformance());
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Lỗi xử lý dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }

    private void displaySubjectPerformance(List<UserDetailedStatisticsResponse.SubjectPerformance> subjects) {
        if (subjectPerformanceContainer == null || subjects == null || subjects.isEmpty()) {
            return;
        }

        subjectPerformanceContainer.removeAllViews();

        for (UserDetailedStatisticsResponse.SubjectPerformance subject : subjects) {
            View subjectView = createSubjectPerformanceView(subject);
            subjectPerformanceContainer.addView(subjectView);
        }
    }

    private View createSubjectPerformanceView(UserDetailedStatisticsResponse.SubjectPerformance subject) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 12, 16, 12);
        
        // Subject name
        TextView tvSubjectName = new TextView(getContext());
        tvSubjectName.setText(subject.getSubjectName());
        tvSubjectName.setTextSize(14);
        tvSubjectName.setTextColor(getContext().getColor(R.color.text_primary));
        layout.addView(tvSubjectName);

        // Stats row
        LinearLayout statsRow = new LinearLayout(getContext());
        statsRow.setOrientation(LinearLayout.HORIZONTAL);
        statsRow.setWeightSum(3);

        // Attempts
        LinearLayout attemptLayout = createStatBox("Bài thi", String.valueOf(subject.getAttempts()), R.color.primary);
        statsRow.addView(attemptLayout);

        // Correct answers
        LinearLayout correctLayout = createStatBox("Đúng", String.valueOf(subject.getCorrectAnswers()), R.color.success);
        statsRow.addView(correctLayout);

        // Accuracy
        LinearLayout accuracyLayout = createStatBox("Độ chính", 
            String.format("%.0f%%", subject.getAccuracy() != null ? subject.getAccuracy() : 0), R.color.primary);
        statsRow.addView(accuracyLayout);

        layout.addView(statsRow);
        return layout;
    }

    private LinearLayout createStatBox(String label, String value, int colorId) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        layout.setGravity(android.view.Gravity.CENTER);
        layout.setPadding(8, 8, 8, 8);

        TextView tvValue = new TextView(getContext());
        tvValue.setText(value);
        tvValue.setTextSize(16);
        tvValue.setTypeface(null, android.graphics.Typeface.BOLD);
        tvValue.setTextColor(getContext().getColor(colorId));
        layout.addView(tvValue);

        TextView tvLabel = new TextView(getContext());
        tvLabel.setText(label);
        tvLabel.setTextSize(10);
        tvLabel.setTextColor(getContext().getColor(R.color.text_secondary));
        layout.addView(tvLabel);

        return layout;
    }
}
