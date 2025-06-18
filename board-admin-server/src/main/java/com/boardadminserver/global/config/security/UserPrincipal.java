package com.boardadminserver.global.config.security;

import com.boardadminserver.admin.service.response.AdminResponse;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class UserPrincipal extends User {

    private final Long id;
    private final String name;

    public UserPrincipal(AdminResponse admin) {
        super(admin.email(), admin.password(), List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        this.id = admin.id();
        this.name = admin.name();
    }
}
