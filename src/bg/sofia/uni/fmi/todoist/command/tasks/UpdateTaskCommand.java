package bg.sofia.uni.fmi.todoist.command.tasks;

import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.command.CommandExecutor;
import bg.sofia.uni.fmi.todoist.command.tools.TaskManipulator;
import bg.sofia.uni.fmi.todoist.exception.AlreadyExistsException;
import bg.sofia.uni.fmi.todoist.exception.CollaborationNotFoundException;
import bg.sofia.uni.fmi.todoist.exception.NotFoundException;
import bg.sofia.uni.fmi.todoist.exception.TaskNotFoundException;
import bg.sofia.uni.fmi.todoist.task.CollaborationTask;
import bg.sofia.uni.fmi.todoist.task.Task;

import java.util.HashMap;
import java.util.Map;

public class UpdateTaskCommand extends CommandExecutor {
    private static final String NAME = "name";
    private static final String DATE = "date";
    private static final String DUE_DATE = "due-date";
    private static final String DESCRIPTION = "description";
    private static final String COLLABORATION = "collaboration";
    private static final String TASK_NOT_FOUND = "Task not found.";
    private static final String NO_SUCH_COLLABORATION = "There is no collaboration with that name";
    private static final String TASK_ALREADY_EXISTS = "Task already exists.";
    private static final String TASK_UPDATING = "Task updating";

    private static final Map<String, Boolean> UPDATE_TASK_ARGS = new HashMap<>() {
        {
            put(NAME, true);
            put(DATE, false);
            put(DUE_DATE, false);
            put(DESCRIPTION, false);
            put(COLLABORATION, false);
        }
    };

    public UpdateTaskCommand(String username, Command cmd) {
        super(username, cmd);
    }

    @Override
    public String executeCommand() {
        Task task = TaskManipulator.createTask(cmd);

        if (cmd.arguments().containsKey(COLLABORATION)) {
            try {
                updateCollaborationTask(task);
            } catch (TaskNotFoundException e) {
                return TASK_NOT_FOUND;
            } catch (CollaborationNotFoundException e) {
                return NO_SUCH_COLLABORATION;
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                updateRegularTask(task);
            } catch (AlreadyExistsException e) {
                return TASK_ALREADY_EXISTS;
            } catch (NotFoundException e) {
                return TASK_NOT_FOUND;
            }
        }

        return String.format(OPERATION_SUCCESSFUL_MESSAGE_FORMAT, TASK_UPDATING);
    }

    @Override
    public Map<String, Boolean> getArgs() {
        return UPDATE_TASK_ARGS;
    }

    private void updateCollaborationTask(Task task) throws NotFoundException {
        CollaborationTask collaborationTask = CollaborationTask.of(task);
        if (collaborationsStorage.getByIdentifier(cmd.arguments().get(COLLABORATION)).tasks()
                .containsKey(collaborationTask.getName())) {

            if (collaborationsStorage
                    .getByIdentifier(cmd.arguments().get(COLLABORATION)).tasks().get(task.getName()) == null) {
                throw new TaskNotFoundException("Task not found");
            }
            collaborationsStorage.getByIdentifier(cmd.arguments().get(COLLABORATION)).tasks()
                    .replace(collaborationTask.getName(), collaborationTask);
        } else {
            throw new CollaborationNotFoundException("Collaboration not found");
        }
    }

    private void updateRegularTask(Task task) throws NotFoundException, AlreadyExistsException {
        Task found = usersStorage.getByIdentifier(username).timedTasks().containsName(task.getName());

        if (found != null) {
            usersStorage.getByIdentifier(username).timedTasks().remove(found.getName(), found.getDate());

            if (cmd.arguments().containsKey(DATE)) {
                usersStorage.getByIdentifier(username).timedTasks().add(task.getName(), task);
            } else {
                usersStorage.getByIdentifier(username).inbox().add(task.getName(), task);
            }

            return;
        }

        if (usersStorage.getByIdentifier(username).inbox().contains(task.getName(), task)) {

            usersStorage.getByIdentifier(username).inbox().remove(task.getName(), null);

            if (cmd.arguments().containsKey(DATE)) {
                usersStorage.getByIdentifier(username).timedTasks().add(task.getName(), task);
            } else {
                usersStorage.getByIdentifier(username).inbox().add(task.getName(), task);
            }

        } else {
            throw new NotFoundException("Task not found");
        }
    }
}
