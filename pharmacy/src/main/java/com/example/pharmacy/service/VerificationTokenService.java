package com.example.pharmacy.service;

import com.example.pharmacy.model.Usuario;
import com.example.pharmacy.model.VerificationToken;

import java.util.Optional;

public interface VerificationTokenService {


    VerificationToken createVerificationToken(Usuario user);


    Optional<VerificationToken> findByToken(String token);


    boolean verifyToken(String token);

    boolean isEmailVerified(Long idUsuario);

    void cleanupExpiredTokens();
}
