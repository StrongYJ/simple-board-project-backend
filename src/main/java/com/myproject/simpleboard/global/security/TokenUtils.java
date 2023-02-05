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

@Component
@Slf4j
public class TokenUtils {
    
    private final Key key;

    public TokenUtils() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String createToken(Long memberId, MemberRole role) {
        return Jwts.builder()
            .setSubject("board_project")
            .claim("id", memberId)
            .claim("role", role)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + JwtProperties.TOKEN_EXPIRE_TIME))
            .signWith(key)
            .compact();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        Collection<GrantedAuthority> authority = new ArrayList<>();
        String role = claims.get("role").toString();
        log.info("[TokenUtils getAuthentication] role = {}", role);
        authority.add(new SimpleGrantedAuthority(role));
        return new UsernamePasswordAuthenticationToken(claims.get("id"), null, authority);
    }

    public boolean verify(String token) {
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        return true;
    }

    public String getToken(Cookie[] cookies) {
        return Arrays.stream(cookies).filter(c -> c.getName().equals(JwtProperties.JWT_NANE)&&c.isHttpOnly()).findAny().orElseThrow().getValue();
    }

    public Long extractMemberId(String token) {
        return parseClaims(token).get("id", Long.class);
    }

    private Claims parseClaims(String accessToken) {
        return Jwts.parserBuilder().setSigningKey(key).build()
            .parseClaimsJws(accessToken)
            .getBody();
    }
}
