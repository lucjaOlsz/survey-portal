package com.test.demo.utils;

import com.test.demo.exceptions.InvalidTokenException;
import com.test.demo.model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/** Own implementation of a verification token for email verification lol ***/
public record VerificationToken(String email, LocalDateTime expirationDate, String tokenHash) {
    private static final String SECRET_KEY = "RandomForTestingPurposesOnly123321123";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static VerificationToken create(User user) {
        String email = user.getEmail();
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(1);
        String data = email + "|" + expirationDate.format(DATE_FORMATTER);
        String hash = hashWithSHA256(data + SECRET_KEY);
        return new VerificationToken(email, expirationDate, hash);
    }

    public static VerificationToken parse(String encodedToken) {
        String decodedToken = new String(Base64.getDecoder().decode(encodedToken), StandardCharsets.UTF_8);
        String[] parts = decodedToken.split("\\|");
        if (parts.length != 3) {
            throw new InvalidTokenException("Unprocessable token format");
        }

        String email = parts[0];
        LocalDateTime expirationDate = LocalDateTime.parse(parts[1], DATE_FORMATTER);
        String tokenHash = parts[2];

        return new VerificationToken(email, expirationDate, tokenHash);
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
