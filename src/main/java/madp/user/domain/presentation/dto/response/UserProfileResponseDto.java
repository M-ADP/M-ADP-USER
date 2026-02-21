package madp.user.domain.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record UserProfileResponseDto(
        Long id,
        String nickname,
        @JsonProperty("github_id")
        String githubId,
        String profile
) {}
