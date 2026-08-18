package com.quizapp.api.controllers;

import com.quizapp.api.dtos.requests.QuestionRequest;
import com.quizapp.api.dtos.responses.QuestionResponse;
import com.quizapp.api.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionResponse> getQuestionById(@PathVariable Integer questionId) {
        return ResponseEntity.ok(questionService.getQuestionById(questionId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<QuestionResponse>> searchQuestions(@RequestParam String keyword) {
        return ResponseEntity.ok(questionService.searchQuestions(keyword));
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<QuestionResponse>> getQuestionsByExam(@PathVariable Integer examId) {
        return ResponseEntity.ok(questionService.getQuestionsByExam(examId));
    }

    @PostMapping
    public ResponseEntity<QuestionResponse> createQuestion(@Valid @RequestBody QuestionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(questionService.createQuestion(request));
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<QuestionResponse> updateQuestion(
            @PathVariable Integer questionId,
            @Valid @RequestBody QuestionRequest request) {
        return ResponseEntity.ok(questionService.updateQuestion(questionId, request));
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Integer questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }
}
