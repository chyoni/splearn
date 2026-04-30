package cwchoiit.splearn.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cwchoiit.splearn.member.domain.vo.Profile;
import org.junit.jupiter.api.Test;

class ProfileTest {

    @Test
    void profile() {
        new Profile("cwchoiit");
        new Profile("cwchoiit123");
        new Profile("123123");
    }

    @Test
    void profileFail() {
        assertThatThrownBy(() -> new Profile("")).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(
                        () ->
                                new Profile(
                                        "oooooooooooooooooooooooooooooooooooooooooooooooooooooooo"))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Profile("한글은 안돼요"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void url() {
        Profile cwchoiit = new Profile("cwchoiit");
        assertThat(cwchoiit.url()).isEqualTo("@%s".formatted("cwchoiit"));
    }
}
