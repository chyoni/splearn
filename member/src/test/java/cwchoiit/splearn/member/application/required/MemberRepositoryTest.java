package cwchoiit.splearn.member.application.required;

import cwchoiit.splearn.member.domain.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static cwchoiit.splearn.member.domain.MemberFixture.createMemberRegisterPayload;
import static cwchoiit.splearn.member.domain.MemberFixture.createPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager entityManager;

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
    }
}
