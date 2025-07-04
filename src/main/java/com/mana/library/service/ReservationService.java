package com.mana.library.service;

import com.mana.library.entity.Reservation;
import com.mana.library.entity.Member;
import com.mana.library.entity.Book;
import com.mana.library.entity.Copy;
import com.mana.library.repository.ReservationRepository;
import com.mana.library.repository.MemberRepository;
import com.mana.library.repository.BookRepository;
import com.mana.library.repository.CopyRepository;
import com.mana.library.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CopyRepository copyRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BrevoEmailService emailService;

    public String createReservation(Long memberId, Long bookId) {
        // Vérification de l'adhérent
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("Membre non trouvé"));

        // Vérification de l'éligibilité de l'adhérent
        if (!isMemberEligible(member)) {
            return "Non éligible à la réservation";
        }

        // Vérification du livre
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Livre non trouvé"));

        // Vérifier si le membre a déjà une réservation active
        Optional<Reservation> existingReservation = reservationRepository
            .findActiveReservationByMember(member, LocalDateTime.now());

        if (existingReservation.isPresent()) {
            return "Une réservation est déjà en cours";
        }

        // Vérifier la disponibilité du livre
        if (isBookAvailable(book)) {
            return "Disponible pour emprunt immédiat";
        }

        // Créer la réservation
        Reservation reservation = new Reservation(member, book);
        reservationRepository.save(reservation);

        // Envoyer email de confirmation
        sendReservationConfirmationEmail(reservation);

        return "Réservation créée avec succès";
    }

    private boolean isMemberEligible(Member member) {
        // Vérifier si le membre a un abonnement actif
        return member.getSubscriptions().stream()
            .anyMatch(subscription -> subscription.getEndDate().isAfter(LocalDateTime.now()));
    }

    private boolean isBookAvailable(Book book) {
        // Compter les exemplaires disponibles
        List<Copy> availableCopies = copyRepository.findByBookAndStatus(book, Copy.CopyStatus.AVAILABLE);

        // Compter les prêts actifs
        long activeLoans = loanRepository.countByBookAndReturnDateIsNull(book);

        return availableCopies.size() > activeLoans;
    }

    public void processExpiredReservations() {
        List<Reservation> expiredReservations = reservationRepository
            .findExpiredReservations(LocalDateTime.now());

        for (Reservation reservation : expiredReservations) {
            reservation.setStatus(Reservation.ReservationStatus.EXPIRED);
            reservationRepository.save(reservation);
        }
    }

    public void notifyAvailableReservations() {
        List<Reservation> reservationsToNotify = reservationRepository
            .findReservationsToNotify(LocalDateTime.now());

        for (Reservation reservation : reservationsToNotify) {
            if (isBookAvailable(reservation.getBook())) {
                // Vérifier la priorité FIFO
                List<Reservation> queueReservations = reservationRepository
                    .findActiveReservationsByBookOrderByDate(reservation.getBook(), LocalDateTime.now());

                if (!queueReservations.isEmpty() && queueReservations.getFirst().getId().equals(reservation.getId())) {
                    sendBookAvailableNotification(reservation);
                    reservation.setNotificationSent(true);
                    reservationRepository.save(reservation);
                }
            }
        }
    }

    public void fulfillReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        reservation.setStatus(Reservation.ReservationStatus.FULFILLED);
        reservationRepository.save(reservation);
    }

    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

    public List<Reservation> getMemberReservations(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("Membre non trouvé"));

        return reservationRepository.findByMemberOrderByReservationDateDesc(member);
    }

    private void sendReservationConfirmationEmail(Reservation reservation) {
        try {
            String subject = "Confirmation de réservation - Bibliothèque";
            String content = """
                Bonjour %s,
                
                Votre réservation pour le livre "%s" a été confirmée.
                Date de réservation : %s
                Date d'expiration : %s
                
                Vous recevrez un email dès que le livre sera disponible.
                
                Cordialement,
                L'équipe de la bibliothèque
                """.formatted(
                    reservation.getMember().getFirstName(),
                    reservation.getBook().getTitle(),
                    reservation.getReservationDate().toLocalDate(),
                    reservation.getExpiryDate().toLocalDate()
                );

            emailService.sendEmail(reservation.getMember().getEmail(), subject, content);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email de confirmation : " + e.getMessage());
        }
    }

    private void sendBookAvailableNotification(Reservation reservation) {
        try {
            String subject = "Livre disponible - Bibliothèque";
            String content = """
                Bonjour %s,
                
                Bonne nouvelle ! Le livre "%s" que vous avez réservé est maintenant disponible.
                Vous avez jusqu'au %s pour venir le retirer.
                
                Cordialement,
                L'équipe de la bibliothèque
                """.formatted(
                    reservation.getMember().getFirstName(),
                    reservation.getBook().getTitle(),
                    reservation.getExpiryDate().toLocalDate()
                );

            emailService.sendEmail(reservation.getMember().getEmail(), subject, content);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email de disponibilité : " + e.getMessage());
        }
    }
}
