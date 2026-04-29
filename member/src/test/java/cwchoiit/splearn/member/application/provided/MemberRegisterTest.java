package cwchoiit.splearn.member.application.provided;

import cwchoiit.splearn.member.application.MemberService;
import cwchoiit.splearn.member.application.required.EmailSender;
import cwchoiit.splearn.member.application.required.MemberRepository;
import cwchoiit.splearn.member.domain.Email;
import cwchoiit.splearn.member.domain.Member;
import cwchoiit.splearn.member.domain.MemberFixture;
import cwchoiit.splearn.member.domain.MemberStatus;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MemberRegisterTest {
    @Test
    void registerTestWithStub() {
        MemberRegister memberRegister =
                new MemberService(
                        new MemberRepositoryStub(),
                        new EmailSenderStub(),
                        MemberFixture.createPasswordEncoder());

        Member member =
                memberRegister.register(
                        MemberFixture.createMemberRegisterPayload("noreply@example.com"));

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void registerTestWithMock() {
        EmailSenderMock emailSenderMock = new EmailSenderMock();
        MemberRegister memberRegister =
                new MemberService(
                        new MemberRepositoryStub(),
                        emailSenderMock,
                        MemberFixture.createPasswordEncoder());

        Member member =
                memberRegister.register(
                        MemberFixture.createMemberRegisterPayload("noreply@example.com"));

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(emailSenderMock.emails).hasSize(1);
        assertThat(emailSenderMock.emails.getFirst()).isEqualTo(member.getEmail());
    }

    @Test
    void registerTestWithMockito() {
        EmailSender emailSenderMock = mock(EmailSender.class);
        MemberRegister memberRegister =
                new MemberService(
                        new MemberRepositoryStub(),
                        emailSenderMock,
                        MemberFixture.createPasswordEncoder());

        Member member =
                memberRegister.register(
                        MemberFixture.createMemberRegisterPayload("noreply@example.com"));

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        verify(emailSenderMock).send(eq(member.getEmail()), any(), any());
    }

    static class MemberRepositoryStub implements MemberRepository {
        @Override
        public Member save(Member member) {
            ReflectionTestUtils.setField(member, "id", 1L);
            return member;
        }
    }

    static class EmailSenderStub implements EmailSender {
        @Override
        public void send(Email email, String subject, String body) {
        }
    }

    static class EmailSenderMock implements EmailSender {

        List<Email> emails = new ArrayList<>();

        @Override
        public void send(Email email, String subject, String body) {
            emails.add(email);
        }
    }
}
