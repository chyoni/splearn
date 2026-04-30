package cwchoiit.splearn.member.application.required;

import static cwchoiit.splearn.member.domain.MemberFixture.createMemberRegisterPayload;
import static cwchoiit.splearn.member.domain.MemberFixture.createPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cwchoiit.splearn.member.SplearnTestConfiguration;
import cwchoiit.splearn.member.domain.Member;
import cwchoiit.splearn.member.domain.MemberStatus;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@Import(SplearnTestConfiguration.class)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Autowired EntityManager entityManager;

    @Test
    void createMember() {
        Member member =
                Member.register(
                        createMemberRegisterPayload("noreply@example.com"),
                        createPasswordEncoder());

        assertThat(member.getId()).isNull();

        memberRepository.save(member);
        entityManager.flush();

        assertThat(member.getId()).isNotNull();

        entityManager.clear();

        Member findMember = memberRepository.findById(member.getId()).orElseThrow();
        assertThat(findMember.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(findMember.getDetail().getRegisteredAt()).isNotNull();
    }

    @Test
    void duplicateEmailFail() {
        Member member =
                Member.register(
                        createMemberRegisterPayload("noreply@example.com"),
                        createPasswordEncoder());
        memberRepository.save(member);

        Member duplicateEmailMember =
                Member.register(
                        createMemberRegisterPayload("noreply@example.com"),
                        createPasswordEncoder());

        assertThatThrownBy(() -> memberRepository.save(duplicateEmailMember))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
