package com.example.jwt_security.payload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
public class UserPermissionDto {
    private String role;
    private Set<String> permissions;

    public Set<SimpleGrantedAuthority> getUserPermissions() {
        Set<SimpleGrantedAuthority> simpleGrantedAuthority = new HashSet<>();
        simpleGrantedAuthority.add(new SimpleGrantedAuthority("ROLE_" + role));
        simpleGrantedAuthority.addAll(
                permissions
                        .stream()
                        .map((per) -> new SimpleGrantedAuthority(role + "::" + per))
                        .collect(Collectors.toSet()));
        return simpleGrantedAuthority;
    }


}
