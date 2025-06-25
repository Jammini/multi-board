package com.boardadminserver.admin.application;

import com.boardadminserver.admin.application.response.AdminResponse;
import com.boardadminserver.global.config.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {
    private final AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AdminResponse admin = adminService.findAdmin(email);
        return new UserPrincipal(admin);
    }
}
