package com.myproject.simpleboard.global.security;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.myproject.simpleboard.domain.member.domain.MemberRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TokenUtils {
    
    private final Key accessKey;
    private final Key refreshKey;

    public TokenUtils() {
        this.accessKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.refreshKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String createAccessToken(Long memberId, MemberRole role) {
        return Jwts.builder()
            .setSubject("board_project")
            .claim("id", memberId)
            .claim("role", role)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_TOKEN_EXPIRE_TIME))
            .signWith(accessKey)
            .compact();
    }

    public String createRefreshToken(Long memberId) {
        return Jwts.builder()
            .setSubject("board_project")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_TOKEN_EXPIRE_TIME))
            .signWith(refreshKey)
            .compact();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        Collection<GrantedAuthority> authority = new ArrayList<>();
        String role = claims.get("role").toString();
        log.info("[TokenProvider getAuthentication] role = {}", role);
        authority.add(new SimpleGrantedAuthority(role));
        return new UsernamePasswordAuthenticationToken(claims.get("id"), null, authority);
    }

    public TokenStatus verifyAccessToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(token);
            return TokenStatus.VALID;
        } catch (ExpiredJwtException e) {
            return TokenStatus.EXPIRED;
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            return TokenStatus.DENIED;
        }
    }

    public TokenStatus verifyRefreshToken(String token) {
        Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(token);
        return TokenStatus.VALID;
    }

    private Claims parseClaims(String accessToken) {
        return Jwts.parserBuilder().setSigningKey(accessKey).build()
            .parseClaimsJws(accessToken)
            .getBody();
    }
}
