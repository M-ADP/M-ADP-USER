package madp.user.domain.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record UserAuthResponseDto(
        @JsonProperty("user_id")
        Long userId,
        @JsonProperty("requires_additional_auth")
        Boolean requiresAdditionalAuth
) {}