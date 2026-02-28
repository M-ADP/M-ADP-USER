package madp.user.domain.exception;

import madp.user.global.exception.MadpBusinessException;
import org.springframework.http.HttpStatus;

public class InvalidRoleException extends MadpBusinessException {
    public InvalidRoleException(String roleString) {
        super(roleString + "역할은 지원하지 않습니다.", HttpStatus.BAD_REQUEST);
    }
}