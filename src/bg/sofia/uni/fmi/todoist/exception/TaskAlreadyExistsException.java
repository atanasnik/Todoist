package bg.sofia.uni.fmi.todoist.exception;

public class TaskAlreadyExistsException extends AlreadyExistsException {
    public TaskAlreadyExistsException(String message) {
        super(message);
    }

    public TaskAlreadyExistsException(String message, Throwable e) {
        super(message, e);
    }
}
