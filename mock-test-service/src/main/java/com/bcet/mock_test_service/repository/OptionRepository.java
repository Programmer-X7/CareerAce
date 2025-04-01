package com.bcet.mock_test_service.repository;

import com.bcet.mock_test_service.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OptionRepository extends JpaRepository<Option, String> {

    @Modifying
    @Query("DELETE FROM Option o WHERE o.question.questionId = :questionId")
    void deleteOptionsByQuestionId(@Param("questionId") String questionId);

}
