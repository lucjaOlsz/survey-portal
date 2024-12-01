package com.test.demo.service.email;

import jakarta.mail.MessagingException;


public interface EmailService {
    void sendEmail(EmailStrategy strategy, String[] recipients) throws MessagingException;
    void sendEmail(EmailStrategy strategy, String recipient) throws MessagingException;
}
