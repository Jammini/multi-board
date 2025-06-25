package com.boardadminserver.admin.application;

import com.boardadminserver.admin.dao.AdminRepository;
import com.boardadminserver.admin.domain.Admin;
import com.boardadminserver.admin.exception.AdminNotFoundException;
import com.boardadminserver.admin.application.response.AdminResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminResponse findAdmin(String email) {
        Admin admin = adminRepository.findByEmail(email)
                          .orElseThrow(() -> new AdminNotFoundException("관리자를 찾을 수 없습니다."));
        return new AdminResponse(admin.getId(), admin.getEmail(), admin.getPassword(),
            admin.getName());
    }
}
