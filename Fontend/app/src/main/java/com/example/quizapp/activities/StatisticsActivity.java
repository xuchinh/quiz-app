package com.example.quizapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.R;
import com.example.quizapp.api.ApiClient;
import com.example.quizapp.api.ApiService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticsActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private LinearLayout statsLayout;
    private TextView tvTotalAttempts, tvAverageScore, tvTotalCorrect;
    private ApiService apiService;
    private static final String TAG = "StatisticsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Setup ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.user_statistics);
        }

        apiService = ApiClient.getClient().create(ApiService.class);

        // Initialize Views
        progressBar = findViewById(R.id.progressBar);
        statsLayout = findViewById(R.id.statsLayout);
        tvTotalAttempts = findViewById(R.id.tvTotalAttempts);
        tvAverageScore = findViewById(R.id.tvAverageScore);
        tvTotalCorrect = findViewById(R.id.tvTotalCorrect);

        // Load statistics
        loadStatistics();
    }

    private void loadStatistics() {
        progressBar.setVisibility(View.VISIBLE);
        statsLayout.setVisibility(View.GONE);

        Log.d(TAG, "Loading statistics from API");
        apiService.getUserStatistics().enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "Response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> stats = response.body();
                    displayStatistics(stats);
                    Log.d(TAG, "Statistics loaded successfully");
                } else {
                    Log.e(TAG, "Response unsuccessful: " + response.message());
                    Toast.makeText(StatisticsActivity.this, R.string.load_statistics_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "API Error: " + t.getMessage(), t);
                Toast.makeText(StatisticsActivity.this, R.string.network_error + "\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayStatistics(Map<String, Object> stats) {
        statsLayout.setVisibility(View.VISIBLE);

        // Extract data from response map
        Object totalAttemptsObj = stats.get("totalAttempts");
        Object averageScoreObj = stats.get("averageScore");
        Object totalCorrectObj = stats.get("totalCorrect");

        int totalAttempts = totalAttemptsObj instanceof Number ? ((Number) totalAttemptsObj).intValue() : 0;
        double averageScore = averageScoreObj instanceof Number ? ((Number) averageScoreObj).doubleValue() : 0.0;
        int totalCorrect = totalCorrectObj instanceof Number ? ((Number) totalCorrectObj).intValue() : 0;

        tvTotalAttempts.setText(String.valueOf(totalAttempts));
        tvAverageScore.setText(String.format("%.2f", averageScore));
        tvTotalCorrect.setText(String.valueOf(totalCorrect));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
