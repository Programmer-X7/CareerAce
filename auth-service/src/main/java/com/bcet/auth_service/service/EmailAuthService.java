package com.bcet.auth_service.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bcet.auth_service.client.UserServiceClient;
import com.bcet.auth_service.dto.EmailSignupRequestDto;
import com.bcet.auth_service.dto.UserResponseDto;
import com.bcet.auth_service.exception.RabbitMQException;
import com.bcet.auth_service.exception.RedisException;
import com.bcet.auth_service.util.JwtUtil;
import com.bcet.auth_service.util.OtpUtil;
import com.bcet.auth_service.wrapper.JwtWrapper;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

@Service
public class EmailAuthService {

    private final RedisTemplate<String, String> redisTemplate;
    private final OtpUtil otpUtil;
    private final JwtUtil jwtUtil;
    private final UserServiceClient userProxy;

    public EmailAuthService(RedisTemplate<String, String> redisTemplate, OtpUtil otpUtil, UserServiceClient userProxy,
            JwtUtil jwtUtil) {
        this.redisTemplate = redisTemplate;
        this.otpUtil = otpUtil;
        this.userProxy = userProxy;
        this.jwtUtil = jwtUtil;
    }

    public boolean isRegisteredEmail(String email) {
        ResponseEntity<Boolean> isExistingUser = userProxy.isEmailExists(email);
        Boolean body = isExistingUser.getBody();
        return body;
    }

    public void sendOtpToEmail(String email) {
        String otp = otpUtil.generateOtp();
        try {
            otpUtil.storeOtp(email, otp);
            otpUtil.sendOtpToQueue(email, otp);
        } catch (RedisException e) {
            System.out.println("Failed to store OTP in Redis: " + e.getMessage());
            throw new RuntimeException("RabbitMQ Error: " + e.getMessage());
        } catch (RabbitMQException e) {
            System.out.println("Failed to send OTP to queue :" + e.getMessage());
            throw new RuntimeException("RabbitMQ Error: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("OTP Error: " + e.getMessage());
        }
    }

    public boolean verifyLoginOtp(String email, String enteredOtp) {
        return otpUtil.verifyOtp(email, enteredOtp);
    }

    public JwtWrapper generateJwt(UserResponseDto user) {
        String token = jwtUtil.generateJwtToken(user.getUserId(), user.getRole());
        return new JwtWrapper(user.getUserId(), user.getEmail(), user.getName(), user.getPicture(), token);
    }

    public void storeUserInRedis(EmailSignupRequestDto user) {
        String key = "auth:user:" + user.getEmail();
        String value = user.toString();
        System.out.println("value: " + value);
        try {
            redisTemplate.opsForValue().set(key, value, 10, TimeUnit.MINUTES);
        } catch (Exception e) {
            System.out.println("Error saving user in redis: " + e.getMessage());
            throw new RuntimeException("Error saving user details in Redis");
        }
    }

    public UserResponseDto createUserAccount(String email) {
        // fetch user information from redis
        String key = "auth:user:" + email;
        String stringUser = redisTemplate.opsForValue().get(key);
        EmailSignupRequestDto userDetails = new EmailSignupRequestDto().toDto(stringUser);

        // Save user to database
        UserResponseDto userToCreate = new UserResponseDto();
        userToCreate.setEmail(userDetails.getEmail());
        userToCreate.setName(userDetails.getName());
        userToCreate.setPhoneNumber(userDetails.getPhoneNumber());
        userToCreate.setCountry(userDetails.getCountry());
        userToCreate.setProvider(userDetails.getProvider());
        userToCreate.setRole("ROLE_STUDENT");

        return userProxy.createUser(userToCreate).getBody();
    }

    public UserResponseDto getUserByEmail(String email) {
        ResponseEntity<UserResponseDto> user = userProxy.getUserByEmail(email);
        if (!user.getStatusCode().is2xxSuccessful()) {
            return null;
        }

        return user.getBody();
    }

}
