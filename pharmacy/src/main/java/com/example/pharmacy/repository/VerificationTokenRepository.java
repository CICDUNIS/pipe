package com.example.pharmacy.repository;

import com.example.pharmacy.model.VerificationToken;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long> {
    
    Optional<VerificationToken> findByToken(String token);
    
    Optional<VerificationToken> findByIdUsuario(Long idUsuario);
    
    @Modifying
    @Query("UPDATE VERIFICATION_TOKEN SET FECHA_VERIFICACION = :fechaVerificacion WHERE TOKEN = :token")
    void updateVerificationDate(@Param("token") String token, @Param("fechaVerificacion") LocalDateTime fechaVerificacion);
    
    @Query("SELECT COUNT(*) FROM VERIFICATION_TOKEN WHERE ID_USUARIO = :idUsuario AND FECHA_VERIFICACION IS NOT NULL")
    int countVerifiedTokensByUser(@Param("idUsuario") Long idUsuario);
    
    @Modifying
    @Query("DELETE FROM VERIFICATION_TOKEN WHERE FECHA_EXPIRACION < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
} 