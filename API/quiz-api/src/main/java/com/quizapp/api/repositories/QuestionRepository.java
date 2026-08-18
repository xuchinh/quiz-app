package com.quizapp.api.repositories;

import com.quizapp.api.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByExamId(Integer examId);
    List<Question> findByQuestionTextContainingIgnoreCase(String keyword);
}
