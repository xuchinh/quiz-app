package com.example.quizapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.example.quizapp.R;
import com.example.quizapp.api.ApiClient;
import com.example.quizapp.api.ApiService;
import com.example.quizapp.requests.LoginRequest;
import com.example.quizapp.responses.AuthResponse;
import com.example.quizapp.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private ProgressBar progressBar;
    private ApiService apiService;
    private SharedPrefManager prefManager;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Activity started");

        // Check if already logged in
        prefManager = SharedPrefManager.getInstance(this);
        if (prefManager.isLoggedIn()) {
            Log.d(TAG, "onCreate: Already logged in, redirecting to MainActivity");
            navigateToMain();
            return;
        }

        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: Layout set");

        // Initialize API
        apiService = ApiClient.getClient().create(ApiService.class);

        // Initialize Views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        progressBar = findViewById(R.id.progressBar);

        // Set Listeners
        btnLogin.setOnClickListener(v -> login());
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        Log.d(TAG, "login: Attempting to login with email: " + email);

        // Validation
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading
        setLoading(true);

        // API Call
        LoginRequest request = new LoginRequest(email, password);
        apiService.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.d(TAG, "onResponse: Response code: " + response.code());
                setLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    Log.d(TAG, "onResponse: Login successful, token: " + 
                        (authResponse.getToken() != null ? authResponse.getToken().substring(0, Math.min(20, authResponse.getToken().length())) + "..." : "NULL"));

                    // Save user data
                    prefManager.saveUser(authResponse.getToken(), authResponse.getUser());
                    Log.d(TAG, "onResponse: User data saved");

                    // Navigate to main
                    Toast.makeText(LoginActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                    navigateToMain();
                } else {
                    Log.e(TAG, "onResponse: Login failed - Code: " + response.code() + ", Message: " + response.message());
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại (Code: " + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: Network error", t);
                setLoading(false);
                Toast.makeText(LoginActivity.this, R.string.network_error + ": " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!isLoading);
        etEmail.setEnabled(!isLoading);
        etPassword.setEnabled(!isLoading);
    }

    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}