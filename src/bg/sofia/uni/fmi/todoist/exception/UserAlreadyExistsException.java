package bg.sofia.uni.fmi.todoist.exception;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, Throwable e) {
        super(message, e);
    }
}
