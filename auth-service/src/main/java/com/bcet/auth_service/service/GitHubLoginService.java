package com.bcet.auth_service.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bcet.auth_service.client.UserServiceClient;
import com.bcet.auth_service.dto.UserResponseDto;
import com.bcet.auth_service.util.JwtUtil;
import com.bcet.auth_service.wrapper.JwtWrapper;

@Service
public class GitHubLoginService {

    private final UserServiceClient userServiceClient;
    private final JwtUtil jwtUtil;

    Logger logger = LoggerFactory.getLogger(GitHubLoginService.class);

    @Value("${oauth.github.client-id}")
    private String CLIENT_ID;

    @Value("${oauth.github.client-secret}")
    private String CLIENT_SECRET;

    @Value("${oauth.github.token-url}")
    private String TOKEN_URL;

    @Value("${oauth.github.user-url}")
    private String USER_URL;

    public GitHubLoginService(UserServiceClient userServiceClient, JwtUtil jwtUtil) {
        this.userServiceClient = userServiceClient;
        this.jwtUtil = jwtUtil;
    }

    @SuppressWarnings(value = { "rawtypes", "unchecked" })
    public JwtWrapper verifyGitHubTokenAndSaveUserAndGenerateJWT(String token) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Step 1: Exchange GitHub token for access token
            Map<String, String> tokenRequest = Map.of(
                    "client_id", CLIENT_ID,
                    "client_secret", CLIENT_SECRET,
                    "code", token);

            HttpHeaders tokenHeaders = new HttpHeaders();
            tokenHeaders.add(HttpHeaders.ACCEPT, "application/json");
            HttpEntity<Map<String, String>> tokenEntity = new HttpEntity<>(tokenRequest, tokenHeaders);
            ResponseEntity<Map> tokenResponse = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, tokenEntity,
                    Map.class);
            String accessToken = (String) tokenResponse.getBody().get("access_token");
            if (accessToken == null || accessToken.isEmpty()) {
                throw new RuntimeException("Failed to retrieve access token");
            }

            // Step 2: Retrieve user information from GitHub using access token
            HttpHeaders userHeaders = new HttpHeaders();
            userHeaders.add(HttpHeaders.ACCEPT, "application/json");
            userHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
            HttpEntity<Void> userEntity = new HttpEntity<>(userHeaders);
            ResponseEntity<Map> userResponse = restTemplate.exchange(USER_URL, HttpMethod.GET, userEntity, Map.class);
            Map<String, Object> userData = (Map<String, Object>) userResponse.getBody();
            logger.info("User information: {}", userData);
            if (userData == null || userData.isEmpty()) {
                throw new RuntimeException("Failed to retrieve user information");
            }

            logger.info("Starting to Debug");

            String githubId = String.valueOf(userData.get("id"));
            String email = String.valueOf(userData.get("email"));
            String name = String.valueOf(userData.get("name"));
            String avatar = String.valueOf(userData.get("avatar_url"));
            UserResponseDto user = null;

            logger.info("Data: {} :: {} :: {} :: {}", githubId, email, name, avatar);

            // Check user existence
            ResponseEntity<Boolean> isRegistered = userServiceClient.isEmailExists(email);
            logger.info("Is Registered Email: {}", isRegistered);

            if (isRegistered.getBody()) {
                // user already exists, so fetch the user details
                logger.info("Inside if");
                user = userServiceClient.getUserByEmail(email).getBody();
                logger.info("User: {}", user);
            } else {
                // user doesn't exist, so create the user
                logger.info("Inside else");
                user = new UserResponseDto();
                user.setUserId(githubId);
                user.setEmail(email);
                user.setName(name);
                user.setPicture(avatar);
                user.setProvider("Github");
                user.setRole("ROLE_STUDENT");

                // save the user in database
                ResponseEntity<UserResponseDto> savedUser = userServiceClient.createUser(user);
                logger.info("Saved user: {}", savedUser);

                if (savedUser.getStatusCode().is5xxServerError()) {
                    throw new RuntimeException("User-service not responding correctly, please try again later.");
                }
            }

            logger.info("End of the if-else");

            // Generate JWT
            String jwt = jwtUtil.generateJwtToken(user.getName(), user.getRole());
            JwtWrapper jwtWrapper = new JwtWrapper();
            jwtWrapper.setUserId(user.getUserId());
            jwtWrapper.setUserId(user.getUserId());
            jwtWrapper.setEmail(user.getEmail());
            jwtWrapper.setName(user.getName());
            jwtWrapper.setPicture(user.getPicture());
            jwtWrapper.setToken(jwt);

            logger.info("Generated JWT: {}", jwtWrapper);
            return jwtWrapper;
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify Github ID Token", e);
        }
    }

}
