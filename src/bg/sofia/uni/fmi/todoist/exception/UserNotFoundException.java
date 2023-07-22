package bg.sofia.uni.fmi.todoist.exception;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable e) {
        super(message, e);
    }
}
