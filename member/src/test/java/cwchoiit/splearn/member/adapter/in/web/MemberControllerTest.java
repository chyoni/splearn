package cwchoiit.splearn.member.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import cwchoiit.splearn.member.application.provided.MemberRegisterUseCase;
import cwchoiit.splearn.member.domain.Member;
import cwchoiit.splearn.member.domain.MemberFixture;
import cwchoiit.splearn.member.domain.payload.MemberRegisterPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
class MemberControllerTest {

    @MockitoBean MemberRegisterUseCase memberRegisterUseCase;

    @Autowired WebApplicationContext wac;

    @Autowired ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(wac).build();
    }

    @Test
    void register() throws Exception {
        Member member = MemberFixture.createMember(1L);
        when(memberRegisterUseCase.register(any())).thenReturn(member);

        MemberRegisterPayload payload =
                MemberFixture.createMemberRegisterPayload("noreply@example.com");
        String requestJson = objectMapper.writeValueAsString(payload);

        mockMvc.perform(
                        post("/api/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(1));

        verify(memberRegisterUseCase, times(1)).register(any());
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
}
