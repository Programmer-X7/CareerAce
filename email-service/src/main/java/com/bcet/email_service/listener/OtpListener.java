package com.bcet.email_service.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bcet.email_service.dto.EmailDto;
import com.bcet.email_service.service.EmailSendService;

@Service
public class OtpListener {

    private final EmailSendService emailSendService;

    @Value("${rabbitmq.queue.otp:otpQueue}")
    private String otpQueue;

    public OtpListener(EmailSendService emailSendService) {
        this.emailSendService = emailSendService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.otp:otpQueue}")
    public void receiveOtpMessage(EmailDto message) {
        String email = message.getEmail();
        String subject = message.getSubject();
        String content = message.getBody();

        emailSendService.sendEmail(email, subject, content);
    }
}
