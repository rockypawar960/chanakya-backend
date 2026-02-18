package com.chanakya.repository;

import com.chanakya.entity.CareerAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareerAttributeRepository extends JpaRepository<CareerAttribute, Long> {
    List<CareerAttribute> findByCareerIdAndIsActiveTrue(Long careerId);
}
