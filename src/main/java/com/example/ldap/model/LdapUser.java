package com.example.ldap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entry(objectClasses = {"inetOrgPerson","top","person","organizationalPerson","naturalPerson"})
public class LdapUser {

    @Id
    private Name dn;

    @Attribute(name = "uid")
    private String username;

    @Attribute(name = "cn")
    private String firstName;

    @Attribute(name = "sn")
    private String lastName;

    @Attribute(name = "emailAddress")
    private String email;

    @Attribute(name = "userPassword")
    private String password;

    @Attribute(name = "description")
    private String role;

}
