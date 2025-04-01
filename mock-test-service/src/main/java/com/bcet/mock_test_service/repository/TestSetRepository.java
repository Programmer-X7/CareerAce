package com.bcet.mock_test_service.repository;

import com.bcet.mock_test_service.dto.TestSetDTO;
import com.bcet.mock_test_service.model.Category;
import com.bcet.mock_test_service.model.TestSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestSetRepository extends JpaRepository<TestSet, String> {

    @Query("SELECT new com.bcet.mock_test_service.dto.TestSetDTO( " +
            "t.testSetId, t.slug, t.name, SIZE(t.questions), t.duration, t.attempts, t.createdAt, t.updatedAt) " +
            "FROM TestSet t WHERE t.category.slug = :slug")
    List<TestSetDTO> findTestSetsByCategorySlug(String slug);

    Optional<TestSet> findBySlugAndCategory(String slug, Category category);

    // Increment attempts for every submission
    @Modifying
    @Transactional
    @Query("UPDATE TestSet t SET t.attempts = t.attempts + 1 WHERE t.testSetId = :testSetId")
    void incrementAttempts(@Param("testSetId") String testSetId);

    @Modifying
    @Transactional
    @Query("UPDATE TestSet t SET t.duration = :duration WHERE t.testSetId = :testSetId")
    void updateDuration(@Param("testSetId") String testSetId, @Param("duration") int duration);
}
