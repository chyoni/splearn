package cwchoiit.splearn.member.domain;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;

import cwchoiit.splearn.member.domain.payload.MemberInfoUpdatePayload;
import cwchoiit.splearn.member.domain.payload.MemberRegisterPayload;
import cwchoiit.splearn.member.domain.vo.Email;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

@Entity
@Getter
@ToString(callSuper = true, exclude = "detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @NaturalId private Email email;

    private String nickname;

    private String passwordHash;

    private MemberStatus status;

    private MemberDetail detail;

    public static Member register(
            MemberRegisterPayload registerPayload, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.email = new Email(requireNonNull(registerPayload.email()));
        member.nickname = requireNonNull(registerPayload.nickname());
        member.passwordHash = requireNonNull(passwordEncoder.encode(registerPayload.password()));
        member.status = MemberStatus.PENDING;
        member.detail = MemberDetail.create();

        return member;
    }

    public void activate() {
        state(this.status == MemberStatus.PENDING, "Member is already active.");

        this.status = MemberStatus.ACTIVE;
        this.detail.activate();
    }

    public void deactivate() {
        state(this.status == MemberStatus.ACTIVE, "Member is already deactivated.");

        this.status = MemberStatus.DEACTIVATED;
        this.detail.deactivate();
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.passwordHash);
    }

    public void changeNickname(String nickname) {
        this.nickname = requireNonNull(nickname);
    }

    public void update(MemberInfoUpdatePayload updatePayload) {
        this.nickname = requireNonNull(updatePayload.nickname());
        this.detail.update(updatePayload);
    }

    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(requireNonNull(password));
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }
}
