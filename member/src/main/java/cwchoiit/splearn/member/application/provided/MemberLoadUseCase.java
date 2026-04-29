package cwchoiit.splearn.member.application.provided;

import cwchoiit.splearn.member.domain.Member;

/** 회원을 조회한다. */
public interface MemberLoadUseCase {
    Member find(Long memberId);
}
