package com.example.jwt_security.entity.user;

import com.example.jwt_security.payload.UserPermissionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.web.servlet.filter.ApplicationContextHeaderFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.persistence.*;
import javax.servlet.ServletContext;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @NotNull
    @Column(unique = true)
    String username;

    String password;

    private String permissions;


    @SneakyThrows
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> userPermissions = new HashSet<>();
        ObjectMapper objectMapper = Objects
                .requireNonNull(ContextLoader.getCurrentWebApplicationContext().getBean(ObjectMapper.class));
        UserPermissionDto[] permissionDtos = objectMapper.readValue(permissions, UserPermissionDto[].class);
        for (var permissionDto : permissionDtos) {
            userPermissions.addAll(permissionDto.getUserPermissions());
        }

        return userPermissions;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
