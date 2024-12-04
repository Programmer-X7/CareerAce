package com.bcet.auth_service.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponseDto {
    private String userId;
    private String email;
    private String name;
    private String picture;
    private String phoneNumber;
    private String provider;
    private String country;
    private String role;
    private Boolean isActive;
    private Date createdAt;
    private Date updatedAt;
}
