package com.test.demo.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public record PasswordResetToken(String email, LocalDateTime expirationDate, String tokenHash) {
    private static final String SECRET_KEY = "SomeSecretForResetPassword";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static PasswordResetToken create(String email) {
        LocalDateTime expirationDate = LocalDateTime.now().plusHours(1);  // Token ważny przez godzinę
        String data = email + "|" + expirationDate.format(DATE_FORMATTER);
        String hash = hashWithSHA256(data + SECRET_KEY);
        return new PasswordResetToken(email, expirationDate, hash);
    }

    public static PasswordResetToken parse(String encodedToken) {
        String decodedToken = new String(Base64.getDecoder().decode(encodedToken), StandardCharsets.UTF_8);
        String[] parts = decodedToken.split("\\|");
        if (parts.length != 3) {
            throw new RuntimeException("Unprocessable token format");
        }
        String email = parts[0];
        LocalDateTime expirationDate = LocalDateTime.parse(parts[1], DATE_FORMATTER);
        String tokenHash = parts[2];
        return new PasswordResetToken(email, expirationDate, tokenHash);
    }

    public boolean isValid() {
        String originalData = email + "|" + expirationDate.format(DATE_FORMATTER);
        String expectedHash = hashWithSHA256(originalData + SECRET_KEY);
        return expectedHash.equals(tokenHash) && LocalDateTime.now().isBefore(expirationDate);
    }

    public String encode() {
        String data = email + "|" + expirationDate.format(DATE_FORMATTER) + "|" + tokenHash;
        return Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    private static String hashWithSHA256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error creating SHA-256 hash", e);
        }
    }
}
