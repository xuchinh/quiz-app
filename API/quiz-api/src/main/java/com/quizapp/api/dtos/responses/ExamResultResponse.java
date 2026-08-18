package com.quizapp.api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultResponse {
    private Integer attemptId;
    private Integer score;
    private Integer correctCount;
    private Integer wrongCount;
    private Integer totalQuestions;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private List<AnswerDetailResponse> answerDetails;
}
