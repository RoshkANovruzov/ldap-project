package com.example.ldap.service;

import com.example.ldap.dto.BaseResponse;
import com.example.ldap.dto.request.UserLoginRequestDto;
import com.example.ldap.dto.request.UserPasswordUpdateRequestDto;
import com.example.ldap.dto.request.UserRegistrationRequestDto;
import com.example.ldap.dto.request.UserUpdateRequestDto;
import com.example.ldap.model.LdapUser;
import com.example.ldap.worker.UserWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final LdapTemplate ldapTemplate;
    private final PasswordEncoder passwordEncoder;
    private final UserWorker userWorker;

    public BaseResponse<Void> create(UserRegistrationRequestDto userRegistrationRequestDto) {
        Name dn = LdapNameBuilder
                .newInstance()
                .add("dc", "org")
                .add("dc", "springframework")
                .add("ou", userRegistrationRequestDto.getGroupName())
                .add("uid", userRegistrationRequestDto.getUsername())
                .build();
        DirContextAdapter context = new DirContextAdapter(dn);

        context.setAttributeValues(
                "objectclass",
                new String[]
                        {"top",
                                "person",
                                "organizationalPerson",
                                "inetOrgPerson",
                                "naturalPerson"});
        context.setAttributeValue("emailAddress", userRegistrationRequestDto.getEmail());
        context.setAttributeValue("description", userRegistrationRequestDto.getRole());
        context.setAttributeValue("cn", userRegistrationRequestDto.getFirstName());
        context.setAttributeValue("sn", userRegistrationRequestDto.getLastName());
        context.setAttributeValue
                ("userPassword", passwordEncoder.encode(userRegistrationRequestDto.getPassword()));

        ldapTemplate.bind(context);
        return BaseResponse.success("SUCCESS");
    }


    public BaseResponse<List<LdapUser>> searchByUSername(String username) {
        return BaseResponse.success(userWorker.searchByUSername(username),"SUCCESS");
    }

    public BaseResponse<Void> update(String uid, UserUpdateRequestDto userUpdateRequestDto) {
        Name dn = LdapNameBuilder
                .newInstance()
                .add("dc", "org")
                .add("dc", "springframework")
                .add("ou", userUpdateRequestDto.getGroupName())
                .add("uid", uid)
                .build();
        DirContextOperations context = ldapTemplate.lookupContext(dn);


        context.setAttributeValues(
                "objectclass",
                new String[]
                        {"top",
                                "person",
                                "organizationalPerson",
                                "inetOrgPerson",
                                "naturalPerson"});
        context.setAttributeValue("emailAddress", userUpdateRequestDto.getEmail());
        context.setAttributeValue("description", userUpdateRequestDto.getRole());
        context.setAttributeValue("cn", userUpdateRequestDto.getFirstName());
        context.setAttributeValue("sn", userUpdateRequestDto.getLastName());
        context.setAttributeValue
                ("userPassword", passwordEncoder.encode(userUpdateRequestDto.getPassword()));

        ldapTemplate.modifyAttributes(context);
        return BaseResponse.success("SUCCESS");
    }

    public BaseResponse<Void> deleteUser(String uid) {
        LdapUser ldapUser = userWorker.searchByUSername(uid).get(0);
        Name dn = LdapNameBuilder
                .newInstance()
                .add("dc", "org")
                .add("dc", "springframework")
                .add("ou", "people")
                .add("uid", uid)
                .build();
        ldapUser.setDn(dn);
        ldapTemplate.delete(ldapUser);
        return BaseResponse.success("SUCCESS");
    }

    public BaseResponse<Void> ChangePassword(UserPasswordUpdateRequestDto userPasswordUpdateRequestDto) {
        Name dn = LdapNameBuilder
                .newInstance()
                .add("dc", "org")
                .add("dc", "springframework")
                .add("ou", userPasswordUpdateRequestDto.getGroupName())
                .add("uid", userPasswordUpdateRequestDto.getUsername())
                .build();
        DirContextOperations context = ldapTemplate.lookupContext(dn);


        context.setAttributeValues(
                "objectclass",
                new String[]
                        {"top",
                                "person",
                                "organizationalPerson",
                                "inetOrgPerson",
                                "naturalPerson"});
        context.setAttributeValue
                ("userPassword", passwordEncoder.encode(userPasswordUpdateRequestDto.getPassword()));

        ldapTemplate.modifyAttributes(context);
        return BaseResponse.success("SUCCESS");
    }

}