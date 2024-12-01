package com.test.demo.service.email.implementations;


import com.test.demo.model.User;
import com.test.demo.service.email.EmailStrategy;
import org.thymeleaf.context.Context;

public class VerificationEmailStrategy implements EmailStrategy {
    private final User user;
    private final String token;

    public VerificationEmailStrategy(User user, String token) {
        if (user == null || token == null) {
            throw new IllegalArgumentException("User and token must be provided");
        }
        this.user = user;
        this.token = token;
    }

    @Override
    public String getSubject() {
        return "Verify your email";
    }

    @Override
    public String getTemplateLocation() {
        return "emails/verification";
    }

    @Override
    public void prepareContext(Context context) {
        context.setVariable("user", user);
        context.setVariable("token", token);
        context.setVariable("redirectUrl", "http://localhost:8080/verify-email?token=" + token);
    }
}




