package com.quizapp.api.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamRequest {
    @NotNull(message = "Subject ID is required")
    private Integer subjectId;

    @NotBlank(message = "Exam title is required")
    @Size(min = 2, max = 255, message = "Exam title must be between 2 and 255 characters")
    private String title;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @NotNull(message = "Duration in minutes is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMinutes;

    @NotNull(message = "Total questions is required")
    @Min(value = 1, message = "Total questions must be at least 1")
    private Integer totalQuestions;
}
