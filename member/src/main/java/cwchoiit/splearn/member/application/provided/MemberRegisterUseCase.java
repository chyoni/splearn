package cwchoiit.splearn.member.application.provided;

import cwchoiit.splearn.member.domain.Member;
import cwchoiit.splearn.member.domain.MemberRegisterPayload;

/** 회원을 등록한다. */
public interface MemberRegisterUseCase {
    Member register(MemberRegisterPayload memberRegisterPayload);
}
