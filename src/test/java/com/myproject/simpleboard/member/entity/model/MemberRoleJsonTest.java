package com.myproject.simpleboard.member.entity.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.simpleboard.domain.member.dto.MemberDto;
import com.myproject.simpleboard.domain.member.entity.model.MemberRole;
import com.myproject.simpleboard.domain.member.entity.model.MemberStatus;
import com.myproject.simpleboard.domain.member.entity.model.PunishDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@AutoConfigureTestDatabase
public class MemberRoleJsonTest {

    @Autowired
    private MockMvc mockMvc;
    private final MemberDto memberDto = new MemberDto(1L, "test", MemberRole.USER, MemberStatus.NORMAL, new PunishDetail(null, null), LocalDateTime.now(), LocalDateTime.now());

    @Test
    void Enum타입_Json변환_테스트() throws Exception {

        mockMvc.perform(post("/test/member-role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(
                        new MyMemberDetailJson(
                                1L, "test", LocalDateTime.now().toString(), "USER", "NORMAL", LocalDateTime.now().toString()
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role.code", equalTo("USER")))
                .andDo(print());
    }

    private record  MyMemberDetailJson(long id, String username, String createdDate, String role, String status, String statusModifiedDate) {}
}
