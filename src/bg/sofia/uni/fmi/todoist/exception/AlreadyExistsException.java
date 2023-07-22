package bg.sofia.uni.fmi.todoist.exception;

public class AlreadyExistsException extends Exception {
    public AlreadyExistsException(String message) {
        super(message);
    }
    public AlreadyExistsException(String message, Throwable e) {
        super(message, e);
    }
}
