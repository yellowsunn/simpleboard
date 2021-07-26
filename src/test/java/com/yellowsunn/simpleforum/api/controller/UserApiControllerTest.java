package com.yellowsunn.simpleforum.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellowsunn.simpleforum.api.SessionConst;
import com.yellowsunn.simpleforum.api.dto.user.UserGetDto;
import com.yellowsunn.simpleforum.api.dto.user.UserLoginDto;
import com.yellowsunn.simpleforum.api.dto.user.UserPatchRequestDto;
import com.yellowsunn.simpleforum.api.dto.user.UserRegisterDto;
import com.yellowsunn.simpleforum.api.service.UserService;
import com.yellowsunn.simpleforum.domain.user.User;
import com.yellowsunn.simpleforum.exception.NotFoundUserException;
import com.yellowsunn.simpleforum.exception.PasswordMismatchException;
import org.apache.juli.logging.Log;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserApiController.class)
class UserApiControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("회원가입 성공")
    void register() throws Exception {
        //given
        UserRegisterDto dto = UserRegisterDto.builder()
                .username("username")
                .password("12345678")
                .nickname("nickname")
                .build();

        //mocking
        doNothing().when(userService).register(dto);

        //then
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
            //mocking
            doNothing().when(userService).register(dto);

            mvc.perform(post("/api/users")
                    .content(objectMapper.writeValueAsString(dto))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest());
        }
    }

    @Test
    @DisplayName("회원 가입 아이디 중복 에러")
    void duplicateRegistration() throws Exception {
        //given
        UserRegisterDto dto = UserRegisterDto.builder()
                .username("username")
                .password("12345678")
                .nickname("hello")
                .build();

        //mocking
        doThrow(DataIntegrityViolationException.class).when(userService).register(dto);

        //then
        mvc.perform(post("/api/users")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인 성공(세션 생성)")
    void login() throws Exception {
        //given
        UserLoginDto dto = UserLoginDto.builder()
                .username("username")
                .password("12345678")
                .build();

        //mocking
        given(userService.login(dto)).willReturn(1L);

        //then
        mvc.perform(post("/api/users/login")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute(SessionConst.USER_ID, 1L));
    }

    @Test
    @DisplayName("로그인 검증 실패(세션 생성x)")
    void validationFailForLogin() throws Exception {
        //given
        List<UserLoginDto> dtos = new ArrayList<>();
        dtos.add(UserLoginDto.builder().username(" ").password("12345678").build());

        dtos.add(UserLoginDto.builder().username("username").password("").build());
        dtos.add(UserLoginDto.builder().username("username").password("1234567").build());
        dtos.add(UserLoginDto.builder().username("username").password("12345678901234567").build());

        for (UserLoginDto dto : dtos) {
            //mocking
            given(userService.login(dto)).willReturn(1L);

            //then
            mvc.perform(post("/api/users/login")
                    .content(objectMapper.writeValueAsString(dto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(status().isBadRequest())
                    .andExpect(request().sessionAttributeDoesNotExist(SessionConst.USER_ID));
        }
    }

    @Test
    @DisplayName("로그인 실패 에러(세션 생성x)")
    void inValidUserLogin() throws Exception {
        //given
        UserLoginDto dto = UserLoginDto.builder()
                .username("username")
                .password("12345678")
                .build();

        //mocking
        doThrow(NotFoundUserException.class).when(userService).login(dto);

        //then
        mvc.perform(post("/api/users/login")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound())
                .andExpect(request().sessionAttributeDoesNotExist(SessionConst.USER_ID));
    }

    @Test
    @DisplayName("현재 로그인한 회원 조회(로그인 인증이 되어있을 때)")
    void findCurrentLoggedInUser() throws Exception {
        //given
        UserGetDto userGetDto = new UserGetDto();
        userGetDto.setId(1L);
        userGetDto.setUsername("username");
        userGetDto.setNickname("nickname");

        //mocking
        given(userService.findUserById(userGetDto.getId())).willReturn(userGetDto);

        mvc.perform(get("/api/users/current")
                .sessionAttr(SessionConst.USER_ID, userGetDto.getId())
        )
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute(SessionConst.USER_ID, userGetDto.getId()))
                .andExpect(content().string("{\"id\":1,\"username\":\"username\",\"nickname\":\"nickname\"}"));
    }

    @Test
    @DisplayName("현재 로그인한 회원 조회 실패 및 세션 무효화(로그인 인증이 되어 있을 때)")
    void FailedToFindCurrentLoggedInUser() throws Exception {
        //given
        UserGetDto userGetDto = new UserGetDto();
        userGetDto.setId(1L);
        userGetDto.setUsername("username");
        userGetDto.setNickname("nickname");

        //mocking
        given(userService.findUserById(userGetDto.getId())).willThrow(NotFoundUserException.class);

        //then
        mvc.perform(get("/api/users/current")
                .sessionAttr(SessionConst.USER_ID, userGetDto.getId())
        )
                .andExpect(status().isNotFound())
                .andExpect(request().sessionAttributeDoesNotExist(SessionConst.USER_ID));
    }

    @Test
    @DisplayName("현재 접속한 회원 조회 인증 실패")
    void unauthorizedForFindCurrentUser() throws Exception {

        //then
        mvc.perform(get("/api/users/current"))
                .andExpect(status().isUnauthorized())
                .andExpect(request().sessionAttributeDoesNotExist(SessionConst.USER_ID));
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    void changePassword() throws Exception {
        //given
        Long userId = 1L;
        UserPatchRequestDto dto = UserPatchRequestDto.builder()
                .password("password")
                .newPassword("password2")
                .build();

        //then
        mvc.perform(patch("/api/users/current")
                .sessionAttr(SessionConst.USER_ID, userId)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("비밀번호 변경 인증 실패")
    void unauthorizedForChangePassword() throws Exception {
        //then
        mvc.perform(patch("/api/users/current"))
                .andExpect(status().isUnauthorized())
                .andExpect(request().sessionAttributeDoesNotExist(SessionConst.USER_ID));
    }

    @Test
    @DisplayName("비밀번호 변경 검증 실패")
    void validationFailedForChangePassword() throws Exception {
        //given
        Long userId = 1L;
        List<UserPatchRequestDto> dtos = new ArrayList<>();
        dtos.add(UserPatchRequestDto.builder().password("").newPassword("password2").build());
        dtos.add(UserPatchRequestDto.builder().password("password").newPassword("").build());
        dtos.add(UserPatchRequestDto.builder().password("password").newPassword("passwor").build());
        dtos.add(UserPatchRequestDto.builder().password("password").newPassword("passwordpasswordp").build());

        //then
        for (UserPatchRequestDto dto : dtos) {
            mvc.perform(patch("/api/users/current")
                    .sessionAttr(SessionConst.USER_ID, userId)
                    .content(objectMapper.writeValueAsString(dto))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest());
        }
    }

    @Test
    @DisplayName("비밀번호 변경하려는 회원 조회 실패")
    void failedToFindUserWhoWantChangePassword() throws Exception {
        //given
        Long userId = 1L;
        UserPatchRequestDto dto = UserPatchRequestDto.builder()
                .password("password").newPassword("password2").build();
        //mocking
        doThrow(NotFoundUserException.class).when(userService).changePassword(userId, dto);

        //then
        mvc.perform(patch("/api/users/current")
                .sessionAttr(SessionConst.USER_ID, userId)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("비밀번호 변경 요청시 기존 비밀번호 입력이 일치하지 않는 경우 에러")
    void invalidOldPassword() throws Exception {
        //given
        Long userId = 1L;
        UserPatchRequestDto dto = UserPatchRequestDto.builder()
                .password("password").newPassword("password2").build();

        //mocking
        doThrow(PasswordMismatchException.class).when(userService).changePassword(userId, dto);

        //then
        mvc.perform(patch("/api/users/current")
                .sessionAttr(SessionConst.USER_ID, userId)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }
}