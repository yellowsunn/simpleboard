package com.yellowsunn.simpleforum.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellowsunn.simpleforum.api.SessionConst;
import com.yellowsunn.simpleforum.api.dto.user.UserGetDto;
import com.yellowsunn.simpleforum.api.dto.user.UserLoginDto;
import com.yellowsunn.simpleforum.api.dto.user.UserPatchRequestDto;
import com.yellowsunn.simpleforum.api.dto.user.UserRegisterDto;
import com.yellowsunn.simpleforum.api.service.UserService;
import com.yellowsunn.simpleforum.domain.user.Role;
import com.yellowsunn.simpleforum.domain.user.User;
import com.yellowsunn.simpleforum.exception.NotFoundUserException;
import com.yellowsunn.simpleforum.exception.PasswordMismatchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserApiController.class)
class UserApiControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    @MockBean
    User mockUser;

    ObjectMapper objectMapper = new ObjectMapper();

    Long userId = 1L;
    Role userRole = Role.USER;

    @Test
    @DisplayName("회원가입 성공")
    void register() throws Exception {
        //given
        UserRegisterDto dto = getTestUserRegisterDto();
        MockHttpServletRequestBuilder request = registerRequest();
        setJsonContent(request, dto);
        //then
        mvc.perform(request).andExpect(status().isCreated());
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

        MockHttpServletRequestBuilder request = registerRequest();
        //then
        for (UserRegisterDto dto : dtos) {
            setJsonContent(request, dto);
            mvc.perform(request)
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    @DisplayName("회원 가입 아이디 중복 에러")
    void duplicateRegistration() throws Exception {
        //given
        UserRegisterDto dto = getTestUserRegisterDto();

        MockHttpServletRequestBuilder request = registerRequest();
        setJsonContent(request, dto);

        //mocking
        doThrow(DataIntegrityViolationException.class).when(userService).register(dto);

        //then
        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인 성공(세션 생성)")
    void login() throws Exception {
        //given
        UserLoginDto dto = getTestUserLoginDto();
        MockHttpServletRequestBuilder request = loginRequest();
        setJsonContent(request, dto);

        //mocking
        given(mockUser.getId()).willReturn(userId);
        given(mockUser.getRole()).willReturn(userRole);
        given(userService.login(dto)).willReturn(mockUser);

        //then
        ResultActions resultActions = mvc.perform(request)
                .andExpect(status().isOk());

        expectExistLoginSession(resultActions);
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

        MockHttpServletRequestBuilder request = loginRequest();

        for (UserLoginDto dto : dtos) {
            setJsonContent(request, dto);
            //then
            ResultActions resultActions = mvc.perform(request)
                    .andExpect(status().isBadRequest());

            expectNotExistLoginSession(resultActions);
        }
    }

    @Test
    @DisplayName("로그인 실패 에러(세션 생성x)")
    void inValidUserLogin() throws Exception {
        //given
        UserLoginDto dto = getTestUserLoginDto();
        MockHttpServletRequestBuilder request = loginRequest();
        setJsonContent(request, dto);

        //mocking
        doThrow(NotFoundUserException.class).when(userService).login(dto);

        //then
        ResultActions resultActions = mvc.perform(request)
                .andExpect(status().isNotFound());

        expectNotExistLoginSession(resultActions);
    }

    @Test
    @DisplayName("현재 로그인한 회원 조회(로그인 인증이 되어있을 때)")
    void findCurrentLoggedInUser() throws Exception {
        //given
        UserGetDto userGetDto = getTestUserGetDto();

        MockHttpServletRequestBuilder request = getCurrentUserRequest();
        setLoginSession(request);

        //mocking
        given(userService.findUserById(userGetDto.getId())).willReturn(userGetDto);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1,\"username\":\"username\",\"nickname\":\"nickname\"}"));
    }

    @Test
    @DisplayName("현재 로그인한 회원 조회 실패 및 세션 무효화(로그인 인증이 되어 있을 때)")
    void FailedToFindCurrentLoggedInUser() throws Exception {
        //given
        MockHttpServletRequestBuilder request = getCurrentUserRequest();
        setLoginSession(request);

        //mocking
        given(userService.findUserById(userId)).willThrow(NotFoundUserException.class);

        //then
        ResultActions resultActions = mvc.perform(request)
                .andExpect(status().isNotFound());
        expectNotExistLoginSession(resultActions);
    }

    @Test
    @DisplayName("현재 로그인한 회원 조회 인증 실패")
    void unauthorizedForFindCurrentUser() throws Exception {
        //given
        MockHttpServletRequestBuilder request = getCurrentUserRequest();

        //then
        ResultActions resultActions = mvc.perform(request);
        expectUnauthorized(resultActions);
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    void changePassword() throws Exception {
        //given
        UserPatchRequestDto dto = getTestUserPatchRequestDto();
        MockHttpServletRequestBuilder request = patchCurrentUserRequest();
        setLoginSession(request);
        setJsonContent(request, dto);

        //then
        mvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("비밀번호 변경 인증 실패")
    void unauthorizedForChangePassword() throws Exception {
        //given
        MockHttpServletRequestBuilder request = patchCurrentUserRequest();

        //then
        ResultActions resultActions = mvc.perform(request);
        expectUnauthorized(resultActions);
    }

    @Test
    @DisplayName("비밀번호 변경 검증 실패")
    void validationFailedForChangePassword() throws Exception {
        //given
        List<UserPatchRequestDto> dtos = new ArrayList<>();
        dtos.add(UserPatchRequestDto.builder().password("").newPassword("password2").build());
        dtos.add(UserPatchRequestDto.builder().password("password").newPassword("").build());
        dtos.add(UserPatchRequestDto.builder().password("password").newPassword("passwor").build());
        dtos.add(UserPatchRequestDto.builder().password("password").newPassword("passwordpasswordp").build());

        MockHttpServletRequestBuilder request = patchCurrentUserRequest();
        setLoginSession(request);

        for (UserPatchRequestDto dto : dtos) {
            setJsonContent(request, dto);

            mvc.perform(request)
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    @DisplayName("비밀번호 변경하려는 회원 조회 실패")
    void failedToFindUserWhoWantChangePassword() throws Exception {
        //given
        UserPatchRequestDto dto = getTestUserPatchRequestDto();
        MockHttpServletRequestBuilder request = patchCurrentUserRequest();
        setLoginSession(request);
        setJsonContent(request, dto);

        //mocking
        doThrow(NotFoundUserException.class).when(userService).changePassword(userId, dto);

        //then
        ResultActions resultActions = mvc.perform(request)
                .andExpect(status().isNotFound());

        expectNotExistLoginSession(resultActions);
    }

    @Test
    @DisplayName("비밀번호 변경 요청시 기존 비밀번호 입력이 일치하지 않는 경우 에러")
    void invalidOldPassword() throws Exception {
        //given
        UserPatchRequestDto dto = getTestUserPatchRequestDto();
        MockHttpServletRequestBuilder request = patchCurrentUserRequest();
        setLoginSession(request);
        setJsonContent(request, dto);

        //mocking
        doThrow(PasswordMismatchException.class).when(userService).changePassword(userId, dto);

        //then
        mvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("현재 로그인한 회원 삭제 성공")
    void deleteCurrentUser() throws Exception {
        //given
        MockHttpServletRequestBuilder request = deleteCurrentUserRequest();
        setLoginSession(request);

        //then
        mvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 삭제 인증 실패")
    void unauthorizedForDeleteUser() throws Exception {
        //given
        MockHttpServletRequestBuilder request = deleteCurrentUserRequest();

        //then
        ResultActions resultActions = mvc.perform(request);
        expectUnauthorized(resultActions);
    }

    @Test
    @DisplayName("삭제하려는 회원 조회 실패")
    void failedToFindUserWhoWantDelete() throws Exception {
        //given
        MockHttpServletRequestBuilder request = deleteCurrentUserRequest();
        setLoginSession(request);

        //mocking
        doThrow(NotFoundUserException.class).when(userService).deleteUserById(userId);
        
        //then
        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    private MockHttpServletRequestBuilder registerRequest() {
        return post("/api/users");
    }

    private MockHttpServletRequestBuilder loginRequest() {
        return post("/api/users/login");
    }

    private MockHttpServletRequestBuilder getCurrentUserRequest() {
        return get("/api/users/current");
    }

    private MockHttpServletRequestBuilder patchCurrentUserRequest() {
        return patch("/api/users/current");
    }

    private MockHttpServletRequestBuilder deleteCurrentUserRequest() {
        return delete("/api/users/current");
    }

    private void setLoginSession(MockHttpServletRequestBuilder builder) {
        getSession().forEach((key, value) -> builder.sessionAttr(key, value));
    }

    private void setJsonContent(MockHttpServletRequestBuilder builder, Object object) throws JsonProcessingException {
        builder
                .content(objectMapper.writeValueAsString(object))
                .contentType(MediaType.APPLICATION_JSON);
    }

    private void expectUnauthorized(ResultActions resultActions) throws Exception {
        expectNotExistLoginSession(resultActions);
        resultActions
                .andExpect(status().isUnauthorized());
    }

    private void expectNotExistLoginSession(ResultActions resultActions) throws Exception {
        for (String key : getSession().keySet()) {
            resultActions.andExpect(request().sessionAttributeDoesNotExist(key));
        }
    }

    private void expectExistLoginSession(ResultActions resultActions) throws Exception {
        for (String key : getSession().keySet()) {
            resultActions.andExpect(request().sessionAttribute(key, getSession().get(key)));
        }
    }

    private Map<String, Object> getSession() {
        Map<String, Object> session = new HashMap<>();
        session.put(SessionConst.USER_ID, userId);
        session.put(SessionConst.USER_ROLE, userRole);

        return session;
    }

    private UserRegisterDto getTestUserRegisterDto() {
        return UserRegisterDto.builder()
                .username("username")
                .password("12345678")
                .nickname("hello")
                .build();
    }

    private UserLoginDto getTestUserLoginDto() {
        return UserLoginDto.builder()
                .username("username")
                .password("12345678")
                .build();
    }

    private UserGetDto getTestUserGetDto() {
        UserGetDto userGetDto = new UserGetDto();
        userGetDto.setId(userId);
        userGetDto.setUsername("username");
        userGetDto.setNickname("nickname");
        return userGetDto;
    }

    private UserPatchRequestDto getTestUserPatchRequestDto() {
        return UserPatchRequestDto.builder()
                .password("password").newPassword("password2").build();
    }
}