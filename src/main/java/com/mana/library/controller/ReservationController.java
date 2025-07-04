package com.mana.library.controller;

import com.mana.library.entity.Reservation;
import com.mana.library.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/create")
    public ResponseEntity<String> createReservation(
            @RequestParam Long memberId,
            @RequestParam Long bookId) {
        try {
            String result = reservationService.createReservation(memberId, bookId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Reservation>> getMemberReservations(@PathVariable Long memberId) {
        try {
            List<Reservation> reservations = reservationService.getMemberReservations(memberId);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/cancel/{reservationId}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long reservationId) {
        try {
            reservationService.cancelReservation(reservationId);
            return ResponseEntity.ok("Réservation annulée avec succès");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }

    @PostMapping("/fulfill/{reservationId}")
    public ResponseEntity<String> fulfillReservation(@PathVariable Long reservationId) {
        try {
            reservationService.fulfillReservation(reservationId);
            return ResponseEntity.ok("Réservation terminée avec succès");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }
}
