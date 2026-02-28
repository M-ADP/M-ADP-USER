package madp.user.domain.presentation.dto.request;

public record UpdateUserProfileRequestDto(
        String nickname,
        String profile
) {}
