package com.foodcampus.foodcampus.user.repository;

import com.foodcampus.foodcampus.user.entity.TermRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermRecordRepository extends JpaRepository<TermRecord, Long> {
}