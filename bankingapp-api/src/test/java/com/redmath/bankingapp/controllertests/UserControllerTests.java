package com.redmath.bankingapp.controllertests;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void testFindByUname() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/findByUname")
                        .param("uname", "sara1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string(
                        Matchers.equalTo("")
                ));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/findByUname")
                        .param("uname", "sara1")
                        .with(testUser("admin","ADMIN"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string(
                        Matchers.equalTo("")
                ));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/findByUname")
                        .param("uname", "sara1")
                        .with(testUser("sara1","USER"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

    }

    @Test
    @Order(2)
    public void testGetUserByAccountId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .param("accountId","1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string(
                        Matchers.equalTo("")
                ));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .param("accountId","1")
                        .with(testUser("admin","ADMIN"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string(
                        Matchers.equalTo("")
                ));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .param("accountId","1")
                        .with(testUser("sara1","USER"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .param("accountId","2")
                        .with(testUser("sara1","USER"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(
                        Matchers.equalTo("")
                ));
    }
    @Test
    @Order(3)
    public void testChangePasswordByAccountId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/changePassword")
                        .param("accountId","1")
                        .contentType("application/json")
                        .content("saraaa"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string(
                        Matchers.equalTo("")
                ));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/changePassword")
                        .param("accountId","1")
                        .with(testUser("admin","ADMIN"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json")
                        .content("saraaa"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string(
                        Matchers.equalTo("")
                ));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/changePassword")
                        .param("accountId","1")
                        .with(testUser("sara1","USER"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json")
                        .content("saraaa"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/changePassword")
                        .param("accountId","2")
                        .with(testUser("sara1","USER"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json")
                        .content("saraaa"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    private RequestPostProcessor testUser(String uname, String authority) {
        return SecurityMockMvcRequestPostProcessors.user(uname).authorities(new SimpleGrantedAuthority(authority));
    }
}

