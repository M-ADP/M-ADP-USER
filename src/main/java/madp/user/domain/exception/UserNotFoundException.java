package madp.user.domain.exception;

import madp.user.global.exception.resource.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException() {
        super("사용자가 존재하지 않습니다.");
    }
}
