package bg.sofia.uni.fmi.todoist.command.collaborations;

import bg.sofia.uni.fmi.todoist.collaboration.Collaboration;
import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.command.CommandExecutor;
import bg.sofia.uni.fmi.todoist.exception.AlreadyExistsException;

import java.util.HashMap;
import java.util.Map;

public class AddCollaborationCommand extends CommandExecutor {
    private static final String NAME = "name";
    private static final String ALREADY_EXISTS = "Collaboration already exists.";

    private static final String COLLABORATION_ADDING = "Collaboration adding";

    public AddCollaborationCommand(String username, Command cmd) {
        super(username, cmd);
    }

    private static final Map<String, Boolean> ADD_COLLABORATION_ARGS = new HashMap<>() {
        {
            put(NAME, true);
        }
    };

    @Override
    public String executeCommand() {
        Collaboration collaboration = new Collaboration(cmd.arguments().get(NAME), username);

        collaboration.collaborators().add(username);

        try {
            collaborationsStorage.add(collaboration.name(), collaboration);
        } catch (AlreadyExistsException e) {
            return ALREADY_EXISTS;
        }

        return String.format(OPERATION_SUCCESSFUL_MESSAGE_FORMAT, COLLABORATION_ADDING);
    }

    @Override
    public Map<String, Boolean> getArgs() {
        return ADD_COLLABORATION_ARGS;
    }
}
