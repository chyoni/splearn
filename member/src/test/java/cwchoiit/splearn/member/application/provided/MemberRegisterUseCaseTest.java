package cwchoiit.splearn.member.application.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cwchoiit.splearn.member.SplearnTestConfiguration;
import cwchoiit.splearn.member.domain.*;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@Import(SplearnTestConfiguration.class)
class MemberRegisterUseCaseTest {

    @Autowired MemberRegisterUseCase memberRegisterUseCase;
    @Autowired EntityManager entityManager;

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

    @Test
    void activate() {
        Member member =
                memberRegisterUseCase.register(
                        MemberFixture.createMemberRegisterPayload("noreply@example.com"));
        entityManager.flush();
        entityManager.clear();

        member = memberRegisterUseCase.activate(member.getId());

        entityManager.flush();

        assertThat(member.isActive()).isTrue();
    }

    @Test
    void memberRegisterPayloadFail() {
        checkValidation(new MemberRegisterPayload("noreply@example.com", "c", "long_password"));
        checkValidation(new MemberRegisterPayload("noreply@example.com", "cwchoiit", "pw"));
        checkValidation(new MemberRegisterPayload("invalid", "cwchoiit", "long_password"));
    }

    private void checkValidation(MemberRegisterPayload invalid) {
        assertThatThrownBy(() -> memberRegisterUseCase.register(invalid))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
