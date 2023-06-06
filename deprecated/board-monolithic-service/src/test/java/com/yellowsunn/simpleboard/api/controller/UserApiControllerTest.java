package com.yellowsunn.simpleboard.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellowsunn.simpleboard.api.SessionConst;
import com.yellowsunn.simpleboard.api.dto.user.UserGetDto;
import com.yellowsunn.simpleboard.api.dto.user.UserLoginDto;
import com.yellowsunn.simpleboard.api.dto.user.UserPatchRequestDto;
import com.yellowsunn.simpleboard.api.dto.user.UserRegisterDto;
import com.yellowsunn.simpleboard.api.service.UserService;
import com.yellowsunn.simpleboard.api.util.RefererFilter;
import com.yellowsunn.simpleboard.domain.user.Role;
import com.yellowsunn.simpleboard.domain.user.User;
import com.yellowsunn.simpleboard.domain.user.repository.UserRepository;
import com.yellowsunn.simpleboard.exception.ForbiddenException;
import com.yellowsunn.simpleboard.exception.NotFoundException;
import com.yellowsunn.simpleboard.exception.PasswordMismatchException;
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
    UserRepository userRepository;

    @MockBean
    User mockUser;

    @MockBean
    RefererFilter refererFilter;

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
        dtos.add(UserRegisterDto.builder().username(" ").password("12345678").build());

        dtos.add(UserRegisterDto.builder().username("username").password("").build());
        dtos.add(UserRegisterDto.builder().username("username").password("1234567").build());
        dtos.add(UserRegisterDto.builder().username("username").password("12345678901234567").build());

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
        doThrow(NotFoundException.class).when(userService).login(dto);

        //then
        ResultActions resultActions = mvc.perform(request)
                .andExpect(status().isNotFound());
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
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("현재 로그인한 회원 조회 실패 및 세션 무효화(로그인 인증이 되어 있을 때)")
    void FailedToFindCurrentLoggedInUser() throws Exception {
        //given
        MockHttpServletRequestBuilder request = getCurrentUserRequest();
        setLoginSession(request);

        //mocking
        given(userService.findUserById(userId)).willThrow(NotFoundException.class);

        //then
        ResultActions resultActions = mvc.perform(request)
                .andExpect(status().isNotFound());
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
        doThrow(NotFoundException.class).when(userService).changePassword(userId, dto);

        //then
        ResultActions resultActions = mvc.perform(request)
                .andExpect(status().isNotFound());
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
        doThrow(NotFoundException.class).when(userService).deleteCurrentUser(userId);

        //then
        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("아이디로 회원 삭제 성공")
    void deleteById() throws Exception {
        //given
        MockHttpServletRequestBuilder request = deleteByIdRequest(userId);
        setLoginSession(request);
        request.sessionAttr(SessionConst.USER_ROLE, Role.ADMIN);

        //then
        mvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("아이디로 회원 삭제 인증 실패")
    void unauthorizedForDeleteById() throws Exception {
        //given
        MockHttpServletRequestBuilder request = deleteByIdRequest(userId);

        //then
        mvc.perform(request)
                .andExpect(status().isUnauthorized());
    }
    @Test
    @DisplayName("아이디로 회원 삭제 실패 - 권한 부족")
    void forbiddenDeleteById() throws Exception {
        //given
        MockHttpServletRequestBuilder request = deleteByIdRequest(userId);
        setLoginSession(request);

        //then
        mvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("아이디로 회원 삭제 실패 - 관리자는 삭제할 수 없다")
    void failedToDeleteById() throws Exception {
        //given
        MockHttpServletRequestBuilder request = deleteByIdRequest(userId);
        setLoginSession(request);
        request.sessionAttr(SessionConst.USER_ROLE, Role.ADMIN);

        //mocking
        doThrow(ForbiddenException.class).when(userService).deleteById(userId);

        //then
        mvc.perform(request)
                .andExpect(status().isForbidden());
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

    private MockHttpServletRequestBuilder deleteByIdRequest(Long id) {
        return delete("/api/users/" + id);
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
        resultActions
                .andExpect(status().isUnauthorized());
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
        userGetDto.setRole(Role.USER);

        return userGetDto;
    }

    private UserPatchRequestDto getTestUserPatchRequestDto() {
        return UserPatchRequestDto.builder()
                .password("password").newPassword("password2").build();
    }
}
