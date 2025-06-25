package com.boardadminserver.global.config.security.filter;

import static com.boardadminserver.global.util.AuthorizationConstants.BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.boardadminserver.admin.application.AdminUserDetailsService;
import com.boardadminserver.global.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final AdminUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws IOException, ServletException {
        String jwtHeader = request.getHeader(AUTHORIZATION);

        if (jwtHeader == null || !jwtHeader.startsWith(BEARER)) {
            chain.doFilter(request, response);
            return;
        }
        // JWT 토큰을 검증해서 정상적인 사용자인지 확인
        String jwtToken = jwtHeader.substring(BEARER.length() + 1);
        String email = jwtTokenUtil.extractEmail(jwtToken);

        // 서명이 정상적으로 됨
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 관리자 아이디를 이용해 사용자 정보 로드
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // JWT 토큰이 유효한지 검증
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
