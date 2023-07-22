package bg.sofia.uni.fmi.todoist.exception;

public class InvalidCommandException extends Exception {
    public InvalidCommandException(String message) {
        super(message);
    }
    public InvalidCommandException(String message, Throwable e) {
        super(message, e);
    }
}
