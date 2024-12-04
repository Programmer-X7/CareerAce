package com.bcet.auth_service.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
@RefreshScope
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String JWT_SECRET_KEY;

    @Value("${jwt.token-validity}")
    private long TOKEN_VALIDITY;

    public String generateJwtToken(String userId, String role) {
        Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET_KEY);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + TOKEN_VALIDITY);

        return JWT.create()
                .withIssuer("careerace")
                .withSubject(userId)
                .withClaim("role", role)
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(algorithm);
    }

    // public DecodedJWT validateToken(String token) {
    // try {
    // Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET_KEY);
    // JWTVerifier verifier =
    // JWT.require(algorithm).withIssuer("careerace").build();
    // return verifier.verify(token);
    // } catch (JWTVerificationException e) {
    // throw new RuntimeException("Invalid or expired JWT token", e);
    // }
    // }

    // public String getUserIdFromToken(String token) {
    // DecodedJWT jwt = validateToken(token);
    // return jwt.getSubject(); // Extracts the "sub" (user ID) claim
    // }

}
