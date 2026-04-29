package cwchoiit.splearn.member;

import cwchoiit.splearn.member.application.required.EmailSender;
import cwchoiit.splearn.member.domain.MemberFixture;
import cwchoiit.splearn.member.domain.PasswordEncoder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SplearnTestConfiguration {
    @Bean
    public EmailSender emailSender() {
        return (email, subject, body) -> System.out.println("Sending email: " + email);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return MemberFixture.createPasswordEncoder();
    }
}
