package com.myproject.simpleboard.domain.member;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.simpleboard.domain.member.domain.MemberRole;
import com.myproject.simpleboard.domain.member.dto.JoinDto;
import com.myproject.simpleboard.domain.member.dto.JoinResponseDtd;
import com.myproject.simpleboard.domain.member.dto.LoginDto;
import com.myproject.simpleboard.domain.member.dto.LoginResponseDto;
import com.myproject.simpleboard.global.security.JwtProperties;
import com.myproject.simpleboard.global.security.LoginMemberId;
import com.myproject.simpleboard.global.security.TokenUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final TokenUtils tokenUtils;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        Cookie cookie = new Cookie(JwtProperties.JWT_NANE, tokenUtils.createToken(1L, MemberRole.USER));
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return new ResponseEntity<LoginResponseDto>(new LoginResponseDto(loginDto.username(), "logined"), HttpStatus.OK);
    }

    @PostMapping("/join")
    public ResponseEntity<JoinResponseDtd> join(@RequestBody @Validated JoinDto joinDto) {
        
        return new ResponseEntity<JoinResponseDtd>(new JoinResponseDtd(joinDto.username(), "joined", true), HttpStatus.OK);
    }

    @PostMapping("/test")
    public String test(@LoginMemberId Long memberId) {

        return "[유효한 토큰] memberId: " + memberId;
    }
}
