package com.example.quizapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.adapters.HistoryAdapter;
import com.example.quizapp.api.ApiClient;
import com.example.quizapp.api.ApiService;
import com.example.quizapp.responses.ExamResultResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerViewHistory;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private HistoryAdapter historyAdapter;
    private List<ExamResultResponse> examList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewHistory = view.findViewById(R.id.recyclerViewHistory);
        progressBar = view.findViewById(R.id.progressBar);
        tvEmpty = view.findViewById(R.id.tvEmpty);

        setupRecyclerView();
        loadHistory();
    }

    private void setupRecyclerView() {
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        examList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(attempt -> {
            // Handle attempt click
        });
        recyclerViewHistory.setAdapter(historyAdapter);
    }

    private void loadHistory() {
        progressBar.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<ExamResultResponse>> call = apiService.getMyAttempts();

        call.enqueue(new Callback<List<ExamResultResponse>>() {
            @Override
            public void onResponse(Call<List<ExamResultResponse>> call, Response<List<ExamResultResponse>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    examList = response.body();
                    if (examList.isEmpty()) {
                        tvEmpty.setVisibility(View.VISIBLE);
                    } else {
                        tvEmpty.setVisibility(View.GONE);
                        historyAdapter.setAttempts(examList);
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<ExamResultResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                tvEmpty.setVisibility(View.VISIBLE);
            }
        });
    }
}
