package bg.sofia.uni.fmi.todoist.command.collaborations;

import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.command.CommandExecutor;
import bg.sofia.uni.fmi.todoist.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;

public class DeleteCollaborationCommand extends CommandExecutor {
    private static final String NAME = "name";
    private static final String NO_SUCH_COLLABORATION = "There is no collaboration with that name";
    private static final String UNAUTHORIZED_OPERATION = "This operation can be only performed" +
            " by the owner of the collaboration";
    private static final String COLLABORATION_REMOVAL = "Collaboration removal";

    private static final Map<String, Boolean> DELETE_COLLABORATION_ARGS = new HashMap<>() {
        {
            put(NAME, true);
        }
    };

    public DeleteCollaborationCommand(String username, Command cmd) {
        super(username, cmd);
    }

    @Override
    public String executeCommand() {
        try {
            if (!collaborationsStorage.getByIdentifier(cmd.arguments().get(NAME)).owner().equals(username)) {
                return UNAUTHORIZED_OPERATION;
            }

            collaborationsStorage.remove(cmd.arguments().get(NAME));

        } catch (NotFoundException e) {
            return NO_SUCH_COLLABORATION;
        }

        return String.format(OPERATION_SUCCESSFUL_MESSAGE_FORMAT, COLLABORATION_REMOVAL);
    }

    @Override
    public Map<String, Boolean> getArgs() {
        return DELETE_COLLABORATION_ARGS;
    }
}
