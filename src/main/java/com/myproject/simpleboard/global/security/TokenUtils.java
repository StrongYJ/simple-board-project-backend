package com.myproject.simpleboard.global.security;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.myproject.simpleboard.domain.member.entity.model.MemberRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenUtils {
    
    private final Key key;

    public TokenUtils() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String createAccessToken(Long memberId, MemberRole role) {
        return JwtProperties.ACCESS_PREFIX + Jwts.builder()
            .setSubject("board_project_access")
            .claim("id", memberId)
            .claim("role", role.name())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRE_TIME))
            .signWith(key)
            .compact();
    }
    public String createRefreshToken() {
        return Jwts.builder()
                .setSubject("board_project_refresh")
                .claim("refresh-token", "refresh-token")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRE_TIME))
                .signWith(key)
                .compact();
    }

    public Authentication getAuthentication(final String accessToken) {
        Claims claims = parseClaims(accessToken);
        final Long memberId = claims.get("id", Long.class);
        final String role = claims.get("role", String.class);
        AuthMember authMember = new AuthMember(memberId, MemberRole.valueOf(role));
        return new UsernamePasswordAuthenticationToken(authMember, null, authMember.getAuthorities());
    }

    public void verifyAcess(final String token) {
        Jwts.parserBuilder().requireSubject("board_project_access").setSigningKey(key).build().parseClaimsJws(token);
    }

    public void verifyRefresh(final String token) {
        Jwts.parserBuilder().requireSubject("board_project_refresh").setSigningKey(key).build().parseClaimsJws(token);
    }

    public String accessTokenResolve(String accessToken) {
        return accessToken.replace(JwtProperties.ACCESS_PREFIX, "");
    }
    public String getRefreshToken(Cookie[] cookies) {
        return Arrays.stream(cookies).filter(c -> c.getName().equals(JwtProperties.REFRESH_NANE)&&c.isHttpOnly()&&c.getSecure()).findAny().orElseThrow().getValue();
    }

    private Claims parseClaims(final String accessToken) {
        return Jwts.parserBuilder().setSigningKey(key).build()
            .parseClaimsJws(accessToken)
            .getBody();
    }
}
