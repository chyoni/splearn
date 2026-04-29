package cwchoiit.splearn.member.application;

import cwchoiit.splearn.member.application.provided.MemberRegisterUseCase;
import cwchoiit.splearn.member.application.required.EmailSender;
import cwchoiit.splearn.member.application.required.MemberRepository;
import cwchoiit.splearn.member.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberRegisterUseCase {

    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberRegisterPayload memberRegisterPayload) {
        checkDuplicateEmail(memberRegisterPayload);

        Member member = Member.register(memberRegisterPayload, passwordEncoder);

        memberRepository.save(member);

        sendWelcomeEmail(member);

        return member;
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
