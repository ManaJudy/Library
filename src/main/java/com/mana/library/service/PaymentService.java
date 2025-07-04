package com.mana.library.service;

import com.mana.library.entity.Member;
import com.mana.library.entity.Payment;
import com.mana.library.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment createPayment(Member member, String wayOfPayment, Double amount) {
        Payment payment = new Payment();
        payment.setMemberInPayment(member);
        payment.setWayOfPayment(wayOfPayment);
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDate.now());
        return paymentRepository.save(payment);
    }
}
