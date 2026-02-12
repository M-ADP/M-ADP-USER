package madp.user.domain.presentation.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import madp.user.domain.application.service.UserService;
import madp.user.domain.presentation.dto.request.DeactivateUserRequestDto;
import madp.user.domain.presentation.dto.request.OAuth2UserInformationRequestDto;
import madp.user.domain.presentation.dto.request.UpdateUserProfileRequestDto;
import madp.user.domain.presentation.dto.response.UserAuthResponseDto;
import madp.user.domain.presentation.dto.response.UserProfileResponseDto;
import madp.user.global.annotation.Trace;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user/auth-status")
    @Trace
    public ResponseEntity<UserAuthResponseDto> getUserAuthStatus(@AuthenticationPrincipal Long userId, @RequestBody @Valid OAuth2UserInformationRequestDto oAuth2UserInformationRequestDto) {
        return ResponseEntity.ok(userService.getUserAuthStatus(userId, oAuth2UserInformationRequestDto));
    }

    @GetMapping("/user/profile")
    public ResponseEntity<UserProfileResponseDto> getMyUserProfile(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(userService.getMyUserProfile(userId));
    }

    @GetMapping("/user/profile/{nickname}")
    public ResponseEntity<UserProfileResponseDto> getUserProfileByNickname(@PathVariable @NotNull(message = "nickname은 필수값입니다.") String nickname) {
        return ResponseEntity.ok(userService.getUserProfileByNickname(nickname));
    }

    @PatchMapping("/user/profile")
    public ResponseEntity<Void> updateUserProfile(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid UpdateUserProfileRequestDto updateUserProfileRequestDto
    ) {
        userService.updateUserProfile(userId, updateUserProfileRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/withdraw")
    public ResponseEntity<Void> withdrawUser(@AuthenticationPrincipal Long userId) {
        userService.withdrawUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/user/deactivate")
    public ResponseEntity<Void> deactivateUserByAdmin(@RequestBody @Valid DeactivateUserRequestDto deactivateUserRequestDto) {
        userService.deactivateUserByAdmin(deactivateUserRequestDto.userId());
        return ResponseEntity.noContent().build();
    }
}
