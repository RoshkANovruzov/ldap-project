package com.example.ldap.worker;

import com.example.ldap.dto.BaseResponse;
import com.example.ldap.model.LdapUser;
import lombok.RequiredArgsConstructor;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserWorker {
    private final LdapTemplate ldapTemplate;

    public List<LdapUser> searchByUSername(String username) {
        String searchBase = "ou=people,dc=springframework,dc=org";
        String filter = "uid=" + username;

        List<LdapUser> results = ldapTemplate.search(searchBase, filter, (AttributesMapper<LdapUser>) attrs -> {
            LdapUser ldapUser = new LdapUser();
            ldapUser.setRole((String) attrs.get("description").get());
            ldapUser.setUsername(username);
            ldapUser.setPassword((String) attrs.get("userPassword").get().toString());
            ldapUser.setEmail((String) attrs.get("emailAddress").get());
            ldapUser.setLastName((String) attrs.get("sn").get());
            ldapUser.setFirstName((String) attrs.get("cn").get());
            return ldapUser;
        });

        return results;
    }
}
