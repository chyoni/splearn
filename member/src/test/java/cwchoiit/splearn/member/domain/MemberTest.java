package cwchoiit.splearn.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberTest {

    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder =
                new PasswordEncoder() {
                    @Override
                    public String encode(String password) {
                        return password.toUpperCase();
                    }

                    @Override
                    public boolean matches(String password, String passwordHash) {
                        return encode(password).equals(passwordHash);
                    }
                };

        member =
                Member.register(
                        new MemberRegisterPayload("noreply@example.com", "cwchoiit", "pw"),
                        passwordEncoder);
    }

    @Test
    void registerMember() {
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void activate() {
        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    void activateFail() {
        member.activate();

        assertThatThrownBy(member::activate).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deactivate() {
        member.activate();

        member.deactivate();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
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
        assertThat(member.verifyPassword("pw", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("failed", passwordEncoder)).isFalse();
    }

    @Test
    void changeNickname() {
        assertThat(member.getNickname()).isEqualTo("cwchoiit");

        member.changeNickname("newCwchoiit");

        assertThat(member.getNickname()).isEqualTo("newCwchoiit");
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
                                        new MemberRegisterPayload("invalid", "cwchoiit", "pw"),
                                        passwordEncoder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
