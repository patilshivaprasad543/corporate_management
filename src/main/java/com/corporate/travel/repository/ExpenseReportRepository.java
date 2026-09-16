package com.corporate.travel.repository;

import com.corporate.travel.entity.ExpenseReport;
import com.corporate.travel.entity.enums.ExpenseStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseReportRepository extends JpaRepository<ExpenseReport, Long> {
    Optional<ExpenseReport> findByReportNumber(String reportNumber);
    List<ExpenseReport> findByEmployeeIdOrderByCreatedAtDesc(Long employeeId);
    List<ExpenseReport> findByDepartmentId(Long departmentId);
    List<ExpenseReport> findByStatus(ExpenseStatus status);
    Optional<ExpenseReport> findByTravelRequestId(Long travelRequestId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from ExpenseReport r where r.id = :id")
    Optional<ExpenseReport> findByIdForUpdate(@Param("id") Long id);
}
