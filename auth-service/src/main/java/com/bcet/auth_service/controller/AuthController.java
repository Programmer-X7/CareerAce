package com.bcet.auth_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bcet.auth_service.dto.SocialTokenDto;
import com.bcet.auth_service.dto.UserResponseDto;
import com.bcet.auth_service.dto.EmailSignupRequestDto;
import com.bcet.auth_service.service.EmailAuthService;
import com.bcet.auth_service.service.GitHubLoginService;
import com.bcet.auth_service.service.GoogleLoginService;
import com.bcet.auth_service.wrapper.JwtWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final GoogleLoginService googleLoginService;
    private final GitHubLoginService gitHubLoginService;
    private final EmailAuthService emailAuthService;

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(GoogleLoginService googleLoginService, EmailAuthService emailAuthService,
            GitHubLoginService gitHubLoginService) {
        this.googleLoginService = googleLoginService;
        this.gitHubLoginService = gitHubLoginService;
        this.emailAuthService = emailAuthService;
    }

    // Google login & signup API
    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody SocialTokenDto token) {
        try {
            logger.info("Google Token: {}", token);
            JwtWrapper jwt = googleLoginService.verifyGoogleTokenAndSaveUserAndGenerateJWT(token.token());
            logger.info("jwt: {}", jwt);
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An unexpected error occurred, please try again");
        }
    }

    // GitHub Login & signup API
    @PostMapping("/github")
    public ResponseEntity<?> githubLogin(@RequestBody SocialTokenDto token) {
        try {
            logger.info("Github Token: {}", token);
            JwtWrapper jwt = gitHubLoginService.verifyGitHubTokenAndSaveUserAndGenerateJWT(token.token());
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An unexpected error occurred, please try again");
        }
    }

    // Basic (Email) Signup API
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody EmailSignupRequestDto user) {
        boolean isRegisteredEmail = emailAuthService.isRegisteredEmail(user.getEmail());
        if (isRegisteredEmail) {
            return ResponseEntity.badRequest().body("Email is already registered, please login");
        }

        try {
            // store user details in redis until verification is complete (10 minutes)
            emailAuthService.storeUserInRedis(user);
            // Send otp in rabbitmq server
            emailAuthService.sendOtpToEmail(user.getEmail());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An unexpected error occurred, please try again later");
        }

        return ResponseEntity.ok("OTP successfully send to your email");
    }

    @PostMapping("/signup/verify-otp")
    public ResponseEntity<?> verifySignupOtpAndSaveUserAndGenerateJWT(
            @RequestParam String email, @RequestParam String otp) {
        if (!emailAuthService.verifyLoginOtp(email, otp)) {
            return new ResponseEntity<>("Invalid OTP", HttpStatus.BAD_REQUEST);
        }
        // Save the user in database
        UserResponseDto createdUser = emailAuthService.createUserAccount(email);

        JwtWrapper jwtToken = emailAuthService.generateJwt(createdUser);
        if (jwtToken.getToken() == null) {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(jwtToken);
    }

    // Email Login API
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email) {
        boolean isRegisteredEmail = emailAuthService.isRegisteredEmail(email);

        // Check if the user is not registered
        if (!isRegisteredEmail) {
            return ResponseEntity.badRequest().body("Email is not registered, please create a new account");
        }

        // Send otp to email address
        try {
            emailAuthService.sendOtpToEmail(email);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to send otp, please try again later");
        }

        return new ResponseEntity<>("OTP successfully send to your email", HttpStatus.OK);
    }

    @PostMapping("/login/verify-otp")
    public ResponseEntity<?> verifyLoginOtpAndGenerateJWT(
            @RequestParam String email, @RequestParam String otp) {

        // Verify OTP
        Boolean isValidOtp = emailAuthService.verifyLoginOtp(email, otp);
        if (!isValidOtp) {
            return new ResponseEntity<>("Invalid OTP, please try again", HttpStatus.BAD_REQUEST);
        }

        // Fetch user details by email
        UserResponseDto user = emailAuthService.getUserByEmail(email);
        if (user == null) {
            return new ResponseEntity<>("Something went wrong, please try again later",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Generate jwt
        JwtWrapper jwt = emailAuthService.generateJwt(user);
        if (jwt == null) {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(jwt);
    }

}
