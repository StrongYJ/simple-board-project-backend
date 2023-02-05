package com.myproject.simpleboard.domain.member;

import com.myproject.simpleboard.domain.member.dto.*;
import com.myproject.simpleboard.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.myproject.simpleboard.global.security.JwtProperties;
import com.myproject.simpleboard.global.security.LoginMemberId;
import com.myproject.simpleboard.global.security.TokenUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<MemberBasicDto> info(@LoginMemberId long id) {
        return new ResponseEntity<>(new MemberBasicDto(memberService.getMemberById(id)), HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        Member loginMember = memberService.login(loginDto);
        Cookie cookie = new Cookie(JwtProperties.JWT_NANE, tokenUtils.createToken(loginMember.getId(), loginMember.getRole()));
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return new ResponseEntity<LoginResponseDto>(new LoginResponseDto(loginMember.getUsername(), "logined"), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<MemberJoinResponse> join(@RequestBody @Validated MemberJoinDto memberJoinDto) {
        Member member = memberService.join(memberJoinDto);
        return new ResponseEntity<MemberJoinResponse>(new MemberJoinResponse(member.getUsername(), "joined", true), HttpStatus.CREATED);
    }

    @PatchMapping("")
    public ResponseEntity<LoginResponseDto> modify(
            @LoginMemberId long id,
            @RequestBody @Validated MemberUpdateDto memberUpdateDto) {
        Member modifiedMember = memberService.modify(id, memberUpdateDto);

        return new ResponseEntity<>(new LoginResponseDto(modifiedMember.getUsername(), "modified") , HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<withdrawalResponse> withdrawal(@LoginMemberId long id) {
        withdrawalResponse response = memberService.withdrawal(id);

        return new ResponseEntity<>(memberService.withdrawal(id), HttpStatus.OK);
    }

    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<MemberDetailDto> getMemberDetailInfo(@PathVariable("id") long id) {
        return new ResponseEntity<>(new MemberDetailDto(memberService.getMemberById(id)), HttpStatus.OK);
    }

    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    @PostMapping("/{id}")
    public ResponseEntity<ChangeStatusResponse> postMemberPunishment(
            @PathVariable("id") long id,
            @RequestBody @Validated MemberPunishDto punish
    ) {
        ChangeStatusResponse response =  memberService.changeStatus(id, punish);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    @GetMapping("/all")
    public ResponseEntity<Page<MemberDetailDto>> getMembers(Pageable pageable) {
        return new ResponseEntity<>(memberService.getAllMembers(pageable).map(MemberDetailDto::new), HttpStatus.OK);
    }

    @PostMapping("/test")
    public String test(@LoginMemberId Long memberId) {

        return "[유효한 토큰] memberId: " + memberId;
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
}
