package madp.user.domain.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

public record DeactivateUserRequestDto(
        @NotNull(message = "사용자 아이디는 필수값입니다.")
        Long userId
) {}