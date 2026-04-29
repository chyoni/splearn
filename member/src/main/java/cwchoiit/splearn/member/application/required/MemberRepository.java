package cwchoiit.splearn.member.application.required;

import cwchoiit.splearn.member.domain.Email;
import cwchoiit.splearn.member.domain.Member;
import java.util.Optional;
import org.springframework.data.repository.Repository;

/** 멤버를 저장하거나 조회한다. */
public interface MemberRepository extends Repository<Member, Long> {
    Member save(Member member);

    Optional<Member> findByEmail(Email email);
}
