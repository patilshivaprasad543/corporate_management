package com.corporate.travel.repository;

import com.corporate.travel.entity.PolicyRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyRuleRepository extends JpaRepository<PolicyRule, Long> {
    List<PolicyRule> findByPolicy_Id(Long policyId);
}
