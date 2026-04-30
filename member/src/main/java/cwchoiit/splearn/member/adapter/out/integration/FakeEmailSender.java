package cwchoiit.splearn.member.adapter.out.integration;

import cwchoiit.splearn.member.application.required.EmailSender;
import cwchoiit.splearn.member.domain.vo.Email;
import org.springframework.stereotype.Component;

@Component
public class FakeEmailSender implements EmailSender {

    @Override
    public void send(Email email, String subject, String body) {
        System.out.println("email = " + email);
    }
}
