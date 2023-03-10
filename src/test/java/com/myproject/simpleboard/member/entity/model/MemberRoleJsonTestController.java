package com.myproject.simpleboard.member.entity.model;

import com.myproject.simpleboard.domain.member.dto.MemberDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class MemberRoleJsonTestController {
    @PostMapping("/test/member-role")
    ResponseEntity<MemberDto> postTest(@RequestBody MemberDto member) {
        return new ResponseEntity<>(member, HttpStatus.OK);
    }
}
