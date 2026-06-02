package com.chanakya.repository;

import com.chanakya.entity.UserLearningPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserLearningPathRepository extends JpaRepository<UserLearningPath, Long> {

    Optional<UserLearningPath>
    findByUserIdAndIsCurrentTrue(Long userId);

    @Modifying
    @Query("""
       update UserLearningPath u
       set u.isCurrent = false
       where u.user.id = :userId
       """)
    void clearCurrentPath(Long userId);

    Optional<UserLearningPath> findByUserIdAndLearningPathId(
            Long userId,
            Long learningPathId
    );
}
