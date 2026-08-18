package com.quizapp.api.repositories;

import com.quizapp.api.models.ExamAttemptAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamAttemptAnswerRepository extends JpaRepository<ExamAttemptAnswer, Integer> {
}
