package com.myproject.simpleboard.api.member;

import com.myproject.simpleboard.ApiTest;
import com.myproject.simpleboard.domain.member.MemberRepository;
import com.myproject.simpleboard.domain.member.dto.LoginDto;
import com.myproject.simpleboard.domain.member.dto.MemberJoinDto;
import com.myproject.simpleboard.domain.member.dto.MemberUpdateDto;
import com.myproject.simpleboard.domain.member.entity.Member;
import com.myproject.simpleboard.domain.member.entity.model.MemberRole;
import com.myproject.simpleboard.domain.member.entity.model.MemberStatus;
import com.myproject.simpleboard.global.security.JwtProperties;
import com.myproject.simpleboard.global.security.TokenUtils;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class MemberControllerTest extends ApiTest {

    @Autowired private TokenUtils tokenUtils;
    @Autowired private MemberRepository memberRepo;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final static Member oldMember = new Member("oldMember", "oldMemberPwd", MemberRole.USER, MemberStatus.NORMAL);
    private Long oldMemberId;
    private String oldMemberAccessToken;
    @BeforeEach
    void init() {
        Member member = new Member(oldMember.getUsername(), bCryptPasswordEncoder.encode(oldMember.getPwd()), oldMember.getRole(), oldMember.getStatus());
        memberRepo.save(member);
        oldMemberId = member.getId();
        oldMemberAccessToken = tokenUtils.createAccessToken(member.getId(), oldMember.getRole());
    }

    @Test
    void 회원가입() {
        MemberJoinDto join = new MemberJoinDto("test1", "test1pwd");
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(join)
        .when()
                .post("/members")
        .then().log().all() // String username, String message, boolean success
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .statusCode(HttpStatus.CREATED.value())
                .body("username", equalTo(join.username()))
                .body("message", equalTo("Joined"))
                .body("success", equalTo(true));
    }

    @Test
    void 회원가입_중복이름() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new MemberJoinDto(oldMember.getUsername(), UUID.randomUUID().toString().substring(0, 16)))
        .when()
                .post("/members")
        .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("이미 존재하는 아이디입니다."));
    }

    @Test
    void 로그인() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LoginDto(oldMember.getUsername(), oldMember.getPwd()))
        .when()
                .post("/members/login")
        .then().log().all()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value())
                .body("username", equalTo(oldMember.getUsername()))
                .body("message", equalTo("logined"))
                .header(HttpHeaders.AUTHORIZATION, notNullValue())
                .cookie(JwtProperties.REFRESH_NANE, notNullValue());

    }

    @Test
    void 로그인_틀린비밀번호() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LoginDto(oldMember.getUsername(), "wrongpwd"))
                .when()
                .post("/members/login")
                .then().log().all()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("아이디나 비밀번호가 잘못되었습니다."));
    }


    @Test
    void 로그인_없는_아이디() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LoginDto("nonexistusername", oldMember.getPwd()))
                .when()
                .post("/members/login")
                .then().log().all()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("아이디나 비밀번호가 잘못되었습니다."));
    }

    @Test
    void 로그인_의미없는_정보() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LoginDto("nonexistusername","gfdmngslgkf"))
                .when()
                .post("/members/login")
                .then().log().all()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("아이디나 비밀번호가 잘못되었습니다."));
    }

    @Test
    void 이름만수정() {
        final String changeName = "oldMemUpdate";

        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, oldMemberAccessToken)
                .body(new MemberUpdateDto(changeName, null))
                .when()
                .patch("/members/" + oldMemberId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("username", equalTo(changeName))
                .body("message", equalTo("modified"));

        Member updatedOldMember = memberRepo.findById(oldMemberId).get();
        Assertions.assertThat(updatedOldMember.getUsername()).isEqualTo(changeName);
        Assertions.assertThat(bCryptPasswordEncoder.matches(oldMember.getPwd(), updatedOldMember.getPwd())).isTrue();

    }

    @Test
    void 비밀번호만수정() {
        final String changePwd = "oldMemUpdate";

        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, oldMemberAccessToken)
                .body(new MemberUpdateDto(null, changePwd))
                .when()
                .patch("/members/" + oldMemberId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("username", equalTo(oldMember.getUsername()))
                .body("message", equalTo("modified"));

        Member updatedOldMember = memberRepo.findById(oldMemberId).get();

        Assertions.assertThat(updatedOldMember.getUsername()).isEqualTo(oldMember.getUsername());
        Assertions.assertThat(bCryptPasswordEncoder.matches(changePwd, updatedOldMember.getPwd())).isTrue();
    }

    @Test
    void 이름비밀번호둘다수정() {
        final String changeName = "oldMemUpdateName";
        final String changePwd = "oldMemUpdatePwd";

        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, oldMemberAccessToken)
                .body(new MemberUpdateDto(changeName, changePwd))
                .when()
                .patch("/members/" + oldMemberId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("username", equalTo(changeName))
                .body("message", equalTo("modified"));

        Member updatedOldMember = memberRepo.findById(oldMemberId).get();

        Assertions.assertThat(updatedOldMember.getUsername()).isEqualTo(changeName);
        Assertions.assertThat(bCryptPasswordEncoder.matches(changePwd, updatedOldMember.getPwd())).isTrue();
    }

    @Test
    @Order(Integer.MAX_VALUE)
    void 탈퇴() {
        given()
                .header(HttpHeaders.AUTHORIZATION, oldMemberAccessToken)
                .when()
                .delete("/members/" + oldMemberId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("username", equalTo(oldMember.getUsername()))
                .body("deleteDate", equalTo(LocalDate.now().toString()));

        Assertions.assertThat(memberRepo.findById(oldMemberId).isEmpty()).isTrue();
    }
}
