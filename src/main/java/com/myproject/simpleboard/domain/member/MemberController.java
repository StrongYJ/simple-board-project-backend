package com.myproject.simpleboard.domain.member;

import com.myproject.simpleboard.domain.member.dto.*;
import com.myproject.simpleboard.domain.member.entity.Member;
import com.myproject.simpleboard.global.security.LoginMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.myproject.simpleboard.global.security.JwtProperties;
import com.myproject.simpleboard.global.security.AuthMember;
import com.myproject.simpleboard.global.security.TokenUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {

    private final TokenUtils tokenUtils;
    private final MemberService memberService;

    @GetMapping("")
    public ResponseEntity<MemberBasicDto> info(@AuthMember LoginMember loginMember) {
        return new ResponseEntity<>(new MemberBasicDto(memberService.getMemberById(loginMember.id())), HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto) {
        MemberDetailDto loginMember = memberService.login(loginDto);
        ResponseCookie cookie = ResponseCookie.from(JwtProperties.REFRESH_NANE, tokenUtils.createRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(JwtProperties.REFRESH_EXPIRE_TIME / 1000)
                .build();
        String accessToken = tokenUtils.createAccessToken(loginMember.id(), loginMember.role());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .body(new LoginResponseDto(loginMember.username(), "logined"));
    }

    @PostMapping("")
    public ResponseEntity<MemberJoinResponse> join(@RequestBody @Validated MemberJoinDto memberJoinDto) {
        MemberDetailDto join = memberService.join(memberJoinDto);
        return new ResponseEntity<>(new MemberJoinResponse(join.username(), "Joined", true), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LoginResponseDto> modify(@PathVariable("id") Long memberId, @AuthMember LoginMember loginMember,
            @RequestBody @Validated MemberUpdateDto memberUpdateDto) {
        if(memberId != loginMember.id()) {
            throw new IllegalArgumentException("잘못된 요청 주소입니다.");
        }

        MemberDetailDto modifiedMember = memberService.modify(loginMember.id(), memberUpdateDto);

        return new ResponseEntity<>(new LoginResponseDto(modifiedMember.username(), "modified") , HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<withdrawalResponse> withdrawal(@PathVariable("id") Long memberId, @AuthMember LoginMember loginMember) {
        if(memberId != loginMember.id()) {
            throw new IllegalArgumentException("잘못된 요청 주소입니다.");
        }

        return new ResponseEntity<>(memberService.withdrawal(loginMember.id()), HttpStatus.OK);
    }

    @Secured({"MANAGER", "ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<MemberDetailDto> getMemberDetailInfo(@PathVariable("id") long id) {
        return new ResponseEntity<>(new MemberDetailDto(memberService.getMemberById(id)), HttpStatus.OK);
    }

    @Secured({"MANAGER", "ADMIN"})
    @PostMapping("/{id}")
    public ResponseEntity<ChangeStatusResponse> postMemberPunishment(
            @PathVariable("id") long id,
            @RequestBody @Validated MemberPunishDto punish
    ) {
        ChangeStatusResponse response =  memberService.changeStatus(id, punish);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Secured({"MANAGER", "ADMIN"})
    @GetMapping("/all")
    public ResponseEntity<Page<MemberDetailDto>> getMembers(Pageable pageable) {
        return new ResponseEntity<>(memberService.getAllMembers(pageable).map(MemberDetailDto::new), HttpStatus.OK);
    }


    @Secured("MANAGER")
    @GetMapping("/manager-test")
    public String managerTest() {
        return "manager";
    }
    @Secured("ADMIN")
    @GetMapping("/admin-test")
    public String adminTest() {
        return "admin";
    }
    @Secured({"ADMIN", "MANAGER"})
    @GetMapping("/admin-manager-test")
    public String adminManagerTest() {
        return "admin";
    }
    @PostMapping("/test")
    public String test(@AuthMember LoginMember loginMember) {

        return "[유효한 토큰] memberId: " + loginMember.id();
    }
}
