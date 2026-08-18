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
import com.example.quizapp.adapters.ExamAdapter;
import com.example.quizapp.api.ApiClient;
import com.example.quizapp.api.ApiService;
import com.example.quizapp.models.Exam;
import com.example.quizapp.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamListActivity extends AppCompatActivity implements ExamAdapter.OnExamClickListener {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private ExamAdapter adapter;
    private ApiService apiService;
    private int subjectId;
    private String subjectName;
    private static final String TAG = "ExamListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_list);
        Log.d(TAG, "onCreate: Activity started");

        // Get intent extras
        subjectId = getIntent().getIntExtra(Constants.EXTRA_SUBJECT_ID, -1);
        subjectName = getIntent().getStringExtra(Constants.EXTRA_SUBJECT_NAME);
        Log.d(TAG, "onCreate: subjectId=" + subjectId + ", subjectName=" + subjectName);

        // Setup ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(subjectName != null ? subjectName : getString(R.string.exams));
        }

        apiService = ApiClient.getClient().create(ApiService.class);
        Log.d(TAG, "ApiService initialized");

        // Initialize Views
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);

        // Setup RecyclerView
        adapter = new ExamAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load exams
        loadExams();
    }

    private void loadExams() {
        Log.d(TAG, "loadExams: Starting to load exams");
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);

        if (subjectId == -1) {
            Log.d(TAG, "loadExams: Loading all exams");
            apiService.getAllExams().enqueue(examCallback());
        } else {
            Log.d(TAG, "loadExams: Loading exams for subject: " + subjectId);
            apiService.getExamsBySubject(subjectId).enqueue(examCallback());
        }
    }

    private Callback<List<Exam>> examCallback() {
        return new Callback<List<Exam>>() {
            @Override
            public void onResponse(Call<List<Exam>> call, Response<List<Exam>> response) {
                Log.d(TAG, "onResponse: Response code: " + response.code() + " - " + response.message());
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<Exam> exams = response.body();
                    Log.d(TAG, "onResponse: Success - Got " + exams.size() + " exams");

                    if (exams.isEmpty()) {
                        Log.d(TAG, "onResponse: No exams found");
                        tvEmpty.setText(R.string.no_exam);
                        tvEmpty.setVisibility(View.VISIBLE);
                    } else {
                        Log.d(TAG, "onResponse: Setting adapter with " + exams.size() + " items");
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.setExams(exams);
                    }
                } else {
                    Log.e(TAG, "onResponse: Error - Code: " + response.code() + ", Message: " + response.message());
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBody);
                        } catch (Exception e) {
                            Log.e(TAG, "Could not read error body", e);
                        }
                    }
                    Toast.makeText(ExamListActivity.this, "Lỗi " + response.code() + ": " + response.message(), Toast.LENGTH_SHORT).show();
                    tvEmpty.setText("Lỗi tải dữ liệu (Code: " + response.code() + ")");
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Exam>> call, Throwable t) {
                Log.e(TAG, "onFailure: Network error", t);
                progressBar.setVisibility(View.GONE);
                tvEmpty.setText("Lỗi mạng: " + t.getMessage());
                tvEmpty.setVisibility(View.VISIBLE);
                Toast.makeText(ExamListActivity.this, R.string.network_error + ": " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onExamClick(Exam exam) {
        Log.d(TAG, "onExamClick: Clicked on exam: " + exam.getTitle() + " (ID: " + exam.getId() + ")");
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra(Constants.EXTRA_EXAM_ID, exam.getId());
        intent.putExtra(Constants.EXTRA_EXAM_TITLE, exam.getTitle());
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
