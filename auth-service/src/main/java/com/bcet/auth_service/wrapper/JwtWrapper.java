package com.bcet.auth_service.wrapper;

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
public class JwtWrapper {
    
    private String userId;
    private String email;
    private String name;
    private String picture;
    private String token;

}
