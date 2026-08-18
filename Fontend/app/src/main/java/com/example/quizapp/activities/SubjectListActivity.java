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
import com.example.quizapp.adapters.SubjectAdapter;
import com.example.quizapp.api.ApiClient;
import com.example.quizapp.api.ApiService;
import com.example.quizapp.models.Subject;
import com.example.quizapp.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectListActivity extends AppCompatActivity implements SubjectAdapter.OnSubjectClickListener {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private SubjectAdapter adapter;
    private ApiService apiService;
    private static final String TAG = "SubjectListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);
        Log.d(TAG, "onCreate: Activity started");

        // Setup ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.choose_subject);
        }

        apiService = ApiClient.getClient().create(ApiService.class);
        Log.d(TAG, "ApiService initialized");

        // Initialize Views
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);

        // Setup RecyclerView
        adapter = new SubjectAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load subjects
        loadSubjects();
    }

    private void loadSubjects() {
        Log.d(TAG, "loadSubjects: Starting to load subjects");
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);

        apiService.getAllSubjects().enqueue(new Callback<List<Subject>>() {
            @Override
            public void onResponse(Call<List<Subject>> call, Response<List<Subject>> response) {
                Log.d(TAG, "onResponse: Response received - Code: " + response.code());
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<Subject> subjects = response.body();
                    Log.d(TAG, "onResponse: Success - Got " + subjects.size() + " subjects");

                    if (subjects.isEmpty()) {
                        Log.d(TAG, "onResponse: No subjects found");
                        tvEmpty.setVisibility(View.VISIBLE);
                    } else {
                        Log.d(TAG, "onResponse: Setting adapter with " + subjects.size() + " items");
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.setSubjects(subjects);
                    }
                } else {
                    Log.e(TAG, "onResponse: Error - Code: " + response.code() + ", Message: " + response.message());
                    Toast.makeText(SubjectListActivity.this, R.string.exam_load_failed, Toast.LENGTH_SHORT).show();
                    tvEmpty.setText("Lỗi tải dữ liệu (Code: " + response.code() + ")");
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Subject>> call, Throwable t) {
                Log.e(TAG, "onFailure: Network error", t);
                progressBar.setVisibility(View.GONE);
                tvEmpty.setText("Lỗi mạng: " + t.getMessage());
                tvEmpty.setVisibility(View.VISIBLE);
                Toast.makeText(SubjectListActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSubjectClick(Subject subject) {
        Log.d(TAG, "onSubjectClick: Clicked on subject: " + subject.getName());
        Intent intent = new Intent(this, ExamListActivity.class);
        intent.putExtra(Constants.EXTRA_SUBJECT_ID, subject.getId());
        intent.putExtra(Constants.EXTRA_SUBJECT_NAME, subject.getName());
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
