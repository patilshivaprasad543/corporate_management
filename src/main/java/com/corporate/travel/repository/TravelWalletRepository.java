package com.corporate.travel.repository;

import com.corporate.travel.entity.TravelWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TravelWalletRepository extends JpaRepository<TravelWallet, Long> {
    Optional<TravelWallet> findByUserId(Long userId);
}
