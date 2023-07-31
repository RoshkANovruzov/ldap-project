package com.example.ldap.provider;

import com.example.ldap.model.LdapUser;
import lombok.RequiredArgsConstructor;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LdapAuthenticationProvider implements AuthenticationProvider {

    private final LdapTemplate ldapTemplate;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        List<LdapUser> ldapUserList = searchUserByUsername(username);
        if (!ldapUserList.isEmpty()) {
            String role = ldapUserList.get(0).getRole();
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority(role));
            User user = new User(username,
                    ldapUserList.get(0).getPassword(),
                    roles
                    );
            return createSuccessfulAuth(authentication,user);
        }else {
            throw new UsernameNotFoundException(username+" not found");
        }
    }

    private Authentication createSuccessfulAuth(Authentication authentication, User user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user,
                user.getPassword(), user.getAuthorities());
        token.setDetails(authentication.getDetails());
        return token;
    }

    private List<LdapUser> searchUserByUsername(String username) {
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

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
