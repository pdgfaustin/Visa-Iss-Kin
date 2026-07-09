package com.visa_iss_kin.security;

import com.visa_iss_kin.model.Utilisateur;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class JwtService {
    private static final String SECRET_KEY =
            "VISA_ISS_KIN_SECRET_KEY_2026_TRES_LONGUE_ET_SECURISER_123456789";

    private static final long EXPIRATION_TIME =
            1000 * 60 * 60 * 24;

    public String genererToken(
            Utilisateur utilisateur
    ) {
        Date maintenant = new Date();
        Date expiration = new Date(
                maintenant.getTime() + EXPIRATION_TIME
        );

        return Jwts.builder()
                .subject(utilisateur.getUserName())
                .claim("nomComplet", utilisateur.getNomComplet())
                .claim("email", utilisateur.getEmail())
                .claim("role", utilisateur.getRole().name())
                .issuedAt(maintenant)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    public String extraireUserName(
            String token
    ) {
        return extraireClaims(token).getSubject();
    }

    public String extraireRole(
            String token
    ) {
        return extraireClaims(token)
                .get("role", String.class);
    }

    public boolean tokenValide(
            String token,
            Utilisateur utilisateur
    ) {
        String userName = extraireUserName(token);

        return userName.equals(utilisateur.getUserName())
                && !tokenExpire(token);
    }

    public boolean tokenExpire(
            String token
    ) {
        Date expiration =
                extraireClaims(token).getExpiration();

        return expiration.before(new Date());
    }

    private Claims extraireClaims(
            String token
    ) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Key getSigningKey() {
        byte[] keyBytes =
                SECRET_KEY.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
