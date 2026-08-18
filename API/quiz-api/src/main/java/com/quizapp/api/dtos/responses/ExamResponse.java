package com.quizapp.api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResponse {
    private Integer id;
    private String title;
    private String description;
    private Integer durationMinutes;
    private Integer totalQuestions;
    private String subjectName;
    private Integer subjectId;
    private LocalDateTime createdAt;
}
