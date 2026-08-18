package com.quizapp.api.repositories;

import com.quizapp.api.models.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Integer> {
    List<Exam> findBySubjectId(Integer subjectId);
    List<Exam> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
}
