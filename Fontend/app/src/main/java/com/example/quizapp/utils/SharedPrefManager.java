package com.example.quizapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.quizapp.models.User;

public class SharedPrefManager {
    private static SharedPrefManager instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context.getApplicationContext());
        }
        return instance;
    }

    // Save user login data
    public void saveUser(String token, User user) {
        editor.putString(Constants.KEY_TOKEN, token);
        editor.putInt(Constants.KEY_USER_ID, user.getId());
        editor.putString(Constants.KEY_USER_NAME, user.getName());
        editor.putString(Constants.KEY_USER_EMAIL, user.getEmail());
        editor.putBoolean(Constants.KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    // Get token
    public String getToken() {
        return sharedPreferences.getString(Constants.KEY_TOKEN, null);
    }

    // Get user
    public User getUser() {
        if (!isLoggedIn()) {
            return null;
        }
        User user = new User();
        user.setId(sharedPreferences.getInt(Constants.KEY_USER_ID, -1));
        user.setName(sharedPreferences.getString(Constants.KEY_USER_NAME, ""));
        user.setEmail(sharedPreferences.getString(Constants.KEY_USER_EMAIL, ""));
        return user;
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
    }

    // Logout
    public void logout() {
        editor.clear();
        editor.apply();
    }
}
