package com.bcet.auth_service.service;

import com.bcet.auth_service.client.UserServiceClient;
import com.bcet.auth_service.dto.UserResponseDto;
import com.bcet.auth_service.util.JwtUtil;
import com.bcet.auth_service.wrapper.JwtWrapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleLoginService {

    @Value("${oauth.google.client-id}")
    private String CLIENT_ID;

    private final JwtUtil jwtUtil;
    private final UserServiceClient userServiceClient;

    Logger logger = LoggerFactory.getLogger(GoogleLoginService.class);

    public GoogleLoginService(JwtUtil jwtUtil, UserServiceClient userServiceClient) {
        this.jwtUtil = jwtUtil;
        this.userServiceClient = userServiceClient;
    }

    // Helper method to initialize google token verifier
    private GoogleIdTokenVerifier getVerifier() {
        return new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
    }

    public JwtWrapper verifyGoogleTokenAndSaveUserAndGenerateJWT(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = getVerifier();

            logger.info("idTokenString: {}", idTokenString);
            logger.info("Client id: {}", CLIENT_ID);
            GoogleIdToken idToken = verifier.verify(idTokenString);
            logger.info("GoogleToken: {}", idToken);

            if (idToken != null) {
                var payload = idToken.getPayload();
                String gmail = payload.getEmail();
                UserResponseDto user = null;

                logger.info("email: {}", gmail);

                // Check if user already exist in database
                ResponseEntity<Boolean> isRegistered = userServiceClient.isEmailExists(gmail);

                logger.info("User response: {}", isRegistered);

                if (isRegistered.getBody()) {
                    // user already exists, so fetch the user details
                    user = userServiceClient.getUserByEmail(gmail).getBody();

                    logger.info("User: {}", user);

                } else {
                    // user doesn't exist, so create the user
                    user = new UserResponseDto();
                    user.setUserId(payload.getSubject());
                    user.setEmail(gmail);
                    user.setName(payload.get("name").toString());
                    user.setPicture(payload.get("picture").toString());
                    user.setProvider("Google");
                    user.setRole("ROLE_STUDENT");

                    // save the user in database
                    ResponseEntity<UserResponseDto> savedUser = userServiceClient.createUser(user);

                    logger.info("Saved user: {}", savedUser);

                    if (savedUser.getStatusCode().is5xxServerError()) {
                        throw new RuntimeException("User-service not responding correctly, please try again later.");
                    }
                }

                // Generate JWT
                String jwt = jwtUtil.generateJwtToken(user.getUserId(), user.getRole());
                JwtWrapper jwtWrapper = new JwtWrapper();
                jwtWrapper.setUserId(user.getUserId());
                jwtWrapper.setEmail(user.getEmail());
                jwtWrapper.setName(user.getName());
                jwtWrapper.setPicture(user.getPicture());
                jwtWrapper.setToken(jwt);
                jwtWrapper.setRole(user.getRole());

                logger.info("Generated JWT: {}", jwtWrapper);
                return jwtWrapper;
            } else {
                throw new RuntimeException("Invalid Google ID Token");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify Google ID Token", e);
        }
    }

}