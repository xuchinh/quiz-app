package com.example.quizapp.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.quizapp.R;
import com.example.quizapp.fragments.ExamFragment;
import com.example.quizapp.fragments.HistoryFragment;
import com.example.quizapp.fragments.ProfileFragment;
import com.example.quizapp.fragments.StatisticsFragment;
import com.example.quizapp.models.User;
import com.example.quizapp.utils.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private SharedPrefManager prefManager;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefManager = SharedPrefManager.getInstance(this);

        // Check if logged in
        if (!prefManager.isLoggedIn()) {
            navigateToLogin();
            return;
        }

        // Initialize Views
        bottomNav = findViewById(R.id.bottom_nav);
        fragmentManager = getSupportFragmentManager();

        // Set default fragment
        if (savedInstanceState == null) {
            loadFragment(new ExamFragment());
            bottomNav.setSelectedItemId(R.id.nav_exam);
        }

        // Setup Bottom Navigation
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_exam) {
                fragment = new ExamFragment();
            } else if (itemId == R.id.nav_history) {
                fragment = new HistoryFragment();
            } else if (itemId == R.id.nav_statistics) {
                fragment = new StatisticsFragment();
            } else if (itemId == R.id.nav_profile) {
                fragment = new ProfileFragment();
            }

            if (fragment != null) {
                loadFragment(fragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}