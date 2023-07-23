package bg.sofia.uni.fmi.todoist.command.collaborations;

import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.command.CommandExecutor;
import bg.sofia.uni.fmi.todoist.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ListUsersCommand extends CommandExecutor {
    private static final String COLLABORATION = "collaboration";
    private static final String NO_SUCH_COLLABORATION = "There is no collaboration with that name";

    private static final Map<String, Boolean> LIST_USERS_COLLABORATION_ARGS = new HashMap<>() {
        {
            put(COLLABORATION, true);
        }
    };

    public ListUsersCommand(String username, Command cmd) {
        super(username, cmd);
    }

    @Override
    public String executeCommand() {
        String users;

        try {
            var set = collaborationsStorage.getByIdentifier(cmd.arguments()
                    .get(COLLABORATION)).collaborators();

            users = set.stream().sorted().collect(Collectors.joining(System.lineSeparator()));
        } catch (NotFoundException e) {
            return NO_SUCH_COLLABORATION;
        }

        return users + System.lineSeparator();
    }

    @Override
    public Map<String, Boolean> getArgs() {
        return LIST_USERS_COLLABORATION_ARGS;
    }
}
