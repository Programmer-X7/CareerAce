package com.bcet.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailSignupRequestDto {

    private String email;
    private String name;
    private String phoneNumber;
    private String country;
    private String role;
    private final String provider = "Email";

    @Override
    public String toString() {
        return String.join("::",
                email != null ? email : "",
                name != null ? name : "",
                phoneNumber != null ? phoneNumber : "",
                country != null ? country : "",
                role != null ? role : "",
                provider);
    }

    public static EmailSignupRequestDto toDto(String str) {
        if (str == null || str.trim().isEmpty()) {
            return new EmailSignupRequestDto();
        }

        String[] parts = str.split("::", -1);
        return new EmailSignupRequestDto(
                parts.length > 0 ? (parts[0].isEmpty() ? null : parts[0]) : null,
                parts.length > 1 ? (parts[1].isEmpty() ? null : parts[1]) : null,
                parts.length > 2 ? (parts[2].isEmpty() ? null : parts[2]) : null,
                parts.length > 3 ? (parts[3].isEmpty() ? null : parts[3]) : null,
                parts.length > 4 ? (parts[4].isEmpty() ? null : parts[4]) : null);
    }

}
