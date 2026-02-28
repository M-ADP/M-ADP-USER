package madp.user.domain.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import madp.user.domain.domain.enums.OAuth2Type;

public record OAuth2UserInformationRequestDto(
        @NotNull(message = "생성자 아이디는 필수값입니다")
        String providerId,
        @NotNull(message = "이메일은 필수값입니다")
        String mail,
        @NotNull(message = "프로필은 필수값입니다")
        String profile,
        @NotNull(message = "이름은 필수값입니다")
        String name,
        @NotNull(message = "OAuth2 타입은 필수값입니다")
        OAuth2Type oAuth2Type
) { }