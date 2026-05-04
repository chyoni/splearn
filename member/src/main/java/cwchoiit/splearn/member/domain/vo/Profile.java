package cwchoiit.splearn.member.domain.vo;

import java.util.regex.Pattern;

public record Profile(String profile) {
    private static final Pattern PROFILE_ADDRESS_PATTERN = Pattern.compile("[a-z0-9]+");

    public Profile {
        if (profile == null
                || (!profile.isEmpty() && !PROFILE_ADDRESS_PATTERN.matcher(profile).matches())) {
            throw new IllegalArgumentException("Invalid profile address: " + profile);
        }

        if (profile.length() > 15) {
            throw new IllegalArgumentException("Profile address size must be between 0 to 15");
        }
    }

    public String url() {
        return "@%s".formatted(profile);
    }
}
