package madp.user.domain.exception;

import madp.user.global.exception.MadpBusinessException;
import org.springframework.http.HttpStatus;

public class RequireGoogleAuthenticationFirstException extends MadpBusinessException {
    public RequireGoogleAuthenticationFirstException() {
        super("Google 로그인을 먼저 시도해주세요.", HttpStatus.BAD_REQUEST);
    }
}
