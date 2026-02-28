package madp.user.domain.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import madp.user.global.enums.Role;
import madp.user.domain.exception.InvalidUserInformationException;
import madp.user.global.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class UserEntity extends BaseEntity {
    @Column(name = "mail", nullable = false, unique = true)
    private String mail;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Column(name = "profile")
    private String profile;

    @Column(name = "google_id", nullable = false, unique = true)
    private String googleId;

    @Column(name = "github_id", unique = true)
    private String githubId;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder(builderMethodName = "createFromGoogle")
    public UserEntity(String mail, String googleId) {
        validateFieldsFromGoogle(mail, googleId);

        this.mail = mail;
        this.googleId = googleId;
        this.role = Role.PARTIAL_AUTH;
        this.isActive = true;
    }

    public void updateFromGithub(String nickname, String profile, String githubId) {
        validateFieldsFromGithub(nickname, profile, githubId);

        this.nickname = nickname;
        this.profile = profile;
        this.githubId = githubId;
        this.role = Role.USER;
    }

    public void updateProfile(String nickname, String profile) {
        if(nickname != null && !nickname.isEmpty() && !nickname.equals(this.nickname)){
            this.nickname = nickname;
        }
        if(profile != null && !profile.isEmpty() && !profile.equals(this.profile)){
            this.profile = profile;
        }
    }

    public void deactivate() {
        this.isActive = false;
    }

    private void validateFieldsFromGoogle(String mail, String googleId) {
        if(mail == null || mail.isEmpty())
            throw new InvalidUserInformationException("이메일은 필수값입니다.");
        if(googleId == null || googleId.isEmpty())
            throw new InvalidUserInformationException("구글 아이디는 필수값입니다.");
    }

    private void validateFieldsFromGithub(String nickname, String profile, String githubId) {
        if(nickname == null || nickname.isEmpty())
            throw new InvalidUserInformationException("닉네임은 필수값입니다.");
        if(profile == null || profile.isEmpty())
            throw new InvalidUserInformationException("프로필은 필수값입니다.");
        if(githubId == null || githubId.isEmpty())
            throw new InvalidUserInformationException("깃허브 아이디는 필수값입니다.");
    }
}
