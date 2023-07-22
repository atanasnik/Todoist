package bg.sofia.uni.fmi.todoist.exception;

public class CollaborationNotFoundException extends NotFoundException {
    public CollaborationNotFoundException(String message) {
        super(message);
    }

    public CollaborationNotFoundException(String message, Throwable e) {
        super(message, e);
    }
}
