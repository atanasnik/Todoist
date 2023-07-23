package bg.sofia.uni.fmi.todoist.command.collaborations;

import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.command.CommandExecutor;
import bg.sofia.uni.fmi.todoist.exception.NotFoundException;
import bg.sofia.uni.fmi.todoist.task.CollaborationTask;

import java.util.HashMap;
import java.util.Map;

public class AssignTaskCommand extends CommandExecutor {
    private static final String COLLABORATION = "collaboration";
    private static final String USER = "user";
    private static final String TASK = "task";
    private static final String INVALID_ASSIGN_PARAMETERS = "Insufficient/unwanted parameters to assign a task";
    private static final String NO_SUCH_USER = "There is no such user";
    private static final String NO_SUCH_TASK = "There is no such task";
    private static final String NO_SUCH_COLLABORATION = "There is no collaboration with that name";
    private static final String TASK_ASSIGNMENT = "Task assignment";


    private static final Map<String, Boolean> ASSIGN_TASK_COLLABORATION_ARGS = new HashMap<>() {
        {
            put(COLLABORATION, true);
            put(USER, true);
            put(TASK, true);
        }
    };

    public AssignTaskCommand(String username, Command cmd) {
        super(username, cmd);
    }

    @Override
    public String executeCommand() {
        if (usersStorage.getByIdentifier(cmd.arguments().get(USER)) == null) {
            return NO_SUCH_USER;
        }

        try {
            CollaborationTask task = collaborationsStorage.getByIdentifier(cmd.arguments().get(COLLABORATION))
                    .tasks().getOrDefault(cmd.arguments().get(TASK), null);

            if (task == null) {
                return NO_SUCH_TASK;
            }

            collaborationsStorage.getByIdentifier(cmd.arguments().get(COLLABORATION))
                    .tasks().get(cmd.arguments().get(TASK)).setAssignee(cmd.arguments().get(USER));

        } catch (NotFoundException e) {
            return NO_SUCH_COLLABORATION;
        }

        return String.format(OPERATION_SUCCESSFUL_MESSAGE_FORMAT, TASK_ASSIGNMENT);
    }

    @Override
    public Map<String, Boolean> getArgs() {
        return ASSIGN_TASK_COLLABORATION_ARGS;
    }
}
