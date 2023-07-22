package bg.sofia.uni.fmi.todoist.exception;

public class WrongPasswordException extends Exception {
    public WrongPasswordException(String message) {
        super(message);
    }

    public WrongPasswordException(String message, Throwable e) {
        super(message, e);
    }
}
