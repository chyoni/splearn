package cwchoiit.splearn.member.application.required;

import cwchoiit.splearn.member.domain.vo.Email;

/** 이메일을 발송한다. */
public interface EmailSender {
    void send(Email email, String subject, String body);
}
