package com.bcet.auth_service.util;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.bcet.auth_service.dto.EmailDto;
import com.bcet.auth_service.exception.RabbitMQException;
import com.bcet.auth_service.exception.RedisException;

@Component
public class OtpUtil {

    private RedisTemplate<String, String> redisTemplate;
    private RabbitTemplate rabbitTemplate;

    public OtpUtil(RedisTemplate<String, String> redisTemplate, RabbitTemplate rabbitTemplate) {
        this.redisTemplate = redisTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    public String generateOtp() {
        SecureRandom secureRandom = new SecureRandom();
        int otp = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(otp);
    }

    public void storeOtp(String email, String otp) throws RedisException {
        try {
            String key = "auth:otp:" + email;
            redisTemplate.opsForValue().set(key, otp, 10, TimeUnit.MINUTES); // otp lifespan is 10 minutes
        } catch (Exception e) {
            e.printStackTrace();
            throw new RedisException("Error Sending OTP to Rabbit MQ Server");
        }
    }

    public boolean verifyOtp(String email, String receivedOtp) {
        String key = "auth:otp:" + email;
        String userDetailsKey = "auth:user:" + email;
        String storedOtp = redisTemplate.opsForValue().get(key);
        if (storedOtp == null) {
            return false;
        }
        boolean isCorrectOtp = receivedOtp.equals(storedOtp);
        if (isCorrectOtp) {
            redisTemplate.delete(key); // delete the otp from redis after successful verification to prevent REUSE of OTP
            redisTemplate.delete(userDetailsKey);  // delete user details from redis after successful verification
            return true;
        }
        return false;
    }

    public void sendOtpToQueue(String email, String otp) throws RabbitMQException {
        String subject = "CareerAce | OTP";
        String body = String.format("<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "  <meta charset=\"UTF-8\">" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "  <title>Email Template</title>" +
                "  <style>" +
                "    body {" +
                "      font-family: Arial, sans-serif;" +
                "      margin: 0;" +
                "      padding: 0;" +
                "      background-color: #f4f4f4;" +
                "    }" +
                "    .email-container {" +
                "      max-width: 600px;" +
                "      margin: 0 auto;" +
                "      background-color: #ffffff;" +
                "      border-radius: 8px;" +
                "      overflow: hidden;" +
                "      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);" +
                "    }" +
                "    .email-header {" +
                "      background-color: #1e88e5;" +
                "      padding: 20px;" +
                "      text-align: center;" +
                "      color: white;" +
                "      font-size: 24px;" +
                "      font-weight: bold;" +
                "    }" +
                "    .email-body {" +
                "      padding: 20px;" +
                "      color: #333333;" +
                "    }" +
                "    .email-body h2 {" +
                "      color: #1e88e5;" +
                "      font-size: 22px;" +
                "    }" +
                "    .email-body p {" +
                "      line-height: 1.6;" +
                "      font-size: 16px;" +
                "    }" +
                "    .button {" +
                "      display: inline-block;" +
                "      background-color: #1e88e5;" +
                "      color: white;" +
                "      padding: 10px 20px;" +
                "      text-decoration: none;" +
                "      border-radius: 4px;" +
                "      font-weight: bold;" +
                "      margin-top: 20px;" +
                "    }" +
                "    .email-footer {" +
                "      background-color: #f4f4f4;" +
                "      padding: 10px;" +
                "      text-align: center;" +
                "      font-size: 14px;" +
                "      color: #777777;" +
                "    }" +
                "  </style>" +
                "</head>" +
                "<body>" +
                "  <div class=\"email-container\">" +
                "    <div class=\"email-header\">" +
                "      CareerAce - OTP Verification" +
                "    </div>" +
                "    <div class=\"email-body\">" +
                "      <h2>Your OTP Code</h2>" +
                "      <p>Thank you for using CareerAce! Please use the following OTP to verify your identity:</p>" +
                "      <h3 style=\"font-size: 24px; font-weight: bold; color: #1e88e5;\">%s</h3>" + // OTP placeholder
                "      <p>Enter the code on the verification page to complete your process.</p>" +
                "    </div>" +
                "    <div class=\"email-footer\">" +
                "      <p>© 2024 CareerAce. All rights reserved.</p>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>", otp);

        try {

            rabbitTemplate.convertAndSend("otpQueue", new EmailDto(email, subject, body));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RabbitMQException("Error ending email using RabbitMQ");
        }
    }

}
