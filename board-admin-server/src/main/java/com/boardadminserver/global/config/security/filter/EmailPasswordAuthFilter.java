package com.boardadminserver.global.config.security.filter;

import static com.boardadminserver.global.util.AuthorizationConstants.BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.boardadminserver.global.config.security.UserPrincipal;
import com.boardadminserver.global.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class EmailPasswordAuthFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;
    private final JwtTokenUtil jwtTokenUtil;

    public EmailPasswordAuthFilter(String loginUrl, ObjectMapper objectMapper, JwtTokenUtil jwtTokenUtil) {
        super(loginUrl);
        this.objectMapper = objectMapper;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {
        EmailPassword emailPassword = objectMapper.readValue(request.getInputStream(), EmailPassword.class);

        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
            emailPassword.email,
            emailPassword.password
        );

        token.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {
        UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();

        String jwtToken = jwtTokenUtil.generateToken(principal);
        response.addHeader(AUTHORIZATION, BEARER + " " + jwtToken);
    }

    record EmailPassword(
        String email,
        String password
    ) {

    }
}
