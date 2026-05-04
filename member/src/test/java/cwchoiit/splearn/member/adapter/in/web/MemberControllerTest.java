package cwchoiit.splearn.member.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import cwchoiit.splearn.member.application.provided.MemberRegisterUseCase;
import cwchoiit.splearn.member.application.required.MemberRepository;
import cwchoiit.splearn.member.domain.MemberFixture;
import cwchoiit.splearn.member.domain.payload.MemberRegisterPayload;
import cwchoiit.splearn.member.domain.vo.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
class MemberControllerTest {

    @Autowired MemberRegisterUseCase memberRegisterUseCase;
    @Autowired MemberRepository memberRepository;
    @Autowired WebApplicationContext wac;
    @Autowired ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(wac).build();
    }

    @Test
    void register() throws Exception {
        MemberRegisterPayload payload =
                MemberFixture.createMemberRegisterPayload("noreply@example.com");
        String requestJson = objectMapper.writeValueAsString(payload);

        mockMvc.perform(
                        post("/api/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("noreply@example.com"));

        assertThat(memberRepository.findByEmail(new Email("noreply@example.com"))).isPresent();
    }

    @Test
    void registerFail() throws Exception {
        // 잘못된 이메일 검증
        MemberRegisterPayload payload = MemberFixture.createMemberRegisterPayload("noreply");
        String requestJson = objectMapper.writeValueAsString(payload);

        mockMvc.perform(
                        post("/api/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void duplicateEmail() throws Exception {
        MemberRegisterPayload payload =
                MemberFixture.createMemberRegisterPayload("noreply@example.com");
        memberRegisterUseCase.register(payload);

        // 이메일 중복 요청
        String requestJson = objectMapper.writeValueAsString(payload);
        mockMvc.perform(
                        post("/api/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                .andExpect(status().isConflict());
    }
}
