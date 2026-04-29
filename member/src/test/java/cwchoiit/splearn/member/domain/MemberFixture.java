package cwchoiit.splearn.member.domain;

import org.jspecify.annotations.NonNull;

public class MemberFixture {

    public static @NonNull MemberRegisterPayload createMemberRegisterPayload(String email) {
        return new MemberRegisterPayload(email, "cwchoiit", "long_password");
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
