package com.corporate.travel.repository;

import com.corporate.travel.entity.TravelRequest;
import com.corporate.travel.entity.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelRequestRepository extends JpaRepository<TravelRequest, Long> {
    Optional<TravelRequest> findByRequestNumber(String requestNumber);
    List<TravelRequest> findByEmployeeIdOrderByCreatedAtDesc(Long employeeId);
    List<TravelRequest> findByOrganizationIdOrderByCreatedAtDesc(Long organizationId);
    List<TravelRequest> findByDepartmentIdOrderByCreatedAtDesc(Long departmentId);
    List<TravelRequest> findByStatus(RequestStatus status);
}
