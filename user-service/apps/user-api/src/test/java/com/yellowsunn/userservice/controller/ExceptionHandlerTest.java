package com.yellowsunn.userservice.controller;

import com.yellowsunn.common.exception.LoginUserNotFoundException;
import com.yellowsunn.userservice.facade.UserAuthFacade;
import com.yellowsunn.userservice.facade.UserFacade;
import com.yellowsunn.userservice.service.UserAuthService;
import com.yellowsunn.userservice.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.yellowsunn.common.constant.CommonHeaderConst.USER_ID;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {ExternalUserController.class, ExternalUserAuthController.class})
class ExceptionHandlerTest extends RestDocsApiTest {
    @MockBean
    UserAuthFacade userAuthFacade;
    @MockBean
    UserAuthService userAuthService;
    @MockBean
    UserFacade userFacade;
    @MockBean
    UserService userService;

    @DisplayName("로그인 필요 에러")
    @Test
    void requiredLoginTest() throws Exception {
        mockMvc.perform(delete("/api/v2/users/me"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andDo(document("required-login-error",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

    }

    @DisplayName("로그인 유저를 찾을 수 없음")
    @Test
    void userNotFoundError() throws Exception {
        given(userService.deleteUserInfo(1L)).willThrow(LoginUserNotFoundException.class);

        mockMvc.perform(delete("/api/v2/users/me").header(USER_ID, 1L))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andDo(document("login-user-notfound-error",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @DisplayName("잘못된 요청 에러")
    @Test
    void badRequest() throws Exception {
        given(userService.deleteUserInfo(1L)).willThrow(new IllegalArgumentException("<< message >>"));

        mockMvc.perform(delete("/api/v2/users/me").header(USER_ID, 1L))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(document("bad-request-error",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @DisplayName("알 수 없는 에러")
    @Test
    void unknownError() throws Exception {
        given(userService.deleteUserInfo(1L)).willThrow(RuntimeException.class);

        mockMvc.perform(delete("/api/v2/users/me").header(USER_ID, 1L))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andDo(document("unknown-error",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

    }
}
