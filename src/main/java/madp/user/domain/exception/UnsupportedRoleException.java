package madp.user.domain.exception;

import madp.user.global.exception.MadpBusinessException;
import org.springframework.http.HttpStatus;

public class UnsupportedRoleException extends MadpBusinessException {
    public UnsupportedRoleException(String role) {
        super(role + "역할은 지원하지 않습니다.", HttpStatus.BAD_REQUEST);
    }
}
