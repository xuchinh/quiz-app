package com.example.quizapp;

import android.app.Application;

import com.example.quizapp.api.ApiClient;

public class QuizApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize API client
        ApiClient.init(this);
    }
}
