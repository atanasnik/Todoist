package bg.sofia.uni.fmi.todoist.exception;

public class CollaborationAlreadyExistsException extends AlreadyExistsException {
    public CollaborationAlreadyExistsException(String message) {
        super(message);
    }
    public CollaborationAlreadyExistsException(String message, Throwable e) {
        super(message, e);
    }
}
