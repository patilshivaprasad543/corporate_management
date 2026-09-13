package com.corporate.travel.repository;

import com.corporate.travel.entity.Booking;
import com.corporate.travel.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingReference(String bookingReference);
    Optional<Booking> findByPnrNumber(String pnrNumber);
    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Booking> findByOrganizationIdOrderByCreatedAtDesc(Long organizationId);
    List<Booking> findByTravelRequestId(Long travelRequestId);
    List<Booking> findByStatus(BookingStatus status);
    long countByOrganization_Id(Long organizationId);
}
