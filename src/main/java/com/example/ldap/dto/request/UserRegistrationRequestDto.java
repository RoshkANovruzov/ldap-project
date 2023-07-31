package com.example.ldap.dto.request;

import lombok.Data;

@Data
public class UserRegistrationRequestDto {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String groupName;
    private String role;
}
