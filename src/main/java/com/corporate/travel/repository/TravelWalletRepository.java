package com.corporate.travel.repository;

import com.corporate.travel.entity.TravelWallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TravelWalletRepository extends JpaRepository<TravelWallet, Long> {
    Optional<TravelWallet> findByUserId(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select w from TravelWallet w where w.user.id = :userId")
    Optional<TravelWallet> findByUserIdForUpdate(@Param("userId") Long userId);
}
