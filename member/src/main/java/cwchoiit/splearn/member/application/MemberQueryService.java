package cwchoiit.splearn.member.application;

import cwchoiit.splearn.member.application.provided.MemberLoadUseCase;
import cwchoiit.splearn.member.application.required.MemberRepository;
import cwchoiit.splearn.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService implements MemberLoadUseCase {

    private final MemberRepository memberRepository;

    @Override
    public Member find(Long memberId) {
        return memberRepository
                .findById(memberId)
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "Couldn't found member with id: " + memberId));
    }
}
