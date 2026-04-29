package cwchoiit.splearn.member.application.provided;

import cwchoiit.splearn.member.domain.Member;
import cwchoiit.splearn.member.domain.MemberRegisterPayload;
import jakarta.validation.Valid;

/** 회원을 등록한다. */
public interface MemberRegisterUseCase {
    Member register(@Valid MemberRegisterPayload memberRegisterPayload);
}
