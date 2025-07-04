package com.mana.library.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@Service
public class BrevoEmailService {

    @Value("${brevo.api.key:}")
    private String apiKey;

    @Value("${brevo.sender.email:noreply@library.com}")
    private String senderEmail;

    @Value("${brevo.sender.name:Bibliothèque}")
    private String senderName;

    private final RestTemplate restTemplate;

    public BrevoEmailService() {
        this.restTemplate = new RestTemplate();
    }

    public void sendEmail(String to, String subject, String content) {
        try {
            if (apiKey == null || apiKey.isEmpty()) {
                System.out.println("Email simulé envoyé à: " + to);
                System.out.println("Sujet: " + subject);
                System.out.println("Contenu: " + content);
                return;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", apiKey);

            Map<String, Object> emailData = new HashMap<>();
            Map<String, String> sender = new HashMap<>();
            sender.put("email", senderEmail);
            sender.put("name", senderName);
            emailData.put("sender", sender);

            Map<String, String> recipient = new HashMap<>();
            recipient.put("email", to);
            emailData.put("to", new Map[]{recipient});

            emailData.put("subject", subject);
            emailData.put("textContent", content);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(emailData, headers);

            restTemplate.postForEntity(
                "https://api.brevo.com/v3/smtp/email",
                request,
                String.class
            );

            System.out.println("Email envoyé avec succès à: " + to);

        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email: " + e.getMessage());
            // En cas d'erreur, afficher le contenu comme simulation
            System.out.println("Email simulé envoyé à: " + to);
            System.out.println("Sujet: " + subject);
            System.out.println("Contenu: " + content);
        }
    }
}
