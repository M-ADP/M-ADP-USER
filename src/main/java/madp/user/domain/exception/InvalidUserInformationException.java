package madp.user.domain.exception;

import madp.user.global.exception.MadpBusinessException;
import org.springframework.http.HttpStatus;

public class InvalidUserInformationException extends MadpBusinessException {
    public InvalidUserInformationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
