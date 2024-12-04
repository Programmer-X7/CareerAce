package com.bcet.auth_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.bcet.auth_service.dto.UserResponseDto;

@FeignClient(name = "USER-SERVICE", path = "/users")
public interface UserServiceClient {

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserResponseDto user);

    @GetMapping("/id/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable String userId);

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email);

    @GetMapping("/email-exists")
    public ResponseEntity<Boolean> isEmailExists(@RequestParam String email);
}
