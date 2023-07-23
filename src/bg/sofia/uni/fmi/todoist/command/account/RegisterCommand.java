package bg.sofia.uni.fmi.todoist.command.account;

import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.command.CommandExecutor;
import bg.sofia.uni.fmi.todoist.exception.UserAlreadyExistsException;

import java.util.HashMap;
import java.util.Map;

public class RegisterCommand extends CommandExecutor {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String FAILED_ACCESS_ATTEMPT = "Failed access attempt. ";
    private static final String USERNAME_EXISTS_MESSAGE_FORMAT =
            "Username \"%s\" already exists. Try another username.";
    private static final String REGISTRATION = "Registration";
    private static final String ALREADY_LOGGED = "You have already logged into an account.";


    public RegisterCommand(String username, Command cmd) {
        super(username, cmd);
    }

    private static final Map<String, Boolean> REGISTER_ARGS = new HashMap<>() {
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
            usersStorage.register(cmd.arguments().get(USERNAME), cmd.arguments().get(PASSWORD));

        } catch (UserAlreadyExistsException e) {
            return FAILED_ACCESS_ATTEMPT +
                    String.format(USERNAME_EXISTS_MESSAGE_FORMAT, cmd.arguments().get(USERNAME));
        }

        return String.format(OPERATION_SUCCESSFUL_MESSAGE_FORMAT, REGISTRATION);
    }

    @Override
    public Map<String, Boolean> getArgs() {
        return REGISTER_ARGS;
    }
}
