package com.myproject.simpleboard.domain.member;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.simpleboard.domain.member.dto.JoinDto;
import com.myproject.simpleboard.domain.member.dto.JoinResponseDtd;
import com.myproject.simpleboard.domain.member.dto.LoginDto;
import com.myproject.simpleboard.domain.member.dto.LoginResponseDto;

@RestController
public class MemberController {

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto) {
        
        return new ResponseEntity<LoginResponseDto>(new LoginResponseDto(loginDto.username(), "logined"), HttpStatus.OK);
    }

    @PostMapping("/join")
    public ResponseEntity<JoinResponseDtd> join(@RequestBody @Validated JoinDto joinDto) {
        
        return new ResponseEntity<JoinResponseDtd>(new JoinResponseDtd(joinDto.username(), "joined", true), HttpStatus.OK);
    }
}
