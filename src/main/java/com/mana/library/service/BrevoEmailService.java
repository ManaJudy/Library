package com.mana.library.service;

import com.mana.library.entity.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class BrevoEmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.api.url}")
    private String apiUrl;

    @Value("${spring.mail.username}")
    private String email;

    public void sendMail(Member member, String subject, String content) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> body = new HashMap<>();

        Map<String, String> sender = new HashMap<>();
        sender.put("name", "Library");
        sender.put("email", email); // Doit être validé dans Brevo

        Map<String, String> to = new HashMap<>();
        to.put("email", member.getEmail());
        to.put("name", "Utilisateur"); // Tu peux mettre le vrai nom ici si tu l'as

        body.put("sender", sender);
        body.put("to", Collections.singletonList(to));
        body.put("subject", subject);
        body.put("textContent", content);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
            System.out.println("Email envoyé avec succès : " + response.getBody());
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email via API Brevo : " + e.getMessage());
        }
    }
}
