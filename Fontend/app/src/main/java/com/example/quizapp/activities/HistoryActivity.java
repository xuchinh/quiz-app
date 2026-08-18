package com.example.quizapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.adapters.HistoryAdapter;
import com.example.quizapp.api.ApiClient;
import com.example.quizapp.api.ApiService;
import com.example.quizapp.responses.ExamResultResponse;
import com.example.quizapp.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity implements HistoryAdapter.OnAttemptClickListener {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private HistoryAdapter adapter;
    private ApiService apiService;
    private static final String TAG = "HistoryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Log.d(TAG, "onCreate: Activity started");

        // Setup ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.history);
        }

        apiService = ApiClient.getClient().create(ApiService.class);
        Log.d(TAG, "ApiService initialized");

        // Initialize Views
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);

        // Setup RecyclerView
        adapter = new HistoryAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load history
        loadHistory();
    }

    private void loadHistory() {
        Log.d(TAG, "loadHistory: Starting to load history");
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);

        apiService.getMyAttempts().enqueue(new Callback<List<ExamResultResponse>>() {
            @Override
            public void onResponse(Call<List<ExamResultResponse>> call, Response<List<ExamResultResponse>> response) {
                Log.d(TAG, "onResponse: Response received - Code: " + response.code());
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<ExamResultResponse> attempts = response.body();
                    Log.d(TAG, "onResponse: Success - Got " + attempts.size() + " attempts");

                    if (attempts.isEmpty()) {
                        Log.d(TAG, "onResponse: No attempts found");
                        tvEmpty.setText(R.string.no_history);
                        tvEmpty.setVisibility(View.VISIBLE);
                    } else {
                        Log.d(TAG, "onResponse: Setting adapter with " + attempts.size() + " items");
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.setAttempts(attempts);
                    }
                } else {
                    Log.e(TAG, "onResponse: Error - Code: " + response.code() + ", Message: " + response.message());
                    if (response.errorBody() != null) {
                        try {
                            Log.e(TAG, "Error body: " + response.errorBody().string());
                        } catch (Exception e) {
                            Log.e(TAG, "Could not read error body", e);
                        }
                    }
                    Toast.makeText(HistoryActivity.this, R.string.load_history_failed, Toast.LENGTH_SHORT).show();
                    tvEmpty.setText("Lỗi tải dữ liệu (Code: " + response.code() + ")");
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<ExamResultResponse>> call, Throwable t) {
                Log.e(TAG, "onFailure: Network error", t);
                progressBar.setVisibility(View.GONE);
                tvEmpty.setText("Lỗi mạng: " + t.getMessage());
                tvEmpty.setVisibility(View.VISIBLE);
                Toast.makeText(HistoryActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttemptClick(ExamResultResponse attempt) {
        Log.d(TAG, "onAttemptClick: Clicked on attempt ID: " + attempt.getAttemptId());
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(Constants.EXTRA_ATTEMPT_ID, attempt.getAttemptId());
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
