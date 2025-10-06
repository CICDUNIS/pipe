package com.example.pharmacy.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class JwtUtils {

    private static final Logger logger = Logger.getLogger(JwtUtils.class.getName());
    
    private static final String SECRET_KEY = "ThisIsAVerySecureKeyForJWTSigningPleaseChangeInProduction";
    private final Key key;

    private final long EXPIRATION_MS = 24 * 60 * 60 * 1000; // 1 dia

    public JwtUtils() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        logger.info("JwtUtils initialized with consistent key");
        logger.info("Key algorithm: " + key.getAlgorithm());
        logger.info("Key format: " + key.getFormat());
    }

    public String generateToken(String correo, String primaryRole, List<String> roles) {
        logger.info("Generating token for user: " + correo);
        logger.info("User has the following roles: " + String.join(", ", roles));
        logger.info("Using primary role: " + primaryRole);
        
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_MS);
        logger.info("Token will expire at: " + expiryDate);
        
        String token = Jwts.builder()
            .setSubject(correo)
            .claim("rol", primaryRole)
            .claim("roles", roles)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key)
            .compact();
        
        logger.info("Token generated successfully: " + token.substring(0, Math.min(20, token.length())) + "...");
        return token;
    }

    public boolean validateToken(String token) {
        logger.info("Validating token: " + token.substring(0, Math.min(20, token.length())) + "...");
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            logger.info("Token is valid");
            return true;
        } catch (ExpiredJwtException e) {
            logger.severe("Token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.severe("Token format not supported: " + e.getMessage());
        } catch (MalformedJwtException e) {
            logger.severe("Token is malformed: " + e.getMessage());
        } catch (SignatureException e) {
            logger.severe("Invalid token signature: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.severe("Token is empty or has invalid arguments: " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unknown error validating token", e);
        }
        return false;
    }

    public String getCorreoFromToken(String token) {
        logger.info("Extracting email from token: " + token.substring(0, Math.min(20, token.length())) + "...");
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody();
            String correo = claims.getSubject();
            logger.info("Email extracted: " + correo);
            return correo;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error extracting email from token", e);
            throw e;
        }
    }

    public String getRolFromToken(String token) {
        logger.info("Extracting role from token: " + token.substring(0, Math.min(20, token.length())) + "...");
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody();
            String rol = (String) claims.get("rol");
            logger.info("Role extracted: " + rol);
            return rol;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error extracting role from token", e);
            throw e;
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        logger.info("Extracting roles from token: " + token.substring(0, Math.min(20, token.length())) + "...");
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody();
            List<String> roles = (List<String>) claims.get("roles");
            logger.info("Roles extracted: " + (roles != null ? String.join(", ", roles) : "none"));
            return roles;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error extracting roles from token", e);
            throw e;
        }
    }
}


