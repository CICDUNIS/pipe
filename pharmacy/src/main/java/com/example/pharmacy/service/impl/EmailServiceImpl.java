package com.example.pharmacy.service.impl;

import com.example.pharmacy.model.Usuario;
import com.example.pharmacy.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = Logger.getLogger(EmailServiceImpl.class.getName());

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${admin.notification.email}")
    private String adminEmail;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public boolean sendVerificationEmail(Usuario user, String verificationToken) {
        logger.info("Sending verification email to: " + user.getCorreo());
        
        String verificationUrl = frontendUrl + "/verify-email?token=" + verificationToken;
        
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("name", user.getNombre());
        templateModel.put("verificationUrl", verificationUrl);
        
        String subject = "Verify Your Email - Pharmacy System";
        String htmlContent = buildEmailContent("verification-email", templateModel);
        
        return sendHtmlEmail(user.getCorreo(), subject, htmlContent);
    }

    @Override
    public boolean sendWelcomeEmail(Usuario user) {
        logger.info("Sending welcome email to: " + user.getCorreo());
        
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("name", user.getNombre());
        templateModel.put("loginUrl", frontendUrl + "/login");
        
        String subject = "Welcome to Pharmacy System";
        String htmlContent = buildEmailContent("welcome-email", templateModel);
        
        return sendHtmlEmail(user.getCorreo(), subject, htmlContent);
    }

    @Override
    public boolean sendAdminNotificationEmail(Usuario user) {
        logger.info("Sending admin notification email about new user: " + user.getCorreo());
        
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("userName", user.getNombre());
        templateModel.put("userEmail", user.getCorreo());
        templateModel.put("registrationDate", user.getFechaCreacion().toString());
        templateModel.put("adminUrl", frontendUrl + "/admin/users");
        
        String subject = "New User Registration - Pharmacy System";
        String htmlContent = buildEmailContent("admin-notification", templateModel);
        
        return sendHtmlEmail(adminEmail, subject, htmlContent);
    }

    @Override
    public boolean sendAccountActivationEmail(Usuario user, String role) {
        logger.info("Sending account activation email to: " + user.getCorreo());
        
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("name", user.getNombre());
        templateModel.put("role", role);
        templateModel.put("loginUrl", frontendUrl + "/login");
        
        String subject = "Your Account Has Been Activated - Pharmacy System";
        String htmlContent = buildEmailContent("account-activation", templateModel);
        
        return sendHtmlEmail(user.getCorreo(), subject, htmlContent);
    }

    private String buildEmailContent(String templateName, Map<String, Object> model) {
        Context context = new Context();
        model.forEach(context::setVariable);
        
        if (templateEngine != null) {
            return templateEngine.process(templateName, context);
        } else {
            return buildSimpleHtmlEmail(templateName, model);
        }
    }

    private String buildSimpleHtmlEmail(String templateType, Map<String, Object> model) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><style>");
        html.append("body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }");
        html.append(".container { max-width: 600px; margin: 0 auto; padding: 20px; }");
        html.append(".header { background-color: #4CAF50; color: white; padding: 10px; text-align: center; }");
        html.append(".content { padding: 20px; border: 1px solid #ddd; }");
        html.append(".button { display: inline-block; background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; }");
        html.append(".footer { margin-top: 20px; text-align: center; font-size: 12px; color: #777; }");
        html.append("</style></head><body><div class='container'>");
        html.append("<div class='header'><h2>Pharmacy System</h2></div><div class='content'>");

        switch (templateType) {
            case "verification-email":
                html.append("<p>Hello ").append(model.get("name")).append(",</p>");
                html.append("<p>Thank you for registering with our Pharmacy System. Please verify your email address by clicking the button below:</p>");
                html.append("<p><a class='button' href='").append(model.get("verificationUrl")).append("'>Verify Email</a></p>");
                html.append("<p>If the button doesn't work, copy and paste this URL into your browser:</p>");
                html.append("<p>").append(model.get("verificationUrl")).append("</p>");
                break;
                
            case "welcome-email":
                html.append("<p>Hello ").append(model.get("name")).append(",</p>");
                html.append("<p>Welcome to the Pharmacy System! Your account has been verified successfully.</p>");
                html.append("<p>You can now log in to your account using your email and password.</p>");
                html.append("<p><a class='button' href='").append(model.get("loginUrl")).append("'>Log In</a></p>");
                break;
                
            case "admin-notification":
                html.append("<p>Hello Administrator,</p>");
                html.append("<p>A new user has registered with the Pharmacy System:</p>");
                html.append("<p><strong>Name:</strong> ").append(model.get("userName")).append("</p>");
                html.append("<p><strong>Email:</strong> ").append(model.get("userEmail")).append("</p>");
                html.append("<p><strong>Registration Date:</strong> ").append(model.get("registrationDate")).append("</p>");
                html.append("<p>Please review and activate this account if appropriate.</p>");
                html.append("<p><a class='button' href='").append(model.get("adminUrl")).append("'>Manage Users</a></p>");
                break;
                
            case "account-activation":
                html.append("<p>Hello ").append(model.get("name")).append(",</p>");
                html.append("<p>Your account has been activated with the role of <strong>").append(model.get("role")).append("</strong>.</p>");
                html.append("<p>You can now log in to access all features available to your role.</p>");
                html.append("<p><a class='button' href='").append(model.get("loginUrl")).append("'>Log In</a></p>");
                break;
                
            default:
                html.append("<p>This is a notification from the Pharmacy System.</p>");
        }

        html.append("</div><div class='footer'>");
        html.append("<p>This is an automated message. Please do not reply to this email.</p>");
        html.append("<p>&copy; ").append(java.time.Year.now().getValue()).append(" Pharmacy System. All rights reserved.</p>");
        html.append("</div></div></body></html>");

        return html.toString();
    }

    private boolean sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            logger.info("Email sent successfully to: " + to);
            return true;
        } catch (MessagingException e) {
            logger.log(Level.SEVERE, "Failed to send email to: " + to, e);
            return false;
        }
    }
} 
