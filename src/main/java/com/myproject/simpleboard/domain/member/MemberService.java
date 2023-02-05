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

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    
    private final MemberRepository memberRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Member getMemberById(long id) {
        return memberRepo.findById(id).orElseThrow();
    }

    @Transactional(readOnly = true)
    public Page<Member> getAllMembers(Pageable pageable) {
        return memberRepo.findAll(pageable);
    }
    public Member join(MemberJoinDto memberJoinDto) {
        if(memberRepo.existsByUsername(memberJoinDto.username()))
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");

        Member newMember = new Member(
                memberJoinDto.username(), passwordEncoder.encode(memberJoinDto.password()),
                MemberRole.USER, MemberStatus.NORMAL
            );
        memberRepo.save(newMember);
        return newMember;
    }

    public Member login(LoginDto loginDto) {
        Member member = memberRepo.findMemberByUsername(loginDto.username()).orElseThrow(() ->
                new NoSuchElementException("아이디나 비밀번호가 잘못되었습니다."));
        if(!passwordEncoder.matches(loginDto.password(), member.getPwd()))
            throw new NoSuchElementException("아이디나 비밀번호가 잘못되었습니다.");
        checkNormalMember(member);

        return member;
    }


    public Member modify(Long id, MemberUpdateDto memberUpdateDto) {
        Member member = memberRepo.findById(id).orElseThrow();
        if(memberUpdateDto.username() != null) {
            if(memberRepo.existsByUsername(memberUpdateDto.username()))
                throw new IllegalArgumentException("이미 존재하는 아이디입니다.");

            member.modifyUsername(memberUpdateDto.username());
        }
        if(memberUpdateDto.pwd() != null) {
            member.modifyPwd(passwordEncoder.encode(memberUpdateDto.pwd()));
        }

        return member;
    }

    public withdrawalResponse withdrawal(long id) {
        Member member = memberRepo.findById(id).orElseThrow();
        member.modifyStatus(MemberStatus.WITHDRAWAL);
        return new withdrawalResponse(member.getUsername(), member.getStatus().toString());
    }

    private void checkNormalMember(Member member) {
        if(member.getStatus() != MemberStatus.NORMAL)
            throw new DisabledException("탈퇴했거나 정지된 계정입니다.");
    }

    public ChangeStatusResponse changeStatus(long id, MemberPunishDto punish) {
        Member member = memberRepo.findById(id).orElseThrow();
        if(member.getStatus() == MemberStatus.WITHDRAWAL)
            throw new DisabledException("탈퇴한 계정입니다.");

        MemberStatus changeMemberStatus = MemberStatus.valueOf(punish.status().toUpperCase());
        member.modifyStatus(changeMemberStatus);
        member.setMemberStatusDetail(changeMemberStatus, punish.reason());

        return new ChangeStatusResponse(member.getUsername(), "정지되었습니다.", changeMemberStatus.getTitle());
    }
}
