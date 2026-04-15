package com.chanakya.repository;

import com.chanakya.entity.AdminActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdminActivityLogRepository extends JpaRepository<AdminActivityLog, Long> {

    Page<AdminActivityLog> findByAdminId(Long adminId, Pageable pageable);

    Page<AdminActivityLog> findByAction(String action, Pageable pageable);

    Page<AdminActivityLog> findByEntityType(String entityType, Pageable pageable);

    List<AdminActivityLog> findByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime dateTime);

    List<AdminActivityLog> findByAdminIdAndActionOrderByCreatedAtDesc(Long adminId, String action);
}
