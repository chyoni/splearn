package cwchoiit.splearn.member.adapter.in.web.response;

import cwchoiit.splearn.member.domain.Member;

public record MemberRegisterResponse(Long memberId, String email) {

    public static MemberRegisterResponse of(Member member) {
        return new MemberRegisterResponse(member.getId(), member.getEmail().email());
    }
}
