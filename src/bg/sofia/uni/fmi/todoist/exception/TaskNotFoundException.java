package bg.sofia.uni.fmi.todoist.exception;

public class TaskNotFoundException extends NotFoundException {
    public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException(String message, Throwable e) {
        super(message, e);
    }
}
