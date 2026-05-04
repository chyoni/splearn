package cwchoiit.splearn.member.domain;

import cwchoiit.splearn.member.domain.payload.MemberRegisterPayload;
import org.jspecify.annotations.NonNull;
import org.springframework.test.util.ReflectionTestUtils;

public class MemberFixture {

    public static @NonNull MemberRegisterPayload createMemberRegisterPayload(String email) {
        return new MemberRegisterPayload(email, "cwchoiit", "long_password");
    }

    public static @NonNull Member createMember(Long id) {
        MemberRegisterPayload payload =
                new MemberRegisterPayload("noreply@example.com", "cwchoiit", "long_password");
        Member member = Member.register(payload, createPasswordEncoder());
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }

    public static @NonNull PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
    }
}
