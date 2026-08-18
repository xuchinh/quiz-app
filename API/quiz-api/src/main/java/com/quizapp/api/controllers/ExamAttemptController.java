package com.quizapp.api.controllers;

import com.quizapp.api.dtos.requests.SubmitExamRequest;
import com.quizapp.api.dtos.responses.ExamResultResponse;
import com.quizapp.api.models.User;
import com.quizapp.api.services.ExamAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/exam-attempts")
public class ExamAttemptController {

    @Autowired
    private ExamAttemptService examAttemptService;

    @PostMapping("/submit")
    public ResponseEntity<ExamResultResponse> submitExam(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody SubmitExamRequest request) {
        return ResponseEntity.ok(examAttemptService.submitExam(user, request));
    }

    @GetMapping("/my-attempts")
    public ResponseEntity<List<ExamResultResponse>> getMyAttempts(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(examAttemptService.getUserAttempts(user));
    }

    @GetMapping("/{attemptId}")
    public ResponseEntity<ExamResultResponse> getAttemptDetail(
            @AuthenticationPrincipal User user,
            @PathVariable Integer attemptId) {
        return ResponseEntity.ok(examAttemptService.getAttemptDetail(user, attemptId));
    }
}
