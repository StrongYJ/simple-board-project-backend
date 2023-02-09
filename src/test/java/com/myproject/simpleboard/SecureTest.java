package com.myproject.simpleboard;

import com.myproject.simpleboard.domain.member.entity.model.MemberRole;
import com.myproject.simpleboard.global.security.JwtProperties;
import com.myproject.simpleboard.global.security.TokenUtils;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

        Cookie userCookie = new Cookie(JwtProperties.REFRESH_NANE, userToken);
        userCookie.setHttpOnly(true);

        Cookie managerCookie = new Cookie(JwtProperties.REFRESH_NANE, managerToken);
        managerCookie.setHttpOnly(true);

        Cookie adminCookie = new Cookie(JwtProperties.REFRESH_NANE, adminToken);
        adminCookie.setHttpOnly(true);

        mockMvc.perform(get("/members/manager-test").cookie(userCookie))
                .andExpect(status().isForbidden())
                .andDo(print());
        mockMvc.perform(get("/members/manager-test").cookie(managerCookie))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(get("/members/manager-test").cookie(adminCookie))
                .andExpect(status().isForbidden())
                .andDo(print());

        mockMvc.perform(get("/members/admin-test").cookie(userCookie))
                .andExpect(status().isForbidden())
                .andDo(print());
        mockMvc.perform(get("/members/admin-test").cookie(managerCookie))
                .andExpect(status().isForbidden())
                .andDo(print());
        mockMvc.perform(get("/members/admin-test").cookie(adminCookie))
                .andExpect(status().isOk())
                .andDo(print());

        mockMvc.perform(get("/members/admin-manager-test").cookie(userCookie))
                .andExpect(status().isForbidden())
                .andDo(print());
        mockMvc.perform(get("/members/admin-manager-test").cookie(managerCookie))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(get("/members/admin-manager-test").cookie(adminCookie))
                .andExpect(status().isOk())
                .andDo(print());
    }

}
