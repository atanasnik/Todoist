package bg.sofia.uni.fmi.todoist.command;

import bg.sofia.uni.fmi.todoist.exception.AlreadyExistsException;
import bg.sofia.uni.fmi.todoist.exception.CollaborationNotFoundException;
import bg.sofia.uni.fmi.todoist.exception.InvalidCommandException;
import bg.sofia.uni.fmi.todoist.exception.NotFoundException;
import bg.sofia.uni.fmi.todoist.exception.TaskNotFoundException;
import bg.sofia.uni.fmi.todoist.task.CollaborationTask;
import bg.sofia.uni.fmi.todoist.task.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskExecutor extends CommandExecutor {

    private static final String CMD_ADD_TASK = "add-task";
    private static final String CMD_UPDATE_TASK = "update-task";
    private static final String CMD_DELETE_TASK = "delete-task";
    private static final String CMD_GET_TASK = "get-task";

    //There is one for collaborations too
    private static final String CMD_LIST_TASKS = "list-tasks";
    private static final String CMD_LIST_DASHBOARD = "list-dashboard";
    private static final String CMD_FINISH_TASK = "finish-task";
    private static final String NAME = "name";
    private static final String DATE = "date";
    private static final String DUE_DATE = "due-date";
    private static final String DESCRIPTION = "description";
    private static final String COMPLETED = "completed";

    private static final String COLLABORATION = "collaboration";
    private static final String TASK_ADDING = "Task adding";
    private static final String TASK_UPDATING = "Task updating";
    private static final String TASK_DELETING = "Task deleting";
    private static final String TASK_COMPLETED = "Task completed!";
    private static final String TASK_ALREADY_EXISTS = "Task already exists.";
    private static final String TASK_NOT_FOUND = "Task not found.";
    private static final String NO_TASKS_FOUND = "No tasks found.";
    private static final String NO_SUCH_COLLABORATION = "There is no collaboration with that name";

    private static final String UNAUTHORIZED_COMMAND = "Unauthorized command. Register or login.";
    private static final String INVALID_ADD_PARAMETERS = "Insufficient/unwanted parameters to add a task";
    private static final String INVALID_FINISH_PARAMETERS = "Insufficient/unwanted parameters to finish a task";
    private static final String INVALID_UPDATE_PARAMETERS = "Insufficient/unwanted parameters to update a task";
    private static final String INVALID_DELETE_PARAMETERS = "Insufficient/unwanted parameters to delete a task";
    private static final String INVALID_GET_PARAMETERS = "Insufficient/unwanted parameters to get a task";
    private static final String INVALID_LIST_PARAMETERS = "Insufficient/unwanted parameters to list tasks";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public TaskExecutor(String username, Command cmd) {
        super(username, cmd);
    }

    @Override
    public String executeCommand() {
        if (username == null) {
            return UNAUTHORIZED_COMMAND;
        }

        return switch (cmd.command()) {
            case CMD_ADD_TASK -> addTask();
            case CMD_UPDATE_TASK -> updateTask();
            case CMD_DELETE_TASK -> deleteTask();
            case CMD_GET_TASK -> getTask();
            case CMD_LIST_TASKS -> listTasks();
            case CMD_LIST_DASHBOARD -> listDashboard();
            case CMD_FINISH_TASK -> finishTask();
            default -> "";
        };
    }

    private String addTask() {
        try {
            ArgumentChecker.assureArgsMatch(cmd, ArgumentChecker.getAddUpdateTaskArgs());
        } catch (InvalidCommandException e) {
            // e.printStackTrace();
            return INVALID_ADD_PARAMETERS;
        }

        Task task = createTask();

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

    private String updateTask() {
        try {
            ArgumentChecker.assureArgsMatch(cmd, ArgumentChecker.getAddUpdateTaskArgs());
        } catch (InvalidCommandException e) {
            // e.printStackTrace();
            return INVALID_UPDATE_PARAMETERS;
        }

        Task task = createTask();

        if (cmd.arguments().containsKey(COLLABORATION)) {

            try {
                updateCollaborationTask(task);
            } catch (TaskNotFoundException e) {
                //e.printStackTrace();
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
                //e.printStackTrace();
                return TASK_ALREADY_EXISTS;
            } catch (NotFoundException e) {
                //e.printStackTrace();
                return TASK_NOT_FOUND;
            }
        }

        return String.format(OPERATION_SUCCESSFUL_MESSAGE_FORMAT, TASK_UPDATING);
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

    private String deleteTask() {
        try {
            ArgumentChecker.assureArgsMatch(cmd, ArgumentChecker.getDeleteGetFinishTaskArgs());
        } catch (InvalidCommandException e) {
            // e.printStackTrace();
            return INVALID_DELETE_PARAMETERS;
        }

        if (cmd.arguments().containsKey(COLLABORATION)) {
            try {
                deleteCollaborationTask();
            } catch (TaskNotFoundException e) {
                return TASK_NOT_FOUND;
            } catch (NotFoundException e) {
                //e.printStackTrace();
                return NO_SUCH_COLLABORATION;
            }
        } else {
            try {
                deleteRegularTask();
            } catch (NotFoundException e) {
                //e.printStackTrace();
                return TASK_NOT_FOUND;
            }
        }

        return String.format(OPERATION_SUCCESSFUL_MESSAGE_FORMAT, TASK_DELETING);
    }

    private void deleteCollaborationTask() throws NotFoundException {
        var result = collaborationsStorage.getByIdentifier(cmd.arguments().get(COLLABORATION)).tasks()
                .remove(cmd.arguments().get(NAME));

        if (result == null) {
            throw new TaskNotFoundException("Task not found");
        }
    }

    private void deleteRegularTask() throws NotFoundException {
        if (cmd.arguments().containsKey(DATE)) {
            usersStorage.getByIdentifier(username).timedTasks()
                    .remove(cmd.arguments().get(NAME), cmd.arguments().get(DATE));
        } else {
            usersStorage.getByIdentifier(username).inbox()
                    .remove(cmd.arguments().get(NAME), null);
        }
    }

    private String getTask() {
        try {
            ArgumentChecker.assureArgsMatch(cmd, ArgumentChecker.getDeleteGetFinishTaskArgs());
        } catch (InvalidCommandException e) {
            // e.printStackTrace();
            return INVALID_GET_PARAMETERS;
        }

        if (cmd.arguments().containsKey(COLLABORATION)) {

            try {
                return getCollaborationTask();

            } catch (NotFoundException e) {
                //e.printStackTrace();
                return NO_SUCH_COLLABORATION;
            }

        }

        try {
            return getRegularTask();
        } catch (NotFoundException e) {
            //e.printStackTrace();
            return TASK_NOT_FOUND;
        }

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

    private String listTasks() {
        try {
            ArgumentChecker.assureArgsMatch(cmd, ArgumentChecker.getListTasksArgs());
        } catch (InvalidCommandException e) {
            // e.printStackTrace();
            return INVALID_LIST_PARAMETERS;
        }

        if (cmd.arguments().containsKey(COLLABORATION)) {
            try {
                return listCollaborationTasks();
            } catch (NotFoundException e) {
                //e.printStackTrace();
                return NO_SUCH_COLLABORATION;
            }
        }

        return listRegularTasks();
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

    private String listDashboard() {
        try {
            ArgumentChecker.assureArgsMatch(cmd, null);
        } catch (InvalidCommandException e) {
            // e.printStackTrace();
            return INVALID_LIST_PARAMETERS;
        }

        String result = "";

        Set<String> tasks = usersStorage.getByIdentifier(username).timedTasks()
                .list()
                .stream()
                .filter(task -> stringToDate(task.getDate()).equals(LocalDate.now()))
                .map(Task::toString)
                .collect(Collectors.toSet());

        if (tasks.isEmpty()) {
            return NO_TASKS_FOUND;
        }

        result = tasks
                .stream()
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

    private String finishTask() {
        try {
            ArgumentChecker.assureArgsMatch(cmd, ArgumentChecker.getDeleteGetFinishTaskArgs());
        } catch (InvalidCommandException e) {
            // e.printStackTrace();
            return INVALID_FINISH_PARAMETERS;
        }

        if (cmd.arguments().containsKey(COLLABORATION)) {
            try {
                finishCollaborationTask();
            } catch (NotFoundException e) {
                //e.printStackTrace();
                return NO_SUCH_COLLABORATION;
            }
        } else {

            try {
                finishRegularTask();
            } catch (TaskNotFoundException e) {
                //e.printStackTrace();
                return TASK_NOT_FOUND;
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }

        return TASK_COMPLETED;
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

    private Task createTask() {
        var buildTask = Task.builder(cmd.arguments().get(NAME));

        if (cmd.arguments().containsKey(DATE)) {
            buildTask.setDate(cmd.arguments().get(DATE));
        }
        if (cmd.arguments().containsKey(DUE_DATE)) {
            buildTask.setDueDate(cmd.arguments().get(DUE_DATE));
        }
        if (cmd.arguments().containsKey(DESCRIPTION)) {
            buildTask.setDescription(cmd.arguments().get(DESCRIPTION));
        }

        return buildTask.build();
    }

    private LocalDate stringToDate(String date) {
        return LocalDate.parse(date, FORMATTER);
    }
}
