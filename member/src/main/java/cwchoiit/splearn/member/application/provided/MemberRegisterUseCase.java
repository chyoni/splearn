package cwchoiit.splearn.member.application.provided;

import cwchoiit.splearn.member.domain.Member;
import cwchoiit.splearn.member.domain.payload.MemberInfoUpdatePayload;
import cwchoiit.splearn.member.domain.payload.MemberRegisterPayload;
import jakarta.validation.Valid;

/** 회원을 등록한다. */
public interface MemberRegisterUseCase {
    Member register(@Valid MemberRegisterPayload memberRegisterPayload);

    Member activate(Long memberId);

    Member deactivate(Long memberId);

    Member updateInfo(@Valid MemberInfoUpdatePayload memberInfoUpdatePayload, Long memberId);
}
