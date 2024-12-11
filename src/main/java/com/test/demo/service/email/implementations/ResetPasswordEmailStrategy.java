package com.test.demo.service.email.implementations;

import com.test.demo.model.User;
import com.test.demo.service.email.EmailStrategy;
import org.thymeleaf.context.Context;

public class ResetPasswordEmailStrategy implements EmailStrategy {

    private final User user;
    private final String resetLink;

    public ResetPasswordEmailStrategy(User user, String resetLink) {
        this.user = user;
        this.resetLink = resetLink;
    }

    @Override
    public void prepareContext(Context context) {
        context.setVariable("user", user);
        context.setVariable("resetLink", resetLink);
    }

    @Override
    public String getSubject() {
        return "Reset Your Password";
    }

    @Override
    public String getTemplateLocation() {
        return "emails/resetPassword";  // Musisz mieć szablon e-maila
    }

}
