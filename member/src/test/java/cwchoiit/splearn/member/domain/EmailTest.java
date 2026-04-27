package cwchoiit.splearn.member.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTest {

    @Test
    void equality() {
        Email email1 = new Email("cwchoiit@example.com");
        Email email2 = new Email("cwchoiit@example.com");

        assertThat(email1).isEqualTo(email2);
    }
}
