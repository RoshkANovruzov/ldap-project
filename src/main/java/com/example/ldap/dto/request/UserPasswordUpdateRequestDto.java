package com.example.ldap.dto.request;

import lombok.Data;

@Data
public class UserPasswordUpdateRequestDto {
    private String username;
    private String password;
    private String groupName;
}
