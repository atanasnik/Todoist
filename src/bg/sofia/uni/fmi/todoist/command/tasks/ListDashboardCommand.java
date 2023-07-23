package bg.sofia.uni.fmi.todoist.command.tasks;

import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.command.CommandExecutor;
import bg.sofia.uni.fmi.todoist.task.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ListDashboardCommand extends CommandExecutor {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final String NO_TASKS_FOUND = "No tasks found.";

    public ListDashboardCommand(String username, Command cmd) {
        super(username, cmd);
    }

    @Override
    public String executeCommand() {
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

    @Override
    public Map<String, Boolean> getArgs() {
        return null;
    }

    private LocalDate stringToDate(String date) {
        return LocalDate.parse(date, FORMATTER);
    }
}
