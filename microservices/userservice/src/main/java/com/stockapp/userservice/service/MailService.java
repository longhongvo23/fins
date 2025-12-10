package com.stockapp.userservice.service;

import com.stockapp.userservice.service.dto.AppUserDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Service for sending emails.
 * Uses Thymeleaf templates for email content.
 */
@Service
public class MailService {

    private static final Logger LOG = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";
    private static final String BASE_URL = "baseUrl";

    @Value("${spring.mail.from:honglongvo23@gmail.com}")
    private String from;

    @Value("${application.base-url:http://localhost:8080}")
    private String baseUrl;

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public MailService(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Send email from template asynchronously.
     */
    @Async
    public void sendEmailFromTemplate(AppUserDTO user, String templateName, String titleKey, String subject) {
        if (user.getEmail() == null) {
            LOG.warn("Email doesn't exist for user '{}'", user.getLogin());
            return;
        }
        Locale locale = Locale.forLanguageTag("en");
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process(templateName, context);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    /**
     * Send activation email.
     */
    public Mono<Void> sendActivationEmail(AppUserDTO user) {
        return Mono.fromRunnable(() -> {
            LOG.debug("Sending activation email to '{}'", user.getEmail());
            sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title",
                    "Account Activation - FinStock");
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    /**
     * Send creation email.
     */
    public Mono<Void> sendCreationEmail(AppUserDTO user) {
        return Mono.fromRunnable(() -> {
            LOG.debug("Sending creation email to '{}'", user.getEmail());
            sendEmailFromTemplate(user, "mail/creationEmail", "email.creation.title", "Account Created - FinStock");
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    /**
     * Send password reset email.
     */
    public Mono<Void> sendPasswordResetMail(AppUserDTO user) {
        return Mono.fromRunnable(() -> {
            LOG.debug("Sending password reset email to '{}'", user.getEmail());
            sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title", "Password Reset - FinStock");
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    /**
     * Send simple email.
     */
    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        LOG.debug(
                "Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart,
                isHtml,
                to,
                subject,
                content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            LOG.debug("Sent email to User '{}'", to);
        } catch (MessagingException e) {
            LOG.warn("Email could not be sent to user '{}'", to, e);
        }
    }
}
