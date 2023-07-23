package bg.sofia.uni.fmi.todoist.command.collaborations;

import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.command.CommandExecutor;
import bg.sofia.uni.fmi.todoist.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;

public class AddUserCommand extends CommandExecutor {
    private static final String COLLABORATION = "collaboration";
    private static final String USER = "user";
    private static final String NO_SUCH_COLLABORATION = "There is no collaboration with that name";
    private static final String USER_ADDING = "User adding";
    private static final String NO_SUCH_USER = "There is no such user";

    public AddUserCommand(String username, Command cmd) {
        super(username, cmd);
    }

    private static final Map<String, Boolean> ADD_USER_COLLABORATION_ARGS = new HashMap<>() {
        {
            put(COLLABORATION, true);
            put(USER, true);
        }
    };

    @Override
    public String executeCommand() {
        if (usersStorage.getByIdentifier(cmd.arguments().get(USER)) == null) {
            return NO_SUCH_USER;
        }

        try {
            collaborationsStorage.getByIdentifier(cmd.arguments().get(COLLABORATION))
                    .collaborators().add(cmd.arguments().get(USER));
        } catch (NotFoundException e) {
            return NO_SUCH_COLLABORATION;
        }

        return String.format(OPERATION_SUCCESSFUL_MESSAGE_FORMAT, USER_ADDING);
    }

    @Override
    public Map<String, Boolean> getArgs() {
        return ADD_USER_COLLABORATION_ARGS;
    }
}
