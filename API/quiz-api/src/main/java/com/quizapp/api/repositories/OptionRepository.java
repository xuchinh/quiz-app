package com.quizapp.api.repositories;

import com.quizapp.api.models.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Integer> {
    List<Option> findByQuestionId(Integer questionId);
}
