package com.example.ldap.controller;

import com.example.ldap.dto.BaseResponse;
import com.example.ldap.dto.request.UserLoginRequestDto;
import com.example.ldap.dto.request.UserPasswordUpdateRequestDto;
import com.example.ldap.dto.request.UserRegistrationRequestDto;
import com.example.ldap.dto.request.UserUpdateRequestDto;
import com.example.ldap.model.LdapUser;
import com.example.ldap.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {

    private final UserService userService;

    @GetMapping
    public BaseResponse<Void> test(){
        return BaseResponse.success("SUCCESS");
    }



    @PostMapping
    public BaseResponse<Void> create(@RequestBody UserRegistrationRequestDto userRegistrationRequestDto) {
        return userService.create(userRegistrationRequestDto);
    }

    @GetMapping("/{username}")
    public BaseResponse<List<LdapUser>> searchUserByUsername(@PathVariable String username) {
        return userService.searchByUSername(username);
    }

    @PatchMapping("/{uid}")
    public BaseResponse<Void> updateUser(@PathVariable String uid, @RequestBody UserUpdateRequestDto userUpdateRequestDto){
        return userService.update(uid,userUpdateRequestDto);
    }

    @DeleteMapping("/{uid}")
    public BaseResponse<Void> deleteUser(@PathVariable String uid){
        return userService.deleteUser(uid);
    }

    @PatchMapping
    public BaseResponse<Void> changePassword(@RequestBody UserPasswordUpdateRequestDto userPasswordUpdateRequestDto){
        return userService.ChangePassword(userPasswordUpdateRequestDto);
    }

}