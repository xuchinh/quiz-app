package com.example.quizapp.api;

import com.example.quizapp.models.Exam;
import com.example.quizapp.models.Subject;
import com.example.quizapp.models.User;
import com.example.quizapp.requests.LoginRequest;
import com.example.quizapp.requests.RegisterRequest;
import com.example.quizapp.requests.SubmitExamRequest;
import com.example.quizapp.responses.AuthResponse;
import com.example.quizapp.responses.ExamDetailResponse;
import com.example.quizapp.responses.ExamResultResponse;
import com.example.quizapp.responses.UserDetailedStatisticsResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // ===== AUTH ENDPOINTS =====

    @POST("api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST("api/auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);

    // ===== SUBJECT ENDPOINTS =====

    @GET("api/subjects")
    Call<List<Subject>> getAllSubjects();

    @GET("api/subjects/{id}")
    Call<Subject> getSubjectById(@Path("id") int id);

    @POST("api/subjects")
    Call<Subject> createSubject(@Body Subject subject);

    @PUT("api/subjects/{id}")
    Call<Subject> updateSubject(@Path("id") int id, @Body Subject subject);

    @DELETE("api/subjects/{id}")
    Call<Void> deleteSubject(@Path("id") int id);

    // ===== EXAM ENDPOINTS =====

    @GET("api/exams")
    Call<List<Exam>> getAllExams();

    @GET("api/exams/subject/{subjectId}")
    Call<List<Exam>> getExamsBySubject(@Path("subjectId") int subjectId);

    @GET("api/exams/{examId}")
    Call<ExamDetailResponse> getExamDetail(@Path("examId") int examId);

    @GET("api/exams/search")
    Call<List<Exam>> searchExams(@Query("keyword") String keyword);

    @POST("api/exams")
    Call<Exam> createExam(@Body Exam exam);

    @PUT("api/exams/{id}")
    Call<Exam> updateExam(@Path("id") int id, @Body Exam exam);

    @DELETE("api/exams/{id}")
    Call<Void> deleteExam(@Path("id") int id);

    // ===== EXAM ATTEMPT ENDPOINTS =====

    @POST("api/exam-attempts/submit")
    Call<ExamResultResponse> submitExam(@Body SubmitExamRequest request);

    @GET("api/exam-attempts/my-attempts")
    Call<List<ExamResultResponse>> getMyAttempts();

    @GET("api/exam-attempts/{attemptId}")
    Call<ExamResultResponse> getAttemptDetail(@Path("attemptId") int attemptId);

    // ===== USER ENDPOINTS =====

    @GET("api/users/profile")
    Call<User> getUserProfile();

    @PUT("api/users/profile")
    Call<User> updateUserProfile(@Body User user);

    @GET("api/users/statistics")
    Call<Map<String, Object>> getUserStatistics();

    @GET("api/users/statistics/detailed")
    Call<UserDetailedStatisticsResponse> getUserDetailedStatistics();
}
