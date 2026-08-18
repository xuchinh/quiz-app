package com.example.quizapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.api.ApiClient;
import com.example.quizapp.api.ApiService;
import com.example.quizapp.models.Subject;
import com.example.quizapp.requests.SubmitExamRequest;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCustomExamActivity extends AppCompatActivity {

    private EditText etNumQuestions;
    private EditText etDurationMinutes;
    private Spinner spinnerSubject;
    private MaterialButton btnCreateExam;
    private Button btnBack;
    private List<Subject> subjects = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_custom_exam);

        etNumQuestions = findViewById(R.id.etNumQuestions);
        etDurationMinutes = findViewById(R.id.etDurationMinutes);
        spinnerSubject = findViewById(R.id.spinnerSubject);
        btnCreateExam = findViewById(R.id.btnCreateExam);
        btnBack = findViewById(R.id.btnBack);

        apiService = ApiClient.getClient().create(ApiService.class);

        btnBack.setOnClickListener(v -> finish());
        loadSubjects();
        setupButtonListener();
    }

    private void loadSubjects() {
        Call<List<Subject>> call = apiService.getAllSubjects();
        call.enqueue(new Callback<List<Subject>>() {
            @Override
            public void onResponse(Call<List<Subject>> call, Response<List<Subject>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    subjects = response.body();
                    // TODO: Setup spinner with subjects
                }
            }

            @Override
            public void onFailure(Call<List<Subject>> call, Throwable t) {
                Toast.makeText(CreateCustomExamActivity.this, "Lỗi tải môn học", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupButtonListener() {
        btnCreateExam.setOnClickListener(v -> createCustomExam());
    }

    private void createCustomExam() {
        try {
            String numQuestionsStr = etNumQuestions.getText().toString().trim();
            String durationStr = etDurationMinutes.getText().toString().trim();

            if (numQuestionsStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập số câu hỏi", Toast.LENGTH_SHORT).show();
                return;
            }

            if (durationStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập thời gian làm bài", Toast.LENGTH_SHORT).show();
                return;
            }

            int numQuestions = Integer.parseInt(numQuestionsStr);
            int duration = Integer.parseInt(durationStr);

            if (numQuestions <= 0 || numQuestions > 100) {
                Toast.makeText(this, "Số câu hỏi phải từ 1 đến 100", Toast.LENGTH_SHORT).show();
                return;
            }

            if (duration <= 0 || duration > 180) {
                Toast.makeText(this, "Thời gian phải từ 1 đến 180 phút", Toast.LENGTH_SHORT).show();
                return;
            }

            // Start quiz with custom parameters
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("is_custom", true);
            intent.putExtra("num_questions", numQuestions);
            intent.putExtra("duration_minutes", duration);
            startActivity(intent);
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập số hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}
