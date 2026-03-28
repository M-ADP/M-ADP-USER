package madp.user.domain.presentation.dto.response;

import lombok.Builder;

@Builder
public record UserSearchResponseDto(
        Long id,
        String nickname,
        String profile
) {}