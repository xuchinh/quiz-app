package com.quizapp.api.services;

import com.quizapp.api.dtos.responses.UserResponse;
import com.quizapp.api.dtos.responses.UserStatisticsResponse;
import com.quizapp.api.dtos.responses.UserDetailedStatisticsResponse;
import com.quizapp.api.exceptions.NotFoundException;
import com.quizapp.api.models.User;
import com.quizapp.api.repositories.UserRepository;
import com.quizapp.api.repositories.ExamAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExamAttemptRepository examAttemptRepository;

    public UserResponse getUserProfile(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    public UserStatisticsResponse getUserStatistics(User user) {
        Integer totalAttempts = examAttemptRepository.countAttemptsByUserId(user.getId());
        Integer totalCorrectAnswers = examAttemptRepository.sumCorrectAnswersByUserId(user.getId());
        Integer averageScore = examAttemptRepository.averageScoreByUserId(user.getId());
        Integer maxScore = examAttemptRepository.maxScoreByUserId(user.getId());
        Integer minScore = examAttemptRepository.minScoreByUserId(user.getId());

        return new UserStatisticsResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                totalAttempts,
                totalCorrectAnswers,
                averageScore,
                maxScore != null ? maxScore.doubleValue() : null,
                minScore != null ? minScore.doubleValue() : null
        );
    }

    public UserDetailedStatisticsResponse getUserDetailedStatistics(User user) {
        Integer totalAttempts = examAttemptRepository.countAttemptsByUserId(user.getId());
        Integer totalCorrectAnswers = examAttemptRepository.sumCorrectAnswersByUserId(user.getId());
        Integer totalWrongAnswers = examAttemptRepository.sumWrongAnswersByUserId(user.getId());
        Integer totalQuestions = examAttemptRepository.sumTotalQuestionsByUserId(user.getId());
        Integer averageScore = examAttemptRepository.averageScoreByUserId(user.getId());
        Integer maxScore = examAttemptRepository.maxScoreByUserId(user.getId());
        Integer minScore = examAttemptRepository.minScoreByUserId(user.getId());

        double accuracy = 0;
        if (totalQuestions > 0) {
            accuracy = (totalCorrectAnswers * 100.0) / totalQuestions;
        }

        // Get performance by subject
        List<Map<String, Object>> subjectPerformanceData = examAttemptRepository.getPerformanceBySubject(user.getId());
        List<UserDetailedStatisticsResponse.SubjectPerformance> subjectPerformance = 
            subjectPerformanceData.stream()
                .map(data -> {
                    Integer subjectId = ((Number) data.get("subjectId")).intValue();
                    String subjectName = (String) data.get("subjectName");
                    Integer attempts = ((Number) data.get("attempts")).intValue();
                    Integer correctAnswers = ((Number) data.get("correctAnswers")).intValue();
                    Integer totalQ = ((Number) data.get("totalQuestions")).intValue();
                    Integer avgScore = ((Number) data.get("averageScore")).intValue();
                    
                    double subjectAccuracy = 0;
                    if (totalQ > 0) {
                        subjectAccuracy = (correctAnswers * 100.0) / totalQ;
                    }
                    
                    return new UserDetailedStatisticsResponse.SubjectPerformance(
                            subjectId, subjectName, attempts, correctAnswers, totalQ, subjectAccuracy, avgScore
                    );
                })
                .collect(Collectors.toList());

        UserDetailedStatisticsResponse response = new UserDetailedStatisticsResponse();
        response.setUserId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setTotalAttempts(totalAttempts);
        response.setTotalCorrectAnswers(totalCorrectAnswers);
        response.setTotalWrongAnswers(totalWrongAnswers);
        response.setTotalQuestions(totalQuestions);
        response.setAccuracy(accuracy);
        response.setAverageScore(averageScore);
        response.setHighestScore(maxScore != null ? maxScore.doubleValue() : 0.0);
        response.setLowestScore(minScore != null ? minScore.doubleValue() : 0.0);
        response.setSubjectPerformance(subjectPerformance);

        return response;
    }

    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
