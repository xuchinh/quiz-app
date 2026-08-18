package com.example.quizapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.adapters.AnswerDetailAdapter;
import com.example.quizapp.api.ApiClient;
import com.example.quizapp.api.ApiService;
import com.example.quizapp.responses.ExamResultResponse;
import com.example.quizapp.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity {

    private int attemptId;
    private ApiService apiService;
    
    private ProgressBar progressBar;
    private LinearLayout resultLayout;
    private TextView tvScore, tvCorrect, tvWrong, tvPercentage;
    private RecyclerView recyclerViewAnswers;
    private Button btnBackHome, btnViewAnswers;
    private AnswerDetailAdapter adapter;
    private boolean showingAnswers = false;
    private static final String TAG = "ResultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Setup back press handler
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                goBackHome();
            }
        });


        // Get intent extras
        attemptId = getIntent().getIntExtra(Constants.EXTRA_ATTEMPT_ID, -1);

        apiService = ApiClient.getClient().create(ApiService.class);

        // Initialize Views
        progressBar = findViewById(R.id.progressBar);
        resultLayout = findViewById(R.id.resultLayout);
        tvScore = findViewById(R.id.tvScore);
        tvCorrect = findViewById(R.id.tvCorrect);
        tvWrong = findViewById(R.id.tvWrong);
        tvPercentage = findViewById(R.id.tvPercentage);
        recyclerViewAnswers = findViewById(R.id.recyclerViewAnswers);
        btnBackHome = findViewById(R.id.btnBackHome);
        btnViewAnswers = findViewById(R.id.btnViewAnswers);

        // Setup RecyclerView
        adapter = new AnswerDetailAdapter();
        recyclerViewAnswers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAnswers.setAdapter(adapter);

        // Set Listeners
        btnBackHome.setOnClickListener(v -> goBackHome());
        btnViewAnswers.setOnClickListener(v -> toggleAnswersView());

        // Load result
        loadResult();
    }

    private void loadResult() {
        progressBar.setVisibility(View.VISIBLE);
        resultLayout.setVisibility(View.GONE);

        Log.d(TAG, "Loading result for attempt ID: " + attemptId);
        apiService.getAttemptDetail(attemptId).enqueue(new Callback<ExamResultResponse>() {
            @Override
            public void onResponse(Call<ExamResultResponse> call, Response<ExamResultResponse> response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "Response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    ExamResultResponse result = response.body();
                    displayResult(result);
                    Log.d(TAG, "Result loaded: Score " + result.getScore() + "/" + result.getTotalQuestions());
                } else {
                    Log.e(TAG, "Response unsuccessful: " + response.message());
                    Toast.makeText(ResultActivity.this, R.string.load_result_failed + "\n" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ExamResultResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "API Error: " + t.getMessage(), t);
                Toast.makeText(ResultActivity.this, R.string.network_error + "\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayResult(ExamResultResponse result) {
        resultLayout.setVisibility(View.VISIBLE);
        
        tvScore.setText(String.valueOf(result.getScore()));
        tvCorrect.setText(String.valueOf(result.getCorrectCount()));
        tvWrong.setText(String.valueOf(result.getWrongCount()));
        
        int percentage = result.getTotalQuestions() > 0 
            ? (result.getScore() * 100) / result.getTotalQuestions() 
            : 0;
        tvPercentage.setText(percentage + "%");
        
        if (result.getAnswerDetails() != null) {
            adapter.setAnswerDetails(result.getAnswerDetails());
        }
    }

    private void toggleAnswersView() {
        showingAnswers = !showingAnswers;
        recyclerViewAnswers.setVisibility(showingAnswers ? View.VISIBLE : View.GONE);
        btnViewAnswers.setText(showingAnswers ? R.string.hide_answers : R.string.view_answers);
    }

    private void goBackHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
