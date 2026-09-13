package com.corporate.travel.repository;

import com.corporate.travel.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    List<Vendor> findByOrganizationId(Long organizationId);
    List<Vendor> findByVendorType(String vendorType);
}
