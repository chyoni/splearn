package cwchoiit.splearn.member.application;

import cwchoiit.splearn.member.application.provided.MemberLoadUseCase;
import cwchoiit.splearn.member.application.provided.MemberRegisterUseCase;
import cwchoiit.splearn.member.application.required.EmailSender;
import cwchoiit.splearn.member.application.required.MemberRepository;
import cwchoiit.splearn.member.domain.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberModifyService implements MemberRegisterUseCase {

    private final MemberRepository memberRepository;
    private final MemberLoadUseCase memberLoadUseCase;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Member register(@Valid MemberRegisterPayload memberRegisterPayload) {
        checkDuplicateEmail(memberRegisterPayload);

        Member member = Member.register(memberRegisterPayload, passwordEncoder);

        memberRepository.save(member);

        sendWelcomeEmail(member);

        return member;
    }

    @Override
    public Member activate(Long memberId) {
        Member findMember = memberLoadUseCase.find(memberId);

        findMember.activate();

        return memberRepository.save(findMember);
    }

    private void sendWelcomeEmail(Member member) {
        emailSender.send(member.getEmail(), "등록을 완료해주세요", "아래 링크를 클릭해서 등록을 완료해주세요");
    }

    private void checkDuplicateEmail(MemberRegisterPayload memberRegisterPayload) {
        if (memberRepository.findByEmail(new Email(memberRegisterPayload.email())).isPresent()) {
            throw new DuplicateEmailException(
                    "Already exists email: " + memberRegisterPayload.email());
        }
    }
}
