package com.yellowsunn.simpleforum.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellowsunn.simpleforum.api.dto.user.UserRegisterDto;
import com.yellowsunn.simpleforum.api.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserApiController.class)
class UserApiControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void register() throws Exception {
        UserRegisterDto dto = UserRegisterDto.builder()
                .username("username")
                .password("12345678")
                .nickname("nickname")
                .build();

        mvc.perform(post("/api/users")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원가입 검증 실패")
    void validationFailForRegistration() throws Exception {
        //given
        List<UserRegisterDto> dtos = new ArrayList<>();
        dtos.add(UserRegisterDto.builder().username(" ").password("12345678").nickname("nickname").build());

        dtos.add(UserRegisterDto.builder().username("username").password("").nickname("nickname").build());
        dtos.add(UserRegisterDto.builder().username("username").password("1234567").nickname("nickname").build());
        dtos.add(UserRegisterDto.builder().username("username").password("12345678901234567").nickname("nickname").build());

        dtos.add(UserRegisterDto.builder().username("username").password("12345678").nickname(" ").build());

        //then
        for (UserRegisterDto dto : dtos) {
            mvc.perform(post("/api/users")
                    .content(objectMapper.writeValueAsString(dto))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest());
        }
    }
}