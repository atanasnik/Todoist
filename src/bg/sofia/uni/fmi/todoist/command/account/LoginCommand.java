package bg.sofia.uni.fmi.todoist.command.account;

import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.command.CommandExecutor;
import bg.sofia.uni.fmi.todoist.exception.UserNotFoundException;
import bg.sofia.uni.fmi.todoist.exception.WrongPasswordException;

import java.util.HashMap;
import java.util.Map;

public class LoginCommand extends CommandExecutor {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String LOGIN = "Login";
    private static final String FAILED_ACCESS_ATTEMPT = "Failed access attempt. ";
    private static final String INVALID_CREDENTIALS_MESSAGE =
            "You have entered an invalid username and/or password. Try again.";
    private static final String ALREADY_LOGGED = "You have already logged into an account.";


    public LoginCommand(String username, Command cmd) {
        super(username, cmd);
    }

    private static final Map<String, Boolean> LOGIN_ARGS = new HashMap<>() {
        {
            put(USERNAME, true);
            put(PASSWORD, true);
        }
    };

    @Override
    public String executeCommand() {
        if (username != null) {
            return ALREADY_LOGGED;
        }

        try {
            usersStorage.login(cmd.arguments().get(USERNAME), cmd.arguments().get(PASSWORD));
        } catch (UserNotFoundException | WrongPasswordException e) {
            return FAILED_ACCESS_ATTEMPT + INVALID_CREDENTIALS_MESSAGE;
        }

        return String.format(OPERATION_SUCCESSFUL_MESSAGE_FORMAT, LOGIN);
    }

    @Override
    public Map<String, Boolean> getArgs() {
        return LOGIN_ARGS;
    }
}
