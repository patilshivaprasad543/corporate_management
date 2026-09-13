package com.corporate.travel.repository;

import com.corporate.travel.entity.ApprovalStep;
import com.corporate.travel.entity.enums.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalStepRepository extends JpaRepository<ApprovalStep, Long> {
    List<ApprovalStep> findByTravelRequestIdOrderByStepOrderAsc(Long travelRequestId);
    List<ApprovalStep> findByApproverIdAndStatus(Long approverId, ApprovalStatus status);
}
