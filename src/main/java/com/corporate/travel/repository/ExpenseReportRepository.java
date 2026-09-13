package com.corporate.travel.repository;

import com.corporate.travel.entity.ExpenseReport;
import com.corporate.travel.entity.enums.ExpenseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
