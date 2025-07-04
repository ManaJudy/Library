package com.mana.library.repository;

import com.mana.library.entity.Subscription;
import com.mana.library.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByMember(Member member);

    List<Subscription> findByMemberOrderByEndDateDesc(Member member);

    @Query("SELECT s FROM Subscription s WHERE s.member = :member AND s.endDate > :currentDate AND s.active = true")
    Optional<Subscription> findActiveSubscriptionByMember(@Param("member") Member member, @Param("currentDate") LocalDateTime currentDate);

    @Query("SELECT s FROM Subscription s WHERE s.endDate < :currentDate AND s.active = true")
    List<Subscription> findExpiredSubscriptions(@Param("currentDate") LocalDateTime currentDate);
}
