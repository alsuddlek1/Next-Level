package com.ddoya.auth.user.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ddoya.auth.common.jwt.JwtTokenProvider;
import com.ddoya.auth.common.oauth.CustomUserDetails;
import com.ddoya.auth.common.util.RedisService;
import com.ddoya.auth.common.util.TokenInfo;
import com.ddoya.auth.user.entity.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles(profiles = {"prod", "jwt", "oauth"})
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RedisService redisService;

    private CustomUserDetails user;
    private String accessToken;

    private Collection<? extends GrantedAuthority> authorities(Role role) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.name()));
        return authorities;
    }

    @BeforeEach
    void setUp() {
        Collection<? extends GrantedAuthority> authorities = authorities(Role.ROLE_USER);
        CustomUserDetails principal = new CustomUserDetails(1L, "test@test.com", authorities);
        user = principal;
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            principal, "", authorities);

        accessToken = jwtTokenProvider.generateToken(authentication).getAccessToken();
    }

    @Test
    @DisplayName("유저 정보 조회 테스트")
    void getUserInformationTest() throws Exception {

        mockMvc.perform(get("/auth/user").with(SecurityMockMvcRequestPostProcessors.user(user))
                .header("Authorization", "Bearer " + accessToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("user-information",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("JWT Access Token")
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지"),
                    fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                    fieldWithPath("data.name").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("data.language").type(JsonFieldType.STRING).description("언어"),
                    fieldWithPath("data.score").type(JsonFieldType.NUMBER).description("점수"),
                    fieldWithPath("data.lastAttendanceDate").type(JsonFieldType.STRING)
                        .description("마지막 출석 날짜"),
                    fieldWithPath("data.nickName").type(JsonFieldType.STRING).description("닉네임"),
                    fieldWithPath("data.profileImageUrl").type(JsonFieldType.STRING)
                        .description("프로필 사진 URL")
                )));
    }

    @Test
    @DisplayName("추가 정보 입력 테스트")
    void addInformationsTest() throws Exception {
        Collection<? extends GrantedAuthority> authorities = authorities(Role.ROLE_GUEST);
        CustomUserDetails principal = new CustomUserDetails(1L, "test@test.com", authorities);
        user = principal;
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            principal, "", authorities);

        accessToken = jwtTokenProvider.generateToken(authentication).getAccessToken();

        Map<String, String> body = new HashMap<>();
        body.put("nickName", "test2");
        body.put("language", "en");

        mockMvc.perform(post("/auth/user/add-informations").with(
                    SecurityMockMvcRequestPostProcessors.user(user))
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("add-information",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("JWT Access Token")
                ),
                requestFields(
                    fieldWithPath("nickName").type(JsonFieldType.STRING).description("변경할 닉네임"),
                    fieldWithPath("language").type(JsonFieldType.STRING).description("변경할 언어")
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지"),
                    fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                    fieldWithPath("data").type(JsonFieldType.STRING).description("데이터 - 엑세스 토큰")
                )));
    }

    @Test
    @DisplayName("회원 정보 수정 테스트")
    void updateInformationsTest() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("nickName", "test2");
        body.put("language", "en");

        mockMvc.perform(
                post("/auth/user/update").with(SecurityMockMvcRequestPostProcessors.user(user))
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(body)))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("update-information",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("JWT Access Token")
                ),
                requestFields(
                    fieldWithPath("nickName").type(JsonFieldType.STRING).description("변경할 닉네임"),
                    fieldWithPath("language").type(JsonFieldType.STRING).description("변경할 언어")
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지"),
                    fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                    fieldWithPath("data").type(JsonFieldType.NULL).description("데이터")
                )));
    }

    @Test
    @DisplayName("토큰 재발급 테스트")
    void reissueTest() throws Exception {
        Collection<? extends GrantedAuthority> authorities = authorities(Role.ROLE_USER);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            user, "", authorities);

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        redisService.setValues(user.getName(), tokenInfo.getRefreshToken(),
            Duration.ofSeconds(
                jwtTokenProvider.getTokenExpirationTime(tokenInfo.getRefreshToken())));

        mockMvc.perform(
                post("/auth/user/reissue").with(SecurityMockMvcRequestPostProcessors.user(user))
                    .header("Authorization", "Bearer " + tokenInfo.getAccessToken())
                    .cookie(new Cookie("refresh_token", tokenInfo.getRefreshToken())))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("reissue",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("JWT Access Token")
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지"),
                    fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                    fieldWithPath("data").type(JsonFieldType.STRING).description("데이터 - 엑세스 토큰")
                )));
    }
}
