package bg.sofia.uni.fmi.todoist.command;

import bg.sofia.uni.fmi.todoist.exception.InvalidCommandException;
import bg.sofia.uni.fmi.todoist.exception.UserAlreadyExistsException;
import bg.sofia.uni.fmi.todoist.exception.UserNotFoundException;
import bg.sofia.uni.fmi.todoist.exception.WrongPasswordException;

public class AccountExecutor extends CommandExecutor {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String CMD_REGISTER = "register";
    private static final String CMD_LOGIN = "login";
    private static final String REGISTRATION = "Registration";
    private static final String LOGIN = "Login";
    private static final String ALREADY_LOGGED = "You have already logged into an account.";
    private static final String FAILED_ACCESS_ATTEMPT = "Failed access attempt. ";
    private static final String USERNAME_EXISTS_MESSAGE_FORMAT =
            "Username \"%s\" already exists. Try another username.";
    private static final String INVALID_CREDENTIALS_MESSAGE =
            "You have entered an invalid username and/or password. Try again.";
    private static final String INVALID_PARAMETERS_MESSAGE =
            "You have entered an invalid parameter for your username and/or password. Try again.";

    public AccountExecutor(String username, Command cmd) {
        super(username, cmd);
    }

    @Override
    public String executeCommand() {
        if (username != null) {
            return ALREADY_LOGGED;
        }

        try {
            ArgumentChecker.assureArgsMatch(cmd, ArgumentChecker.getLoginRegisterArgs());
        } catch (InvalidCommandException e) {
            // e.printStackTrace();
            return FAILED_ACCESS_ATTEMPT + INVALID_PARAMETERS_MESSAGE;
        }

        return switch (cmd.command()) {
            case CMD_REGISTER -> register();
            case CMD_LOGIN -> login();
            default -> "";
        };
    }

    private String register() {
        try {
            usersStorage.register(cmd.arguments().get(USERNAME), cmd.arguments().get(PASSWORD));

        } catch (UserAlreadyExistsException e) {
            //e.printStackTrace();

            return FAILED_ACCESS_ATTEMPT +
                    String.format(USERNAME_EXISTS_MESSAGE_FORMAT, cmd.arguments().get(USERNAME));
        }

        return String.format(OPERATION_SUCCESSFUL_MESSAGE_FORMAT, REGISTRATION);
    }

    private String login() {
        try {
            usersStorage.login(cmd.arguments().get(USERNAME), cmd.arguments().get(PASSWORD));
        } catch (UserNotFoundException | WrongPasswordException e) {
            //e.printStackTrace();

            return FAILED_ACCESS_ATTEMPT + INVALID_CREDENTIALS_MESSAGE;
        }

        return String.format(OPERATION_SUCCESSFUL_MESSAGE_FORMAT, LOGIN);
    }
}
