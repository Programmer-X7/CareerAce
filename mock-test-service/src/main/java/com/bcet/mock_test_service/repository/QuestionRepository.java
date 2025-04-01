package com.bcet.mock_test_service.repository;

import com.bcet.mock_test_service.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {

    long count();

    @Modifying
    @Query("DELETE FROM Question q WHERE q.questionId = :questionId")
    void deleteQuestionById(@Param("questionId") String questionId);

}
