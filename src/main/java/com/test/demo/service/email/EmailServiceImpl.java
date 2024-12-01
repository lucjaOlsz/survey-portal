package com.test.demo.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Arrays;
import java.util.logging.Logger;

@Service
public class EmailServiceImpl implements EmailService {
    private final Logger logger = Logger.getLogger(EmailServiceImpl.class.getName());

    @Value("${spring.mail.username}")
    private String fromMail;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;


    @Override
    public void sendEmail(EmailStrategy strategy, String[] recipients) {
        Context context = new Context();
        strategy.prepareContext(context);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessageHelper.setFrom(fromMail);
            mimeMessageHelper.setTo(recipients);
            mimeMessageHelper.setSubject(strategy.getSubject());
            String processedString = templateEngine.process(strategy.getTemplateLocation(), context);
            mimeMessageHelper.setText(processedString, true);

            mailSender.send(mimeMessage);
            logger.info("Email sent successfully to " + Arrays.toString(recipients));
        } catch (MessagingException e) {
            logger.severe("Failed to send email to " + Arrays.toString(recipients) + ": " + e.getMessage());
            throw new RuntimeException("Failed to send email.", e);
        }

    }

    @Override
    public void sendEmail(EmailStrategy strategy, String recipient) {
        sendEmail(strategy, new String[]{recipient});
    }

}
