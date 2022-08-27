package com.example.shop2.domain.controller;

import com.example.shop2.domain.dto.MemberFormDto;
import com.example.shop2.domain.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ItemControllerTest {

    @Autowired
    private MockMvc mock;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("Item Register test")
    @WithMockUser(roles = "ADMIN")
    void itemRegisterTest() throws Exception {
        //given
        mock.perform(get("/admin/item/new"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Item Register NotAdmin test")
    @WithMockUser(roles = "USER")
    void itemRegisterNotAdminTest() throws Exception {
        //given
        mock.perform(get("/admin/item/new"))
                .andDo(print())
                .andExpect(status().isForbidden());

    }

}