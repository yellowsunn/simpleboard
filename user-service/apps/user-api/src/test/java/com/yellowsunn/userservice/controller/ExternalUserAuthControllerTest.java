//package com.yellowsunn.userservice.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.yellowsunn.userservice.constant.OAuth2Type;
//import com.yellowsunn.userservice.controller.request.EmailLoginRequest;
//import com.yellowsunn.userservice.controller.request.EmailSignUpRequest;
//import com.yellowsunn.userservice.controller.request.OAuth2LinkUserRequest;
//import com.yellowsunn.userservice.controller.request.OAuth2LoginOrSignUpRequest;
//import com.yellowsunn.userservice.controller.request.OAuth2SignUpRequest;
//import com.yellowsunn.userservice.controller.request.RefreshAccessTokenRequest;
//import com.yellowsunn.userservice.application.command.UserEmailLoginCommand;
//import com.yellowsunn.userservice.application.command.UserEmailSignUpCommand;
//import com.yellowsunn.userservice.dto.UserLoginTokenDto;
//import com.yellowsunn.userservice.application.command.UserOAuth2LinkCommand;
//import com.yellowsunn.userservice.application.command.UserOAuth2LoginOrSignUpCommand;
//import com.yellowsunn.userservice.dto.UserOAuth2LoginOrSignUpDto;
//import com.yellowsunn.userservice.application.command.UserOAuth2SignUpCommand;
//import com.yellowsunn.userservice.application.UserAuthFacade;
//import com.yellowsunn.userservice.application.UserAuthService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//
//import java.util.Arrays;
//import java.util.stream.Collectors;
//
//import static com.yellowsunn.common.constant.CommonHeaderConst.USER_ID;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
//import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
//import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
//import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
//import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
//import static org.springframework.restdocs.request.RequestDocumentation.formParameters;
//import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(ExternalUserAuthController.class)
//class ExternalUserAuthControllerTest extends RestDocsApiTest {
//    @MockBean
//    UserAuthFacade userAuthFacade;
//
//    @MockBean
//    UserAuthService userAuthService;
//
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    @Test
//    @DisplayName("이메일 회원가입")
//    void signUpEmail() throws Exception {
//        var requestDto = new EmailSignUpRequest("test@example.com", "12345678", "닉네임");
//        given(userAuthService.signUpEmail(any(UserEmailSignUpCommand.class))).willReturn(true);
//
//        mockMvc.perform(post("/api/v2/auth/email/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto))
//                ).andDo(print())
//                .andExpect(status().isCreated())
//                .andDo(document("post-email-signup",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("email").description("이메일"),
//                                fieldWithPath("password").description("비밀번호"),
//                                fieldWithPath("nickName").description("닉네임")
//                        ),
//                        responseFields(
//                                fieldWithPath("success").description("성공 여부"),
//                                fieldWithPath("code").description("결과 코드"),
//                                fieldWithPath("message").description("메시지"),
//                                fieldWithPath("data").description("생성 여부")
//                        )
//                ));
//    }
//
//    @Test
//    @DisplayName("이메일 로그인")
//    void loginEmail() throws Exception {
//        var requestDto = new EmailLoginRequest("test@example.com", "12345678");
//        given(userAuthService.loginEmail(any(UserEmailLoginCommand.class)))
//                .willReturn(UserLoginTokenDto.builder()
//                        .accessToken("<< access token >>")
//                        .refreshToken("<< refresh token >>")
//                        .build());
//
//        mockMvc.perform(post("/api/v2/auth/email/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto))
//                ).andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("post-email-login",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("email").description("이메일"),
//                                fieldWithPath("password").description("패스워드")
//                        ),
//                        responseFields(
//                                fieldWithPath("success").description("성공 여부"),
//                                fieldWithPath("code").description("결과 코드"),
//                                fieldWithPath("message").description("메시지"),
//                                fieldWithPath("data.accessToken").description("액세스 토큰"),
//                                fieldWithPath("data.refreshToken").description("리프레시 토큰")
//                        )
//                ));
//    }
//
//    @Test
//    @DisplayName("OAuth2 로그인")
//    void loginOAuth2() throws Exception {
//        var state = "ZTdjMGU5NjgtMDRkZS0xMWVlLWJlNTYtMDI0MmFjMTIwMDAy";
//        var requestDto = new OAuth2LoginOrSignUpRequest("<< OAuth2 token >>", "google", state);
//        given(userAuthFacade.loginOrSignUpRequest(any(UserOAuth2LoginOrSignUpCommand.class)))
//                .willReturn(UserOAuth2LoginOrSignUpDto.loginBuilder()
//                        .accessToken("<< access token >>")
//                        .refreshToken("<< refresh token >>")
//                        .build()
//                );
//
//        mockMvc.perform(post("/api/v2/auth/oauth2/authorize")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto))
//                ).andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("post-oauth2-login",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("token").description("OAuth2 인증 토큰"),
//                                fieldWithPath("type").description("OAuth2 로그인 타입 ex) " + oauth2TypeValues()),
//                                fieldWithPath("state").description("상태 코드 (회원가입 시 사용되며 회원가입이 완료될 때 까지 동일한 코드를 사용해야 함)")
//                        ),
//                        responseFields(
//                                fieldWithPath("success").description("성공 여부"),
//                                fieldWithPath("code").description("결과 코드"),
//                                fieldWithPath("message").description("메시지"),
//                                fieldWithPath("data.isLogin").description("로그인 성공 여부"),
//                                fieldWithPath("data.accessToken").description("액세스 토큰"),
//                                fieldWithPath("data.refreshToken").description("리프레시 토큰")
//                        )
//                ));
//    }
//
//    @DisplayName("OAuth2 회원가입 요청")
//    @Test
//    void singUpRequestOAuth2() throws Exception {
//        var state = "ZTdjMGU5NjgtMDRkZS0xMWVlLWJlNTYtMDI0MmFjMTIwMDAy";
//        var requestDto = new OAuth2LoginOrSignUpRequest("<< OAuth2 token >>", "google", state);
//        given(userAuthFacade.loginOrSignUpRequest(any(UserOAuth2LoginOrSignUpCommand.class)))
//                .willReturn(UserOAuth2LoginOrSignUpDto.signUpRequestBuilder()
//                        .tempUserToken("<< temp user token >>")
//                        .build()
//                );
//
//        mockMvc.perform(post("/api/v2/auth/oauth2/authorize")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto))
//                ).andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("post-oauth2-signup-request",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("token").description("OAuth2 인증 토큰"),
//                                fieldWithPath("type").description("OAuth2 로그인 타입 ex) " + oauth2TypeValues()),
//                                fieldWithPath("state").description("상태 코드 (회원가입 시 사용되며 회원가입이 완료될 때 까지 동일한 코드를 사용해야 함)")
//                        ),
//                        responseFields(
//                                fieldWithPath("success").description("성공 여부"),
//                                fieldWithPath("code").description("결과 코드"),
//                                fieldWithPath("message").description("메시지"),
//                                fieldWithPath("data.isLogin").description("로그인 성공 여부"),
//                                fieldWithPath("data.tempUserToken").description("임시 회원 토큰 (추가 회원정보를 입력하기 전에 발급되는 임시 토큰)")
//                        )
//                ));
//    }
//
//    @DisplayName("OAuth2 회원가입 완료")
//    @Test
//    void singUpCompleteOAuth2() throws Exception {
//        var state = "ZTdjMGU5NjgtMDRkZS0xMWVlLWJlNTYtMDI0MmFjMTIwMDAy";
//        var tempUserToken = "<< temp user token >>";
//        var requestDto = new OAuth2SignUpRequest(state, tempUserToken, "닉네임");
//        given(userAuthFacade.signUpOAuth2(any(UserOAuth2SignUpCommand.class)))
//                .willReturn(UserLoginTokenDto.builder()
//                        .accessToken("<< access token >>")
//                        .refreshToken("<< refresh token >>")
//                        .build());
//
//        mockMvc.perform(post("/api/v2/auth/oauth2/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto))
//                ).andDo(print())
//                .andExpect(status().isCreated())
//                .andDo(document("post-oauth2-signup-complete",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("state").description("상태 코드 (회원가입 시 사용되며 회원가입이 완료될 때 까지 동일한 코드를 사용해야 함)"),
//                                fieldWithPath("tempUserToken").description("회원가입 요청 시 발급된 임시 회원 토큰"),
//                                fieldWithPath("nickName").description("사용하려는 닉네임")
//                        ),
//                        responseFields(
//                                fieldWithPath("success").description("성공 여부"),
//                                fieldWithPath("code").description("결과 코드"),
//                                fieldWithPath("message").description("메시지"),
//                                fieldWithPath("data.accessToken").description("액세스 토큰"),
//                                fieldWithPath("data.refreshToken").description("리프레시 토큰")
//                        )
//                ));
//    }
//
//    @DisplayName("OAuth2 계정 연동")
//    @Test
//    void linkOAuth2() throws Exception {
//        var requestDto = new OAuth2LinkUserRequest("<< OAuth2 token >>", "google");
//        given(userAuthFacade.linkOAuth2User(any(UserOAuth2LinkCommand.class)))
//                .willReturn(true);
//
//        mockMvc.perform(put("/api/v2/auth/oauth2/link")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto))
//                        .header(USER_ID, 1L)
//                ).andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("put-link-oauth2",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestHeaders(
//                                headerWithName("x-user-id").description("로그인한 유저 id")
//                        ),
//                        requestFields(
//                                fieldWithPath("token").description("OAuth2 인증 토큰"),
//                                fieldWithPath("type").description("OAuth2 로그인 타입 ex) " + oauth2TypeValues())
//                        ),
//                        responseFields(
//                                fieldWithPath("success").description("성공 여부"),
//                                fieldWithPath("code").description("결과 코드"),
//                                fieldWithPath("message").description("메시지"),
//                                fieldWithPath("data").description("연동 성공 여부")
//                        )
//                ));
//    }
//
//    @DisplayName("OAuth2 계정 연동 해제")
//    @Test
//    void unlinkOAuth2() throws Exception {
//        var type = "google";
//        given(userAuthService.unlinkOAuth2User(1L, OAuth2Type.GOOGLE))
//                .willReturn(true);
//
//        mockMvc.perform(delete("/api/v2/auth/oauth2/link")
//                        .header(USER_ID, 1L)
//                        .param("type", type)
//                ).andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("delete-link-oauth2",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestHeaders(
//                                headerWithName("x-user-id").description("로그인한 유저 id")
//                        ),
//                        formParameters(
//                                parameterWithName("type").description("OAuth2 로그인 타입 ex) " + oauth2TypeValues())
//                        ),
//                        responseFields(
//                                fieldWithPath("success").description("성공 여부"),
//                                fieldWithPath("code").description("결과 코드"),
//                                fieldWithPath("message").description("메시지"),
//                                fieldWithPath("data").description("연동 해제 성공 여부")
//                        )
//                ));
//    }
//
//    @DisplayName("토큰 갱신")
//    @Test
//    void refreshUserToken() throws Exception {
//        var accessToken = "<< access token >>";
//        var refreshToken = "<< refresh token >>";
//        var requestDto = new RefreshAccessTokenRequest(accessToken, refreshToken);
//        given(userAuthService.refreshUserToken(accessToken, refreshToken))
//                .willReturn(UserLoginTokenDto.builder()
//                        .accessToken("<< new access token >>")
//                        .refreshToken("<< new refresh token >>")
//                        .build());
//
//        mockMvc.perform(post("/api/v2/auth/token")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto))
//                ).andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("post-refresh-user-token",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("accessToken").description("액세스 토큰"),
//                                fieldWithPath("refreshToken").description("리프레시 토큰")
//                        ),
//                        responseFields(
//                                fieldWithPath("success").description("성공 여부"),
//                                fieldWithPath("code").description("결과 코드"),
//                                fieldWithPath("message").description("메시지"),
//                                fieldWithPath("data.accessToken").description("갱신된 액세스 토큰"),
//                                fieldWithPath("data.refreshToken").description("갱신된 리프레시 토큰")
//                        )
//                ));
//    }
//
//    private String oauth2TypeValues() {
//        return Arrays.stream(OAuth2Type.values()).map(Enum::name)
//                .collect(Collectors.joining(", "));
//    }
//}
