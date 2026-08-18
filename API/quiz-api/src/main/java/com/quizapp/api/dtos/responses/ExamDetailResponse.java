package com.quizapp.api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamDetailResponse {
    private Integer id;
    private String title;
    private String description;
    private Integer durationMinutes;
    private Integer totalQuestions;
    private String subjectName;
    private List<QuestionResponse> questions;
}
