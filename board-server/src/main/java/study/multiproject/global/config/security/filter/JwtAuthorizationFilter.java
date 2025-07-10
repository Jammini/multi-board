package study.multiproject.global.config.security.filter;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static study.multiproject.global.util.AuthorizationConstants.BEARER;

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
import study.multiproject.global.config.security.CustomUserDetailsService;
import study.multiproject.global.util.JwtTokenUtil;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final String INTERNAL_HEADER = "X-Internal-Secret";
    private static final String INTERNAL_SECRET = "multiproject-secret";
    private static final String INTERNAL_PREFIX = "/internal/";
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws IOException, ServletException {
        if (isInternalRequest(request)) {
            if (authenticateInternalRequest(request, response)) {
                chain.doFilter(request, response);
            }
            return;
        }

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
            // 이메일을 이용해 사용자 정보 로드
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // JWT 토큰이 유효한지 검증
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isInternalRequest(HttpServletRequest request) {
        return request.getRequestURI().startsWith(INTERNAL_PREFIX);
    }

    private boolean authenticateInternalRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String internalSecret = request.getHeader(INTERNAL_HEADER);

        if (INTERNAL_SECRET.equals(internalSecret)) {
            return true;
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid internal secret");
            return false;
        }
    }
}
