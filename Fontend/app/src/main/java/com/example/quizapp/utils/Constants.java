package com.example.quizapp.utils;

public class Constants {
    public static final String BASE_URL = "http://10.0.2.2:8088/"; // For Android Emulator
    // public static final String BASE_URL = "http://192.168.1.x:8088/"; // For Physical Device

    public static final String PREF_NAME = "QuizAppPrefs";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";

    // Intent Extra Keys
    public static final String EXTRA_SUBJECT_ID = "subject_id";
    public static final String EXTRA_SUBJECT_NAME = "subject_name";
    public static final String EXTRA_EXAM_ID = "exam_id";
    public static final String EXTRA_EXAM_TITLE = "exam_title";
    public static final String EXTRA_ATTEMPT_ID = "attempt_id";
}