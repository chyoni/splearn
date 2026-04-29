package cwchoiit.splearn.member.application;

import cwchoiit.splearn.member.application.provided.MemberRegister;
import cwchoiit.splearn.member.application.required.EmailSender;
import cwchoiit.splearn.member.application.required.MemberRepository;
import cwchoiit.splearn.member.domain.Member;
import cwchoiit.splearn.member.domain.MemberRegisterPayload;
import cwchoiit.splearn.member.domain.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberRegister {

    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberRegisterPayload memberRegisterPayload) {
        Member member = Member.register(memberRegisterPayload, passwordEncoder);

        memberRepository.save(member);

        emailSender.send(member.getEmail(), "등록을 완료해주세요", "아래 링크를 클릭해서 등록을 완료해주세요");

        return member;
    }
}
