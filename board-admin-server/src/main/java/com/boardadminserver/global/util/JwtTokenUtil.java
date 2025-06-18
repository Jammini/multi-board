package com.boardadminserver.global.util;

import com.boardadminserver.global.config.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("jwt")
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    /**
     * JWT 토큰에서 이메일을 추출
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject); // subject를 이메일로 사용
    }

    /**
     * JWT 토큰에서 만료 시간을 추출
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * JWT 토큰에서 특정 클레임을 추출
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * JWT 토큰에서 모든 클레임을 추출
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                   .build().parseSignedClaims(token).getPayload();
    }

    /**
     * JWT 토큰이 만료되었는지 확인
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 사용자 정보를 기반으로 JWT 토큰 생성
     */
    public String generateToken(UserPrincipal userPrincipal) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("adminId", userPrincipal.getId());
        claims.put("name", userPrincipal.getName());
        return createToken(claims, userPrincipal.getUsername());
    }

    /**
     * 클레임과 주제를 기반으로 JWT 토큰 생성
     */
    private String createToken(Map<String, Object> claims, String email) {
        // 30분
        long accessTokenValidity = 1000 * 60 * 30;
        return Jwts.builder()
                   .claims(claims)
                   .subject(email)
                   .issuedAt(new Date(System.currentTimeMillis()))
                   .expiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                   .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                   .compact();
    }

    /**
     * JWT 토큰이 유효성 검증
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
