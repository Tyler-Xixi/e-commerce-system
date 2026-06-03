package com.ecommerce.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class JwtPrincipal {
    private final Long userId;
    private final String username;
    private final String roleCode;
    private final List<GrantedAuthority> authorities;

    public JwtPrincipal(Long userId, String username, String roleCode, Set<String> permissions) {
        this.userId = userId;
        this.username = username;
        this.roleCode = roleCode;
        this.authorities = new ArrayList<>();
        if (roleCode != null && !roleCode.isBlank()) {
            this.authorities.add(new SimpleGrantedAuthority("ROLE_" + roleCode));
        }
        if (permissions != null) {
            permissions.stream()
                    .filter(permission -> permission != null && !permission.isBlank())
                    .map(SimpleGrantedAuthority::new)
                    .forEach(this.authorities::add);
        }
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
