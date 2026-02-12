package madp.user.domain.presentation.dto.response;

import lombok.Builder;

@Builder
public record UserProfileResponseDto(
        String nickname,
        String profile
) {}
