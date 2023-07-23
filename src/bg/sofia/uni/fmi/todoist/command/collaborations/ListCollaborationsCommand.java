package bg.sofia.uni.fmi.todoist.command.collaborations;

import bg.sofia.uni.fmi.todoist.collaboration.Collaboration;
import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.command.CommandExecutor;

import java.util.Map;
import java.util.stream.Collectors;

public class ListCollaborationsCommand extends CommandExecutor {
    private static final String NO_COLLABORATIONS_FOUND = "No collaborations found";

    public ListCollaborationsCommand(String username, Command cmd) {
        super(username, cmd);
    }

    @Override
    public String executeCommand() {
        var listed = collaborationsStorage.list();

        if (listed.isEmpty()) {
            return NO_COLLABORATIONS_FOUND;
        }

        return listed
                .stream()
                .filter(collaboration -> collaboration.collaborators().contains(username))
                .map(Collaboration::toString).sorted().collect(Collectors.joining(System.lineSeparator()));
    }

    @Override
    public Map<String, Boolean> getArgs() {
        return null;
    }
}
