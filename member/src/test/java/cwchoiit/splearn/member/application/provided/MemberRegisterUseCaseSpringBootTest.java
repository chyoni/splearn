package cwchoiit.splearn.member.application.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cwchoiit.splearn.member.SplearnTestConfiguration;
import cwchoiit.splearn.member.domain.DuplicateEmailException;
import cwchoiit.splearn.member.domain.Member;
import cwchoiit.splearn.member.domain.MemberFixture;
import cwchoiit.splearn.member.domain.MemberStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@Import(SplearnTestConfiguration.class)
class MemberRegisterUseCaseSpringBootTest {

    @Autowired MemberRegisterUseCase memberRegisterUseCase;

    @Test
    void register() {
        Member member =
                memberRegisterUseCase.register(
                        MemberFixture.createMemberRegisterPayload("noreply@example.com"));
        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmailFail() {
        memberRegisterUseCase.register(
                MemberFixture.createMemberRegisterPayload("noreply@example.com"));

        assertThatThrownBy(
                        () ->
                                memberRegisterUseCase.register(
                                        MemberFixture.createMemberRegisterPayload(
                                                "noreply@example.com")))
                .isInstanceOf(DuplicateEmailException.class);
    }
}
