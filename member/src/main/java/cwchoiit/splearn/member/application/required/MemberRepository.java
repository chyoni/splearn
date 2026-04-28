package cwchoiit.splearn.member.application.required;

import cwchoiit.splearn.member.domain.Member;
import org.springframework.data.repository.Repository;

/**
 * 어? 포트에서 springframework 의존하면 안되지 않나요?
 * : 아무 문제 없다. Spring Data 의 Repository는 그저 인터페이스일뿐 아무런 기능을 제공하지 않는다.
 *   Spring Data JPA 안쓰면 이거 못사용한다고 생각하는데 그것도 아니다. 그냥 spring-data-common 만 라이브러리로 내려받고 사용해도 된다.
 */
public interface MemberRepository extends Repository<Member, Long> {
    Member save(Member member);
}
