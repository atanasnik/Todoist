package bg.sofia.uni.fmi.todoist.command.tasks;

import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.command.CommandExecutor;
import bg.sofia.uni.fmi.todoist.exception.NotFoundException;
import bg.sofia.uni.fmi.todoist.exception.TaskNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class FinishTaskCommand extends CommandExecutor {
    private static final String NAME = "name";
    private static final String DATE = "date";
    private static final String COLLABORATION = "collaboration";
    private static final String NO_SUCH_COLLABORATION = "There is no collaboration with that name";
    private static final String TASK_NOT_FOUND = "Task not found.";
    private static final String TASK_COMPLETED = "Task completed!";

    private static final Map<String, Boolean> FINISH_TASK_ARGS = new HashMap<>() {
        {
            put(NAME, true);
            put(DATE, false);
            put(COLLABORATION, false);
        }
    };

    public FinishTaskCommand(String username, Command cmd) {
        super(username, cmd);
    }

    @Override
    public String executeCommand() {
        if (cmd.arguments().containsKey(COLLABORATION)) {
            try {
                finishCollaborationTask();
            } catch (NotFoundException e) {
                return NO_SUCH_COLLABORATION;
            }
        } else {
            try {
                finishRegularTask();
            } catch (TaskNotFoundException e) {
                return TASK_NOT_FOUND;
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }

        return TASK_COMPLETED;
    }

    @Override
    public Map<String, Boolean> getArgs() {
        return FINISH_TASK_ARGS;
    }

    private void finishCollaborationTask() throws NotFoundException {
        collaborationsStorage.getByIdentifier(cmd.arguments().get(COLLABORATION)).tasks()
                .get(cmd.arguments().get(NAME)).setCompleted(true);
    }

    private void finishRegularTask() throws NotFoundException {
        if (cmd.arguments().containsKey(DATE)) {
            usersStorage.getByIdentifier(username).timedTasks()
                    .getByIdentifier(cmd.arguments().get(NAME), cmd.arguments().get(DATE))
                    .setCompleted(true);
        } else {
            usersStorage.getByIdentifier(username).inbox()
                    .getByIdentifier(cmd.arguments().get(NAME), null)
                    .setCompleted(true);
        }
    }
}
