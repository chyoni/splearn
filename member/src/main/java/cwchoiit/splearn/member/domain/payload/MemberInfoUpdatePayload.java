package cwchoiit.splearn.member.domain.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MemberInfoUpdatePayload(
        @Size(min = 5, max = 20) String nickname,
        @NotNull @Size(max = 15) String profile,
        @NotNull String introduction) {}
