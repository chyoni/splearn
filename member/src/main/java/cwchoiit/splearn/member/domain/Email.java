package cwchoiit.splearn.member.domain;

import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
public record Email(String address) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public Email {
        if (!EMAIL_PATTERN.matcher(address).matches()) {
            throw new IllegalArgumentException("Invalid email address");
        }
    }
}
