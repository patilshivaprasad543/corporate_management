package com.corporate.travel.repository;

import com.corporate.travel.entity.ApprovalStep;
import com.corporate.travel.entity.enums.ApprovalStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalStepRepository extends JpaRepository<ApprovalStep, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from ApprovalStep a where a.travelRequest.id = :travelRequestId order by a.stepOrder asc")
    List<ApprovalStep> findByTravelRequestIdForUpdate(@Param("travelRequestId") Long travelRequestId);

    List<ApprovalStep> findByTravelRequestIdOrderByStepOrderAsc(Long travelRequestId);

    List<ApprovalStep> findByApproverIdAndStatus(Long approverId, ApprovalStatus status);
}
