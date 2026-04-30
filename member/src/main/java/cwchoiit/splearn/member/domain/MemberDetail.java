package cwchoiit.splearn.member.domain;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.isTrue;

import cwchoiit.splearn.member.domain.payload.MemberInfoUpdatePayload;
import cwchoiit.splearn.member.domain.vo.Profile;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail extends BaseEntity {
    private Profile profile;

    private String introduction;

    private LocalDateTime registeredAt;

    private LocalDateTime activatedAt;

    private LocalDateTime deactivatedAt;

    protected static MemberDetail create() {
        MemberDetail memberDetail = new MemberDetail();
        memberDetail.registeredAt = LocalDateTime.now();
        return memberDetail;
    }

    protected void activate() {
        isTrue(activatedAt == null, "Already set on activatedAt");

        this.activatedAt = LocalDateTime.now();
    }

    protected void deactivate() {
        isTrue(deactivatedAt == null, "Already set on deactivatedAt");

        this.deactivatedAt = LocalDateTime.now();
    }

    public void update(MemberInfoUpdatePayload updatePayload) {
        this.profile = new Profile(updatePayload.profile());
        this.introduction = requireNonNull(updatePayload.introduction());
    }
}
