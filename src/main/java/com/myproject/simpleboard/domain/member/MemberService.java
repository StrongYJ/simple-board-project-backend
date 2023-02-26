package com.myproject.simpleboard.domain.member;

import com.myproject.simpleboard.domain.member.dto.*;
import com.myproject.simpleboard.domain.member.entity.Member;
import com.myproject.simpleboard.domain.member.entity.model.MemberRole;
import com.myproject.simpleboard.domain.member.entity.model.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    
    private final MemberRepository memberRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public MemberDto getMemberById(long id) {
        return new MemberDto(memberRepo.findById(id).orElseThrow());
    }

    @Transactional(readOnly = true)
    public Page<MemberDto> getAllMembers(Pageable pageable) {
        return memberRepo.findAll(pageable).map(MemberDto::new);
    }

    public MemberDto join(MemberJoinDto memberJoinDto) {
        if(memberRepo.existsByUsername(memberJoinDto.username()))
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");

        Member newMember = Member.builder()
                .username(memberJoinDto.username())
                .pwd(passwordEncoder.encode(memberJoinDto.password()))
                .role(MemberRole.USER)
                .build();
        memberRepo.save(newMember);
        return new MemberDto(newMember);
    }

    public MemberDto login(LoginDto loginDto) {
        Member member = memberRepo.findMemberByUsername(loginDto.username()).orElseThrow(() ->
                new NoSuchElementException("아이디나 비밀번호가 잘못되었습니다."));
        if(!passwordEncoder.matches(loginDto.password(), member.getPwd()))
            throw new NoSuchElementException("아이디나 비밀번호가 잘못되었습니다.");
        checkNormalMember(member);

        return new MemberDto(member);
    }


    public MemberDto modify(Long id, MemberUpdateDto memberUpdateDto) {
        Member member = memberRepo.findById(id).orElseThrow();
        if(StringUtils.hasText(memberUpdateDto.username()) &&
                memberRepo.existsByUsername(memberUpdateDto.username())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        member.updateUsernameOrPwd(memberUpdateDto.username(),
                StringUtils.hasText(memberUpdateDto.pwd()) ? passwordEncoder.encode(memberUpdateDto.pwd()) : null);

        return new MemberDto(member);
    }

    public withdrawalResponse withdrawal(long id) {
        Member member = memberRepo.findById(id).orElseThrow();
        memberRepo.delete(member);
        return new withdrawalResponse(member.getUsername(), LocalDate.now());
    }

    public ChangeStatusResponse changeStatus(long id, MemberPunishDto punish) {
        Member member = memberRepo.findById(id).orElseThrow();

        if(punish.status() == MemberStatus.PAUSE) {
            member.paused(punish.reason(), LocalDate.now().plusDays(punish.days()));
        } else if(punish.status() == MemberStatus.SUSPEND) {
            member.suspended(punish.reason());
        } else {
            throw new IllegalArgumentException(punish.status() + "은(는) 잘못된 요청입니다.");
        }

        return new ChangeStatusResponse(member.getUsername(), punish.status().getTitle() + "되었습니다.");
    }

    private void checkNormalMember(Member member) {
        if(member.getStatus() != MemberStatus.NORMAL)
            throw new DisabledException("정지된 계정입니다.");
    }

}
