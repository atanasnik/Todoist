package bg.sofia.uni.fmi.todoist.command.tasks;

import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.command.CommandExecutor;
import bg.sofia.uni.fmi.todoist.exception.NotFoundException;
import bg.sofia.uni.fmi.todoist.task.CollaborationTask;
import bg.sofia.uni.fmi.todoist.task.Task;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ListTasksCommand extends CommandExecutor {
    private static final String DATE = "date";
    private static final String COLLABORATION = "collaboration";
    private static final String COMPLETED = "completed";
    private static final String NO_SUCH_COLLABORATION = "There is no collaboration with that name";
    private static final String NO_TASKS_FOUND = "No tasks found.";

    private static final Map<String, Boolean> LIST_TASKS_ARGS = new HashMap<>() {
        {
            put(COMPLETED, false);
            put(DATE, false);
            put(COLLABORATION, false);
        }
    };

    public ListTasksCommand(String username, Command cmd) {
        super(username, cmd);
    }

    @Override
    public String executeCommand() {
        if (cmd.arguments().containsKey(COLLABORATION)) {
            try {
                return listCollaborationTasks();
            } catch (NotFoundException e) {
                return NO_SUCH_COLLABORATION;
            }
        }

        return listRegularTasks();
    }

    @Override
    public Map<String, Boolean> getArgs() {
        return LIST_TASKS_ARGS;
    }

    private String listCollaborationTasks() throws NotFoundException {
        Collection<CollaborationTask> collaborationTasks;
        String result = "";

        collaborationTasks = collaborationsStorage
                .getByIdentifier(cmd.arguments().get(COLLABORATION)).tasks().values();

        if (cmd.arguments().containsKey(COMPLETED)) {
            if (!collaborationTasks.isEmpty()) {
                collaborationTasks =
                        collaborationTasks.stream().filter(Task::isCompleted).collect(Collectors.toSet());
            }
        }

        if (cmd.arguments().containsKey(DATE)) {
            collaborationTasks =
                    collaborationTasks
                            .stream()
                            .filter(task -> task.getDate().equals(cmd.arguments().get(DATE)))
                            .collect(Collectors.toSet());
        }

        if (collaborationTasks.isEmpty()) {
            return NO_TASKS_FOUND;
        }

        result = collaborationTasks
                .stream()
                .map(Task::toString)
                .sorted()
                .collect(Collectors.joining(System.lineSeparator()));

        return result;
    }

    private String listRegularTasks() {
        Set<Task> tasks;
        String concatenated = "";

        if (cmd.arguments().containsKey(DATE)) {
            tasks = usersStorage.getByIdentifier(username).timedTasks().list()
                    .stream()
                    .filter(task -> task.getDate().equals(cmd.arguments().get(DATE)))
                    .collect(Collectors.toSet());
        } else {
            tasks = usersStorage.getByIdentifier(username).inbox().list();
        }

        if (cmd.arguments().containsKey(COMPLETED) && cmd.arguments().get(COMPLETED).equals("true")) {
            if (!tasks.isEmpty()) {
                tasks = tasks.stream().filter(Task::isCompleted).collect(Collectors.toSet());
            }
        }

        if (tasks.isEmpty()) {
            return NO_TASKS_FOUND;
        }

        concatenated = tasks
                .stream()
                .map(Task::toString)
                .sorted()
                .collect(Collectors.joining(System.lineSeparator()));


        return concatenated;
    }
}
