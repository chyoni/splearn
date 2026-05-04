package cwchoiit.splearn.member.domain;

import static cwchoiit.splearn.member.domain.MemberFixture.createMemberRegisterPayload;
import static cwchoiit.splearn.member.domain.MemberFixture.createPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cwchoiit.splearn.member.domain.payload.MemberInfoUpdatePayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberTest {

    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = createPasswordEncoder();
        member =
                Member.register(
                        createMemberRegisterPayload("noreply@example.com"), passwordEncoder);
    }

    @Test
    void registerMember() {
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(member.getDetail().getRegisteredAt()).isNotNull();
    }

    @Test
    void activate() {
        assertThat(member.getDetail().getActivatedAt()).isNull();

        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    void activateFail() {
        member.activate();

        assertThatThrownBy(member::activate).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deactivate() {
        member.activate();

        assertThat(member.getDetail().getDeactivatedAt()).isNull();

        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
    }

    @Test
    void deactivateFail() {
        assertThatThrownBy(member::deactivate).isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(member::deactivate).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void verifyPassword() {
        assertThat(member.verifyPassword("long_password", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("failed", passwordEncoder)).isFalse();
    }

    @Test
    void update() {
        member.activate();

        MemberInfoUpdatePayload updatePayload =
                new MemberInfoUpdatePayload("holy_holo", "holy", "my_intro");
        member.update(updatePayload);

        assertThat(member.getNickname()).isEqualTo(updatePayload.nickname());
        assertThat(member.getDetail().getProfile().profile()).isEqualTo(updatePayload.profile());
        assertThat(member.getDetail().getIntroduction()).isEqualTo(updatePayload.introduction());
    }

    @Test
    void updateFail() {
        MemberInfoUpdatePayload updatePayload =
                new MemberInfoUpdatePayload("holy_holo", "holy", "my_intro");

        assertThatThrownBy(() -> member.update(updatePayload))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void changePassword() {
        member.changePassword("newPw", passwordEncoder);

        assertThat(member.verifyPassword("newPw", passwordEncoder)).isTrue();
    }

    @Test
    void isActive() {
        assertThat(member.isActive()).isFalse();

        member.activate();

        assertThat(member.isActive()).isTrue();

        member.deactivate();

        assertThat(member.isActive()).isFalse();
    }

    @Test
    void invalidEmail() {
        assertThatThrownBy(
                        () ->
                                Member.register(
                                        createMemberRegisterPayload("invalid"), passwordEncoder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
