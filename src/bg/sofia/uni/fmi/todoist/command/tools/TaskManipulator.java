package bg.sofia.uni.fmi.todoist.command.tools;

import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.task.Task;

public class TaskManipulator {
    private static final String NAME = "name";
    private static final String DATE = "date";
    private static final String DUE_DATE = "due-date";
    private static final String DESCRIPTION = "description";

    private TaskManipulator() {
    }

    public static Task createTask(Command cmd) {
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
}
