package com.example.fptufindingmotelv1.service.login;

import com.example.fptufindingmotelv1.model.CustomUserDetails;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    @Value("${ffm.app.secret}")
    private String jwtSecret;

    @Value("${facebook.app.secret}")
    private String facebookSecret;

    @Value("${ffm.app.expiration}")
    private String jwtExpiration;

    public String generateToken(CustomUserDetails userDetails){
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + Long.parseLong(jwtExpiration));
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsernameFromJWT(String token){
        Claims claims = Jwts.parser()
                        .setSigningKey(jwtSecret)
                        .parseClaimsJws(token)
                        .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String authToken){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
