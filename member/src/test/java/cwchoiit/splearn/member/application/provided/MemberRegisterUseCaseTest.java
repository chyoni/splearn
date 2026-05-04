package cwchoiit.splearn.member.application.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cwchoiit.splearn.member.SplearnTestConfiguration;
import cwchoiit.splearn.member.domain.DuplicateEmailException;
import cwchoiit.splearn.member.domain.Member;
import cwchoiit.splearn.member.domain.MemberFixture;
import cwchoiit.splearn.member.domain.MemberStatus;
import cwchoiit.splearn.member.domain.payload.MemberInfoUpdatePayload;
import cwchoiit.splearn.member.domain.payload.MemberRegisterPayload;
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
    void deactivate() {
        Member member =
                memberRegisterUseCase.register(
                        MemberFixture.createMemberRegisterPayload("noreply@example.com"));
        entityManager.flush();
        entityManager.clear();

        member = memberRegisterUseCase.activate(member.getId());

        entityManager.flush();
        entityManager.clear();

        assertThat(member.isActive()).isTrue();

        member = memberRegisterUseCase.deactivate(member.getId());

        entityManager.flush();
        entityManager.clear();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
    }

    @Test
    void updateInfo() {
        Member member =
                memberRegisterUseCase.register(
                        MemberFixture.createMemberRegisterPayload("noreply@example.com"));
        entityManager.flush();
        entityManager.clear();

        member = memberRegisterUseCase.activate(member.getId());

        entityManager.flush();
        entityManager.clear();

        member =
                memberRegisterUseCase.updateInfo(
                        new MemberInfoUpdatePayload("LuLu100", "lulu100", "자기소개"), member.getId());

        assertThat(member.getDetail().getProfile().profile()).isEqualTo("lulu100");
    }

    @Test
    void updateFail() {
        Member member =
                memberRegisterUseCase.register(
                        MemberFixture.createMemberRegisterPayload("noreply@example.com"));
        memberRegisterUseCase.activate(member.getId());

        Member member2 =
                memberRegisterUseCase.register(
                        MemberFixture.createMemberRegisterPayload("noreply2@example.com"));
        memberRegisterUseCase.activate(member2.getId());

        memberRegisterUseCase.updateInfo(
                new MemberInfoUpdatePayload("LuLu100", "lulu100", "자기소개"), member.getId());

        entityManager.flush();
        entityManager.clear();

        // member가 가진 프로필 주소로 member2가 동일하게 변경하려는 경우
        assertThatThrownBy(
                        () ->
                                memberRegisterUseCase.updateInfo(
                                        new MemberInfoUpdatePayload("LuLu100", "lulu100", "자기소개"),
                                        member2.getId()))
                .isInstanceOf(IllegalArgumentException.class);

        // 아무도 사용하지 않는 프로필 주소로 정상 변경하는 경우
        memberRegisterUseCase.updateInfo(
                new MemberInfoUpdatePayload("LuLu100", "lulu1001", "자기소개"), member2.getId());

        // 기존 프로필 주소와 동일한 경우
        memberRegisterUseCase.updateInfo(
                new MemberInfoUpdatePayload("LuLu100", "lulu100", "자기소개"), member.getId());

        // 프로필 주소가 빈 값인 경우
        memberRegisterUseCase.updateInfo(
                new MemberInfoUpdatePayload("LuLu100", "", "자기소개"), member.getId());

        // member2가 가진 프로필 주소로 member가 동일하게 변경하려는 경우
        assertThatThrownBy(
                        () ->
                                memberRegisterUseCase.updateInfo(
                                        new MemberInfoUpdatePayload("LuLu100", "lulu1001", "자기소개"),
                                        member.getId()))
                .isInstanceOf(IllegalArgumentException.class);
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
