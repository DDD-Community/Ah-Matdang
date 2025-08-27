package be.ddd;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import be.ddd.api.HealthCheckAPI;
import be.ddd.domain.entity.member.Member;
import be.ddd.domain.repo.MemberRepository;
import be.ddd.infra.config.CorsConfig;
import be.ddd.infra.filter.Auth0JwtVerifier;
import be.ddd.util.TestJwtGenerator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = HealthCheckAPI.class,
        excludeFilters =
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {CorsConfig.class} // infra/config/CorsConfig
                        ))
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AuthIntegrationTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean private MemberRepository memberRepository;

    @MockitoBean private Auth0JwtVerifier auth0JwtVerifier;

    private final String TEST_AUTH0_SUB = "auth0|664b7555042386365a991846";
    private final Long TEST_MEMBER_ID = 1L;

    @BeforeEach
    void setUp() {
        Member mockMember = mock(Member.class);
        when(mockMember.getId()).thenReturn(TEST_MEMBER_ID);
        when(memberRepository.findByProviderId(TEST_AUTH0_SUB)).thenReturn(Optional.of(mockMember));
        when(memberRepository.findById(TEST_MEMBER_ID)).thenReturn(Optional.of(mockMember));
    }

    @Test
    @DisplayName("유효한 토큰으로 /api/cafe-beverages 조회 시 비어있지 않은 데이터를 반환한다")
    void getCafeBeverages_withValidToken_shouldReturnNonEmptyData() throws Exception {
        // 1. 테스트용 토큰 생성
        String token = TestJwtGenerator.generateToken(TEST_AUTH0_SUB, TEST_MEMBER_ID);

        // 2. API 호출 및 검증
        mockMvc.perform(get("/ping").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void generateToken() {
        String token = TestJwtGenerator.generateToken(TEST_AUTH0_SUB, TEST_MEMBER_ID);
        System.out.println("token = " + token);
    }
}
