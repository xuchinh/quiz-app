package com.example.quizapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.R;
import com.example.quizapp.api.ApiClient;
import com.example.quizapp.api.ApiService;
import com.example.quizapp.models.Option;
import com.example.quizapp.models.Question;
import com.example.quizapp.requests.AnswerRequest;
import com.example.quizapp.requests.SubmitExamRequest;
import com.example.quizapp.responses.ExamDetailResponse;
import com.example.quizapp.responses.ExamResultResponse;
import com.example.quizapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity {

    private int examId;
    private String examTitle;
    private ApiService apiService;
    
    private TextView tvTitle, tvQuestionNumber, tvTime, tvQuestion;
    private RadioGroup rgOptions;
    private Button btnPrevious, btnNext;
    private ProgressBar loadingProgressBar;
    private ScrollView scrollView;
    
    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private CountDownTimer timer;
    private long timeRemaining;
    
    private static final long TIMER_INTERVAL = 1000; // 1 second
    private static final String TAG = "QuizActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Get intent extras
        examId = getIntent().getIntExtra(Constants.EXTRA_EXAM_ID, -1);
        examTitle = getIntent().getStringExtra(Constants.EXTRA_EXAM_TITLE);

        apiService = ApiClient.getClient().create(ApiService.class);

        // Initialize Views
        tvTitle = findViewById(R.id.tvTitle);
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvTime = findViewById(R.id.tvTime);
        tvQuestion = findViewById(R.id.tvQuestion);
        rgOptions = findViewById(R.id.rgOptions);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        loadingProgressBar  = findViewById(R.id.loadingProgressBar);
        scrollView = findViewById(R.id.scrollView);

        tvTitle.setText(examTitle);

        // Set Listeners
        btnPrevious.setOnClickListener(v -> showPreviousQuestion());
        btnNext.setOnClickListener(v -> showNextQuestion());

        // Load exam details
        loadExamDetails();
    }

    private void loadExamDetails() {
        loadingProgressBar.setVisibility(View.VISIBLE);

        Log.d(TAG, "Loading exam details for exam ID: " + examId);
        apiService.getExamDetail(examId).enqueue(new Callback<ExamDetailResponse>() {
            @Override
            public void onResponse(Call<ExamDetailResponse> call, Response<ExamDetailResponse> response) {
                loadingProgressBar.setVisibility(View.GONE);
                Log.d(TAG, "Response code: " + response.code() + " - " + response.message());

                if (response.isSuccessful() && response.body() != null) {
                    ExamDetailResponse examDetail = response.body();
                    questions = examDetail.getQuestions();
                    
                    Log.d(TAG, "Exam detail received: " + (examDetail != null ? "OK" : "NULL"));
                    Log.d(TAG, "Questions: " + (questions != null ? questions.size() : "NULL"));
                    
                    if (questions != null && !questions.isEmpty()) {
                        // Start timer (convert minutes to milliseconds)
                        timeRemaining = examDetail.getDurationMinutes() * 60 * 1000;
                        Log.d(TAG, "Timer set for: " + examDetail.getDurationMinutes() + " minutes");
                        startTimer();
                        
                        // Display first question
                        displayCurrentQuestion();
                        Log.d(TAG, "Exam loaded with " + questions.size() + " questions");
                    } else {
                        Log.w(TAG, "No questions found in exam detail");
                        Toast.makeText(QuizActivity.this, R.string.no_questions, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Response unsuccessful: Code " + response.code() + " - " + response.message());
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBody);
                        } catch (Exception e) {
                            Log.e(TAG, "Could not read error body", e);
                        }
                    }
                    String errorMsg = "Lỗi " + response.code() + ": " + response.message();
                    Toast.makeText(QuizActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ExamDetailResponse> call, Throwable t) {
                loadingProgressBar.setVisibility(View.GONE);
                Log.e(TAG, "Network error loading exam details", t);
                Toast.makeText(QuizActivity.this, R.string.network_error + ": " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startTimer() {
        timer = new CountDownTimer(timeRemaining, TIMER_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                updateTimerDisplay();
            }

            @Override
            public void onFinish() {
                Toast.makeText(QuizActivity.this, R.string.out_of_time, Toast.LENGTH_SHORT).show();
                submitQuiz();
            }
        }.start();
    }

    private void updateTimerDisplay() {
        long minutes = timeRemaining / 60000;
        long seconds = (timeRemaining % 60000) / 1000;
        tvTime.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void displayCurrentQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);
            
            tvQuestionNumber.setText((currentQuestionIndex + 1) + " / " + questions.size());
            tvQuestion.setText(question.getQuestionText());
            
            // Clear and populate options
            rgOptions.removeAllViews();
            
            if (question.getOptions() != null) {
                for (Option option : question.getOptions()) {
                    RadioButton rb = new RadioButton(this);
                    rb.setId(option.getId());
                    rb.setText(option.getOptionText());
                    rb.setTextSize(16);
                    
                    rgOptions.addView(rb);
                    
                    // Check if this option was previously selected
                    if (question.getSelectedOptionId() == option.getId()) {
                        rb.setChecked(true);
                    }
                }
            }
            
            // Update button states
            btnPrevious.setEnabled(currentQuestionIndex > 0);
            boolean isLastQuestion = currentQuestionIndex == questions.size() - 1;
            btnNext.setText(isLastQuestion ? R.string.submit : R.string.next);
            
            // Scroll to top
            scrollView.smoothScrollTo(0, 0);
        }
    }

    private void showPreviousQuestion() {
        saveCurrentAnswer();
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            displayCurrentQuestion();
        }
    }

    private void showNextQuestion() {
        saveCurrentAnswer();
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            displayCurrentQuestion();
        } else {
            // This is the last question, submit instead
            submitQuiz();
        }
    }

    private void saveCurrentAnswer() {
        if (currentQuestionIndex < questions.size()) {
            int selectedOptionId = rgOptions.getCheckedRadioButtonId();
            if (selectedOptionId != -1) {
                questions.get(currentQuestionIndex).setSelectedOptionId(selectedOptionId);
            }
        }
    }

    private void submitQuiz() {
        // Check if all questions are answered
        for (Question question : questions) {
            if (question.getSelectedOptionId() == -1) {
                Toast.makeText(this, R.string.answer_all_questions, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Show confirmation dialog
        new AlertDialog.Builder(this)
                .setTitle(R.string.submit)
                .setMessage(R.string.submit_confirmation)
                .setPositiveButton(R.string.yes, (dialog, which) -> performSubmit())
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void performSubmit() {
        saveCurrentAnswer();
        
        if (timer != null) {
            timer.cancel();
        }

        // Prepare answers
        List<AnswerRequest> answers = new ArrayList<>();
        for (Question question : questions) {
            answers.add(new AnswerRequest(question.getId(), question.getSelectedOptionId()));
        }

        SubmitExamRequest request = new SubmitExamRequest(examId, answers);

        loadingProgressBar.setVisibility(View.VISIBLE);

        apiService.submitExam(request).enqueue(new Callback<ExamResultResponse>() {
            @Override
            public void onResponse(Call<ExamResultResponse> call, Response<ExamResultResponse> response) {
                loadingProgressBar.setVisibility(View.GONE);
                Log.d(TAG, "Submit response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    ExamResultResponse result = response.body();
                    Log.d(TAG, "Exam submitted successfully. Attempt ID: " + result.getAttemptId());
                    
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    intent.putExtra(Constants.EXTRA_ATTEMPT_ID, result.getAttemptId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e(TAG, "Submit failed: " + response.message());
                    Toast.makeText(QuizActivity.this, R.string.submit_exam_failed + "\n" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ExamResultResponse> call, Throwable t) {
                loadingProgressBar.setVisibility(View.GONE);
                Log.e(TAG, "Submit error: " + t.getMessage(), t);
                Toast.makeText(QuizActivity.this, R.string.network_error + "\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
