package com.corporate.travel.repository;

import com.corporate.travel.entity.CarbonRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarbonRecordRepository extends JpaRepository<CarbonRecord, Long> {
    List<CarbonRecord> findByUserId(Long userId);
    List<CarbonRecord> findByDepartmentId(Long departmentId);
}
