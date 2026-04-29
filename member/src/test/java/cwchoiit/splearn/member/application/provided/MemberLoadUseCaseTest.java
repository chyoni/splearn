package cwchoiit.splearn.member.application.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cwchoiit.splearn.member.SplearnTestConfiguration;
import cwchoiit.splearn.member.domain.Member;
import cwchoiit.splearn.member.domain.MemberFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@Import(SplearnTestConfiguration.class)
class MemberLoadUseCaseTest {

    @Autowired MemberLoadUseCase memberLoadUseCase;
    @Autowired MemberRegisterUseCase memberRegisterUseCase;
    @Autowired EntityManager entityManager;

    @Test
    void find() {
        Member member =
                memberRegisterUseCase.register(
                        MemberFixture.createMemberRegisterPayload("noreply@example.com"));
        entityManager.flush();
        entityManager.clear();

        Member findMember = memberLoadUseCase.find(member.getId());

        assertThat(findMember.getId()).isEqualTo(member.getId());
    }

    @Test
    void findFail() {
        assertThatThrownBy(() -> memberLoadUseCase.find(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
