package com.corporate.travel.repository;

import com.corporate.travel.entity.CorporateCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CorporateCardRepository extends JpaRepository<CorporateCard, Long> {
    List<CorporateCard> findByUserIdAndActiveTrue(Long userId);
    List<CorporateCard> findByOrganizationId(Long organizationId);
}
