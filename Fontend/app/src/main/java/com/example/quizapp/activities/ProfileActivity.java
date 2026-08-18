package com.example.quizapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.example.quizapp.R;
import com.example.quizapp.api.ApiClient;
import com.example.quizapp.api.ApiService;
import com.example.quizapp.models.User;
import com.example.quizapp.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail;
    private Button btnSave, btnBack;
    private ProgressBar progressBar;
    private ApiService apiService;
    private SharedPrefManager prefManager;
    private User currentUser;
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Setup ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.user_profile);
        }

        apiService = ApiClient.getClient().create(ApiService.class);
        prefManager = SharedPrefManager.getInstance(this);

        // Initialize Views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.progressBar);

        // Set Listeners
        btnSave.setOnClickListener(v -> saveProfile());
        btnBack.setOnClickListener(v -> finish());

        // Load profile
        loadProfile();
    }

    private void loadProfile() {
        // First load from cache
        User cachedUser = prefManager.getUser();
        if (cachedUser != null) {
            displayUser(cachedUser);
        }

        // Then fetch fresh data from API
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "Loading profile from API");
        apiService.getUserProfile().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "Response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    currentUser = user;
                    displayUser(user);
                    prefManager.saveUser(prefManager.getToken(), user);
                    Log.d(TAG, "Profile loaded successfully");
                } else {
                    Log.e(TAG, "Response unsuccessful: " + response.message());
                    Toast.makeText(ProfileActivity.this, R.string.load_profile_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "API Error: " + t.getMessage(), t);
                Toast.makeText(ProfileActivity.this, R.string.network_error + "\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayUser(User user) {
        etName.setText(user.getName());
        etEmail.setText(user.getEmail());
        currentUser = user;
    }

    private void saveProfile() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);

        User updatedUser = new User();
        updatedUser.setName(name);
        updatedUser.setEmail(email);

        apiService.updateUserProfile(updatedUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressBar.setVisibility(View.GONE);
                btnSave.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    prefManager.saveUser(prefManager.getToken(), user);
                    Toast.makeText(ProfileActivity.this, R.string.update_profile_success, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ProfileActivity.this, R.string.update_profile_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnSave.setEnabled(true);
                Toast.makeText(ProfileActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
