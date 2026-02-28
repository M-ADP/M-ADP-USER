package madp.user.global.exception.resource;

import madp.user.global.exception.MadpBusinessException;
import org.springframework.http.HttpStatus;

public class DuplicateException extends MadpBusinessException {
    public DuplicateException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
