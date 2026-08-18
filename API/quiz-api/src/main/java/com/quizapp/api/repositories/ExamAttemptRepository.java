package com.quizapp.api.repositories;

import com.quizapp.api.models.ExamAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Integer> {
    List<ExamAttempt> findByUserIdOrderByCreatedAtDesc(Integer userId);
    List<ExamAttempt> findByUserIdAndExamIdOrderByCreatedAtDesc(Integer userId, Integer examId);

    @Query("SELECT COUNT(ea) FROM ExamAttempt ea WHERE ea.user.id = :userId")
    Integer countAttemptsByUserId(@Param("userId") Integer userId);

    @Query("SELECT COALESCE(SUM(ea.correctCount), 0) FROM ExamAttempt ea WHERE ea.user.id = :userId")
    Integer sumCorrectAnswersByUserId(@Param("userId") Integer userId);

    @Query("SELECT COALESCE(SUM(ea.wrongCount), 0) FROM ExamAttempt ea WHERE ea.user.id = :userId")
    Integer sumWrongAnswersByUserId(@Param("userId") Integer userId);

    @Query("SELECT COALESCE(SUM(SIZE(e.questions)), 0) FROM ExamAttempt ea JOIN ea.exam e WHERE ea.user.id = :userId")
    Integer sumTotalQuestionsByUserId(@Param("userId") Integer userId);

    @Query("SELECT COALESCE(AVG(CAST(ea.score AS double)), 0) FROM ExamAttempt ea WHERE ea.user.id = :userId")
    Integer averageScoreByUserId(@Param("userId") Integer userId);

    @Query("SELECT MAX(ea.score) FROM ExamAttempt ea WHERE ea.user.id = :userId")
    Integer maxScoreByUserId(@Param("userId") Integer userId);

    @Query("SELECT MIN(ea.score) FROM ExamAttempt ea WHERE ea.user.id = :userId")
    Integer minScoreByUserId(@Param("userId") Integer userId);

    // Performance by subject
    @Query("SELECT NEW map(e.subject.id as subjectId, e.subject.name as subjectName, " +
            "COUNT(ea.id) as attempts, " +
            "COALESCE(SUM(ea.correctCount), 0) as correctAnswers, " +
            "COALESCE(SUM(SIZE(e.questions)), 0) as totalQuestions, " +
            "COALESCE(AVG(CAST(ea.score AS double)), 0) as averageScore) " +
            "FROM ExamAttempt ea " +
            "JOIN ea.exam e " +
            "WHERE ea.user.id = :userId " +
            "GROUP BY e.subject.id, e.subject.name")
    List<java.util.Map<String, Object>> getPerformanceBySubject(@Param("userId") Integer userId);
}

