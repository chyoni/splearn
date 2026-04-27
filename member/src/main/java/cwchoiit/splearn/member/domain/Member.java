package cwchoiit.splearn.member.domain;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Member {
    private Email email;
    private String nickname;
    private String passwordHash;
    private MemberStatus status;

    private Member() {}

    public static Member create(MemberCreateVo memberCreateVo, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.email = new Email(requireNonNull(memberCreateVo.email()));
        member.nickname = requireNonNull(memberCreateVo.nickname());
        member.passwordHash = requireNonNull(passwordEncoder.encode(memberCreateVo.password()));
        member.status = MemberStatus.PENDING;

        return member;
    }

    public void activate() {
        state(this.status == MemberStatus.PENDING, "Member is already active.");

        this.status = MemberStatus.ACTIVE;
    }

    public void deactivate() {
        state(this.status == MemberStatus.ACTIVE, "Member is already deactivated.");

        this.status = MemberStatus.DEACTIVATED;
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.passwordHash);
    }

    public void changeNickname(String nickname) {
        this.nickname = requireNonNull(nickname);
    }

    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(requireNonNull(password));
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }
}
