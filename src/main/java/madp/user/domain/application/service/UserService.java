package madp.user.domain.application.service;

import lombok.RequiredArgsConstructor;
import madp.user.domain.domain.entity.UserEntity;
import madp.user.domain.domain.enums.OAuth2Type;
import madp.user.global.enums.Role;
import madp.user.domain.domain.repository.UserRepository;
import madp.user.domain.exception.*;
import madp.user.domain.presentation.dto.request.OAuth2UserInformationRequestDto;
import madp.user.domain.presentation.dto.request.UpdateUserProfileRequestDto;
import madp.user.domain.presentation.dto.response.UserAuthResponseDto;
import madp.user.domain.presentation.dto.response.UserProfileResponseDto;
import madp.user.global.annotation.Trace;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    @Trace
    public UserAuthResponseDto getUserAuthStatus(Long userId, OAuth2UserInformationRequestDto request) {
        if (userId == null) {
            return handleUserIdAbsentLogin(request);
        }
        
        return handleExistingUserLogin(userId, request);
    }
    
    private UserAuthResponseDto handleUserIdAbsentLogin(OAuth2UserInformationRequestDto request) {
        if (request.oAuth2Type() == OAuth2Type.GOOGLE) {
            return handleGoogleLogin(request);
        }
        
        if (request.oAuth2Type() == OAuth2Type.GITHUB) {
            throw new RequireGoogleAuthenticationFirstException();
        }
        
        throw new UnsupportedOAuth2ProviderException(request.oAuth2Type().name());
    }
    
    private UserAuthResponseDto handleGoogleLogin(OAuth2UserInformationRequestDto request) {
        return userRepository.findByGoogleId(request.providerId())
                .map(this::createResponseForExistingUser)
                .orElseGet(() -> createGoogleUser(request));
    }
    
    private UserAuthResponseDto createResponseForExistingUser(UserEntity user) {
        return switch (user.getRole()) {
            case PARTIAL_AUTH -> UserAuthResponseDto.builder()
                    .userId(user.getId())
                    .requiresAdditionalAuth(true)
                    .build();
            case USER, ADMIN -> UserAuthResponseDto.builder()
                    .userId(user.getId())
                    .requiresAdditionalAuth(false)
                    .build();
            default -> throw new UnsupportedRoleException(user.getRole().name());
        };
    }
    
    private UserAuthResponseDto handleExistingUserLogin(Long userId, OAuth2UserInformationRequestDto request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if(!user.getIsActive()) {
            throw new UserInactiveException();
        }

        if (user.getRole() == Role.USER || user.getRole() == Role.ADMIN) {
            return UserAuthResponseDto.builder()
                    .userId(user.getId())
                    .requiresAdditionalAuth(false)
                    .build();
        }
        
        if (user.getRole() == Role.PARTIAL_AUTH) {
            return handlePartialAuthUser(user, request);
        }

        throw new UnsupportedRoleException(user.getRole().name());
    }
    
    private UserAuthResponseDto handlePartialAuthUser(UserEntity user, OAuth2UserInformationRequestDto request) {
        if (request.oAuth2Type() == OAuth2Type.GOOGLE) {
            return UserAuthResponseDto.builder()
                    .userId(user.getId())
                    .requiresAdditionalAuth(true)
                    .build();
        }
        
        if (request.oAuth2Type() == OAuth2Type.GITHUB) {
            return completeGithubAuthentication(user, request);
        }
        
        throw new UnsupportedOAuth2ProviderException(request.oAuth2Type().name());
    }
    
    private UserAuthResponseDto createGoogleUser(OAuth2UserInformationRequestDto request) {
        UserEntity newUser = UserEntity.createFromGoogle()
                .mail(request.mail())
                .googleId(request.providerId())
                .build();
        UserEntity savedUser = userRepository.save(newUser);
        return UserAuthResponseDto.builder()
                .userId(savedUser.getId())
                .requiresAdditionalAuth(true)
                .build();
    }
    
    private UserAuthResponseDto completeGithubAuthentication(UserEntity user, OAuth2UserInformationRequestDto request) {
        user.updateFromGithub(
                request.providerId(),
                request.profile(),
                request.providerId()
        );
        return UserAuthResponseDto.builder()
                .userId(user.getId())
                .requiresAdditionalAuth(false)
                .build();
    }

    @Transactional(readOnly = true)
    public UserProfileResponseDto getMyUserProfile(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return UserProfileResponseDto.builder()
                .nickname(userEntity.getNickname())
                .githubId(userEntity.getGithubId())
                .profile(userEntity.getProfile())
                .build();
    }

    @Transactional(readOnly = true)
    public UserProfileResponseDto getUserProfileByNickname(String nickname) {
        UserEntity userEntity = userRepository.findByNickname(nickname).orElseThrow(UserNotFoundException::new);
        return UserProfileResponseDto.builder()
                .id(userEntity.getId())
                .nickname(userEntity.getNickname())
                .githubId(userEntity.getGithubId())
                .profile(userEntity.getProfile())
                .build();
    }

    @Transactional(readOnly = true)
    public UserProfileResponseDto getUserProfile(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return UserProfileResponseDto.builder()
                .id(userEntity.getId())
                .nickname(userEntity.getNickname())
                .githubId(userEntity.getGithubId())
                .profile(userEntity.getProfile())
                .build();
    }

    @Transactional
    public void updateUserProfile(Long userId, UpdateUserProfileRequestDto updateUserProfileRequestDto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        user.updateProfile(updateUserProfileRequestDto.nickname(),  updateUserProfileRequestDto.profile());
    }

    @Transactional
    public void withdrawUser(Long userId) {
        userRepository.deleteById(userId);
    }
    
    @Transactional
    public void deactivateUserByAdmin(Long userId) {
        UserEntity targetUser = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        targetUser.deactivate();
    }
}
