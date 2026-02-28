package madp.user.domain.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OAuth2Type {
    GOOGLE("google"),
    GITHUB("github");

    private final String value;
}