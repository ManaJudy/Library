package com.mana.library.repository;

import com.mana.library.entity.Reservation;
import com.mana.library.entity.Member;
import com.mana.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Vérifier si un membre a déjà une réservation active
    @Query("SELECT r FROM Reservation r WHERE r.member = :member AND r.status = 'ACTIVE' AND r.expiryDate > :now")
    Optional<Reservation> findActiveReservationByMember(@Param("member") Member member, @Param("now") LocalDateTime now);

    // Vérifier si un livre a déjà une réservation active
    @Query("SELECT r FROM Reservation r WHERE r.book = :book AND r.status = 'ACTIVE' AND r.expiryDate > :now")
    List<Reservation> findActiveReservationsByBook(@Param("book") Book book, @Param("now") LocalDateTime now);

    // Trouver toutes les réservations expirées
    @Query("SELECT r FROM Reservation r WHERE r.status = 'ACTIVE' AND r.expiryDate <= :now")
    List<Reservation> findExpiredReservations(@Param("now") LocalDateTime now);

    // Trouver les réservations prêtes à être notifiées (livre disponible)
    @Query("SELECT r FROM Reservation r WHERE r.status = 'ACTIVE' AND r.notificationSent = false AND r.expiryDate > :now")
    List<Reservation> findReservationsToNotify(@Param("now") LocalDateTime now);

    // Trouver les réservations par priorité FIFO pour un livre
    @Query("SELECT r FROM Reservation r WHERE r.book = :book AND r.status = 'ACTIVE' AND r.expiryDate > :now ORDER BY r.reservationDate ASC")
    List<Reservation> findActiveReservationsByBookOrderByDate(@Param("book") Book book, @Param("now") LocalDateTime now);

    // Trouver les réservations d'un membre
    List<Reservation> findByMemberOrderByReservationDateDesc(Member member);
}
