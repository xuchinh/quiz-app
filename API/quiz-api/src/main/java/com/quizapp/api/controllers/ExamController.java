package com.quizapp.api.controllers;

import com.quizapp.api.dtos.requests.ExamRequest;
import com.quizapp.api.dtos.responses.ExamDetailResponse;
import com.quizapp.api.dtos.responses.ExamResponse;
import com.quizapp.api.services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @GetMapping
    public ResponseEntity<List<ExamResponse>> getAllExams() {
        return ResponseEntity.ok(examService.getAllExams());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ExamResponse>> searchExams(@RequestParam String keyword) {
        return ResponseEntity.ok(examService.searchExams(keyword));
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<ExamResponse>> getExamsBySubject(@PathVariable Integer subjectId) {
        return ResponseEntity.ok(examService.getExamsBySubject(subjectId));
    }

    @GetMapping("/{examId}")
    public ResponseEntity<ExamDetailResponse> getExamById(@PathVariable Integer examId) {
        return ResponseEntity.ok(examService.getExamById(examId));
    }

    @PostMapping
    public ResponseEntity<ExamResponse> createExam(@Valid @RequestBody ExamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(examService.createExam(request));
    }

    @PutMapping("/{examId}")
    public ResponseEntity<ExamResponse> updateExam(
            @PathVariable Integer examId,
            @Valid @RequestBody ExamRequest request) {
        return ResponseEntity.ok(examService.updateExam(examId, request));
    }

    @DeleteMapping("/{examId}")
    public ResponseEntity<Void> deleteExam(@PathVariable Integer examId) {
        examService.deleteExam(examId);
        return ResponseEntity.noContent().build();
    }
}
