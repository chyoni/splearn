package cwchoiit.splearn.member.application.required;

import cwchoiit.splearn.member.domain.Member;
import cwchoiit.splearn.member.domain.vo.Email;
import cwchoiit.splearn.member.domain.vo.Profile;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

/** 멤버를 저장하거나 조회한다. */
public interface MemberRepository extends Repository<Member, Long> {
    Member save(Member member);

    Optional<Member> findByEmail(Email email);

    Optional<Member> findById(Long memberId);

    @Query("select m from Member m where m.detail.profile = :profile")
    Optional<Member> findByProfile(Profile profile);
}
