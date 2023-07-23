package bg.sofia.uni.fmi.todoist.command.tasks;

import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.command.CommandExecutor;
import bg.sofia.uni.fmi.todoist.exception.NotFoundException;
import bg.sofia.uni.fmi.todoist.task.CollaborationTask;
import bg.sofia.uni.fmi.todoist.task.Task;

import java.util.HashMap;
import java.util.Map;

public class GetTaskCommand extends CommandExecutor {
    private static final String NAME = "name";
    private static final String DATE = "date";
    private static final String COLLABORATION = "collaboration";
    private static final String NO_SUCH_COLLABORATION = "There is no collaboration with that name";
    private static final String TASK_NOT_FOUND = "Task not found.";

    private static final Map<String, Boolean> GET_TASK_ARGS = new HashMap<>() {
        {
            put(NAME, true);
            put(DATE, false);
            put(COLLABORATION, false);
        }
    };

    public GetTaskCommand(String username, Command cmd) {
        super(username, cmd);
    }

    @Override
    public String executeCommand() {
        if (cmd.arguments().containsKey(COLLABORATION)) {
            try {
                return getCollaborationTask();
            } catch (NotFoundException e) {
                return NO_SUCH_COLLABORATION;
            }
        }

        try {
            return getRegularTask();
        } catch (NotFoundException e) {
            return TASK_NOT_FOUND;
        }
    }

    @Override
    public Map<String, Boolean> getArgs() {
        return GET_TASK_ARGS;
    }

    private String getCollaborationTask() throws NotFoundException {
        CollaborationTask result;
        result = collaborationsStorage.getByIdentifier(cmd.arguments().get(COLLABORATION)).tasks()
                .get(cmd.arguments().get(NAME));

        if (result == null) {
            return TASK_NOT_FOUND;
        }

        return result.toString();
    }

    private String getRegularTask() throws NotFoundException {
        Task task;
        if (cmd.arguments().containsKey(DATE)) {
            task = usersStorage.getByIdentifier(username).timedTasks()
                    .getByIdentifier(cmd.arguments().get(NAME), cmd.arguments().get(DATE));
        } else {
            task = usersStorage.getByIdentifier(username).inbox()
                    .getByIdentifier(cmd.arguments().get(NAME), null);
        }
        return task.toString();
    }
}
