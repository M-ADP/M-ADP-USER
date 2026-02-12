package madp.user.domain.exception;

import madp.user.global.exception.MadpBusinessException;
import org.springframework.http.HttpStatus;

public class UserInactiveException extends MadpBusinessException {
    public UserInactiveException() {
        super("비활성화된 계정입니다.", HttpStatus.BAD_REQUEST);
    }
}
