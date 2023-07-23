package bg.sofia.uni.fmi.todoist.command.tasks;

import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.command.CommandExecutor;
import bg.sofia.uni.fmi.todoist.exception.NotFoundException;
import bg.sofia.uni.fmi.todoist.exception.TaskNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class DeleteTaskCommand extends CommandExecutor {
    private static final String NAME = "name";
    private static final String DATE = "date";
    private static final String COLLABORATION = "collaboration";
    private static final String TASK_NOT_FOUND = "Task not found.";
    private static final String NO_SUCH_COLLABORATION = "There is no collaboration with that name";
    private static final String TASK_DELETING = "Task deleting";

    private static final Map<String, Boolean> DELETE_TASK_ARGS = new HashMap<>() {
        {
            put(NAME, true);
            put(DATE, false);
            put(COLLABORATION, false);
        }
    };

    public DeleteTaskCommand(String username, Command cmd) {
        super(username, cmd);
    }

    @Override
    public String executeCommand() {
        if (cmd.arguments().containsKey(COLLABORATION)) {
            try {
                deleteCollaborationTask();
            } catch (TaskNotFoundException e) {
                return TASK_NOT_FOUND;
            } catch (NotFoundException e) {
                return NO_SUCH_COLLABORATION;
            }
        } else {
            try {
                deleteRegularTask();
            } catch (NotFoundException e) {
                return TASK_NOT_FOUND;
            }
        }

        return String.format(OPERATION_SUCCESSFUL_MESSAGE_FORMAT, TASK_DELETING);
    }

    @Override
    public Map<String, Boolean> getArgs() {
        return DELETE_TASK_ARGS;
    }

    private void deleteCollaborationTask() throws NotFoundException {
        var result = collaborationsStorage().getByIdentifier(cmd().arguments()
                .get(COLLABORATION)).tasks().remove(cmd().arguments().get(NAME));

        if (result == null) {
            throw new TaskNotFoundException("Task not found");
        }
    }

    private void deleteRegularTask() throws NotFoundException {
        if (cmd().arguments().containsKey(DATE)) {
            usersStorage().getByIdentifier(username()).timedTasks()
                    .remove(cmd().arguments().get(NAME), cmd().arguments().get(DATE));
        } else {
            usersStorage().getByIdentifier(username()).inbox()
                    .remove(cmd().arguments().get(NAME), null);
        }
    }
}
