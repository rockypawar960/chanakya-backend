package com.chanakya.repository;

import com.chanakya.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Page<Feedback> findByUserId(Long userId, Pageable pageable);

    Page<Feedback> findByStatus(String status, Pageable pageable);

    Page<Feedback> findByFeedbackType(String feedbackType, Pageable pageable);

    List<Feedback> findByStatusOrderByCreatedAtDesc(String status);

    long countByStatus(String status);
}
