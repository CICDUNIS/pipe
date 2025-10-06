package com.example.pharmacy.service.impl;

import com.example.pharmacy.model.Usuario;
import com.example.pharmacy.model.VerificationToken;
import com.example.pharmacy.repository.VerificationTokenRepository;
import com.example.pharmacy.service.VerificationTokenService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private static final Logger logger = Logger.getLogger(VerificationTokenServiceImpl.class.getName());
    private static final int EXPIRATION_HOURS = 24;

    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationTokenServiceImpl(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    @Transactional
    public VerificationToken createVerificationToken(Usuario user) {
        logger.info("Creating verification token for user: " + user.getCorreo());

        verificationTokenRepository.findByIdUsuario(user.getIdUsuario())
                .ifPresent(token -> verificationTokenRepository.deleteById(token.getIdToken()));

        String tokenValue = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plusHours(EXPIRATION_HOURS);

        VerificationToken verificationToken = new VerificationToken(
                tokenValue,
                user.getIdUsuario(),
                now,
                expiryDate
        );

        VerificationToken savedToken = verificationTokenRepository.save(verificationToken);
        logger.info("Verification token created successfully for user: " + user.getCorreo());

        return savedToken;
    }

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        logger.info("Finding verification token: " + token);
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public boolean verifyToken(String token) {
        logger.info("Verifying token: " + token);

        Optional<VerificationToken> optionalToken = verificationTokenRepository.findByToken(token);

        if (optionalToken.isEmpty()) {
            logger.warning("Token not found: " + token);
            return false;
        }

        VerificationToken verificationToken = optionalToken.get();

        if (verificationToken.isExpired()) {
            logger.warning("Token expired: " + token);
            return false;
        }

        if (verificationToken.isVerified()) {
            logger.warning("Token already verified: " + token);
            return true;
        }

        LocalDateTime now = LocalDateTime.now();
        verificationTokenRepository.updateVerificationDate(token, now);
        logger.info("Token verified successfully: " + token);

        return true;
    }

    @Override
    public boolean isEmailVerified(Long idUsuario) {
        logger.info("Checking if email is verified for user ID: " + idUsuario);
        int count = verificationTokenRepository.countVerifiedTokensByUser(idUsuario);
        boolean verified = count > 0;
        logger.info("Email verification status for user ID " + idUsuario + ": " + verified);
        return verified;
    }

    @Override
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void cleanupExpiredTokens() {
        logger.info("Cleaning up expired verification tokens");
        LocalDateTime now = LocalDateTime.now();
        verificationTokenRepository.deleteExpiredTokens(now);
        logger.info("Expired verification tokens cleanup completed");
    }
}
