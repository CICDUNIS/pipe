package com.example.pharmacy.service;

import com.example.pharmacy.model.Usuario;

public interface EmailService {

    boolean sendVerificationEmail(Usuario user, String verificationToken);

    boolean sendWelcomeEmail(Usuario user);

    boolean sendAdminNotificationEmail(Usuario user);

    boolean sendAccountActivationEmail(Usuario user, String role);
}
