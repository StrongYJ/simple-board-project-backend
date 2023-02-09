package com.myproject.simpleboard;

import com.myproject.simpleboard.domain.member.entity.model.MemberRole;
import com.myproject.simpleboard.global.security.JwtProperties;
import com.myproject.simpleboard.global.security.TokenUtils;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecureTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private TokenUtils tokenUtils;

    @Test
    void onlyManager() throws Exception {
        String userToken = tokenUtils.createAccessToken(1L, MemberRole.USER);
        String managerToken = tokenUtils.createAccessToken(1L, MemberRole.MANAGER);
        String adminToken = tokenUtils.createAccessToken(1L, MemberRole.ADMIN);

        mockMvc.perform(get("/members/manager-test").header(HttpHeaders.AUTHORIZATION, userToken))
                .andExpect(status().isForbidden())
                .andDo(print());
        mockMvc.perform(get("/members/manager-test").header(HttpHeaders.AUTHORIZATION, managerToken))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(get("/members/manager-test").header(HttpHeaders.AUTHORIZATION, adminToken))
                .andExpect(status().isForbidden())
                .andDo(print());

        mockMvc.perform(get("/members/admin-test").header(HttpHeaders.AUTHORIZATION, userToken))
                .andExpect(status().isForbidden())
                .andDo(print());
        mockMvc.perform(get("/members/admin-test").header(HttpHeaders.AUTHORIZATION, managerToken))
                .andExpect(status().isForbidden())
                .andDo(print());
        mockMvc.perform(get("/members/admin-test").header(HttpHeaders.AUTHORIZATION, adminToken))
                .andExpect(status().isOk())
                .andDo(print());

        mockMvc.perform(get("/members/admin-manager-test").header(HttpHeaders.AUTHORIZATION, userToken))
                .andExpect(status().isForbidden())
                .andDo(print());
        mockMvc.perform(get("/members/admin-manager-test").header(HttpHeaders.AUTHORIZATION, managerToken))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(get("/members/admin-manager-test").header(HttpHeaders.AUTHORIZATION, adminToken))
                .andExpect(status().isOk())
                .andDo(print());
    }

}
