package bg.sofia.uni.fmi.todoist.command.tasks;

import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.command.CommandExecutor;
import bg.sofia.uni.fmi.todoist.command.tools.TaskManipulator;
import bg.sofia.uni.fmi.todoist.exception.AlreadyExistsException;
import bg.sofia.uni.fmi.todoist.exception.NotFoundException;
import bg.sofia.uni.fmi.todoist.task.CollaborationTask;
import bg.sofia.uni.fmi.todoist.task.Task;

import java.util.HashMap;
import java.util.Map;

public class AddTaskCommand extends CommandExecutor {
    private static final String NAME = "name";
    private static final String DATE = "date";
    private static final String DUE_DATE = "due-date";
    private static final String DESCRIPTION = "description";
    private static final String COLLABORATION = "collaboration";
    private static final String NO_SUCH_COLLABORATION = "There is no collaboration with that name";
    private static final String TASK_ALREADY_EXISTS = "Task already exists.";
    private static final String TASK_ADDING = "Task adding";

    private static final Map<String, Boolean> ADD_TASK_ARGS = new HashMap<>() {
        {
            put(NAME, true);
            put(DATE, false);
            put(DUE_DATE, false);
            put(DESCRIPTION, false);
            put(COLLABORATION, false);
        }
    };

    public AddTaskCommand(String username, Command cmd) {
        super(username, cmd);
    }

    @Override
    public String executeCommand() {
        Task task = TaskManipulator.createTask(cmd);

        if (cmd.arguments().containsKey(COLLABORATION)) {
            try {
                addCollaborationTask(task);
            } catch (NotFoundException e) {
                return NO_SUCH_COLLABORATION;
            }
        } else {
            try {
                addRegularTask(task);
            } catch (AlreadyExistsException e) {
                return TASK_ALREADY_EXISTS;
            }
        }

        return String.format(OPERATION_SUCCESSFUL_MESSAGE_FORMAT, TASK_ADDING);
    }

    @Override
    public Map<String, Boolean> getArgs() {
        return ADD_TASK_ARGS;
    }

    private void addCollaborationTask(Task task) throws NotFoundException {
        CollaborationTask collaborationTask = CollaborationTask.of(task);

        collaborationsStorage.getByIdentifier(cmd.arguments().get(COLLABORATION)).tasks()
                .put(collaborationTask.getName(), collaborationTask);

    }

    private void addRegularTask(Task task) throws AlreadyExistsException {
        if (cmd.arguments().containsKey(DATE)) {
            usersStorage.getByIdentifier(username).timedTasks().add(task.getName(), task);
        } else {
            usersStorage.getByIdentifier(username).inbox().add(task.getName(), task);
        }
    }
}
