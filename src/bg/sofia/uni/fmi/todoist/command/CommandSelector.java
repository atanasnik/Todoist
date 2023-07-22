package bg.sofia.uni.fmi.todoist.command;

import bg.sofia.uni.fmi.todoist.exception.InvalidCommandException;

public class CommandSelector {

    private static final String CMD_REGISTER = "register";
    private static final String CMD_LOGIN = "login";

    private static final String CMD_ADD_TASK = "add-task";
    private static final String CMD_UPDATE_TASK = "update-task";
    private static final String CMD_DELETE_TASK = "delete-task";
    private static final String CMD_GET_TASK = "get-task";

    //There is one for collaborations too
    private static final String CMD_LIST_TASKS = "list-tasks";
    private static final String CMD_LIST_DASHBOARD = "list-dashboard";
    private static final String CMD_FINISH_TASK = "finish-task";

    private static final String CMD_ADD_COLLABORATION = "add-collaboration";
    private static final String CMD_DELETE_COLLABORATION = "delete-collaboration";
    private static final String CMD_LIST_COLLABORATIONS = "list-collaborations";
    private static final String CMD_ADD_USER = "add-user";
    private static final String CMD_ASSIGN_TASK = "assign-task";
    private static final String CMD_LIST_USERS = "list-users";

    public static CommandExecutor select(Command cmd, String username) throws InvalidCommandException {
        return switch(cmd.command()) {
            case CMD_REGISTER, CMD_LOGIN -> new AccountExecutor(username, cmd);

            case CMD_ADD_TASK, CMD_DELETE_TASK, CMD_GET_TASK, CMD_LIST_TASKS,
                    CMD_LIST_DASHBOARD, CMD_FINISH_TASK, CMD_UPDATE_TASK -> new TaskExecutor(username, cmd);

            case CMD_ADD_COLLABORATION, CMD_DELETE_COLLABORATION, CMD_LIST_COLLABORATIONS,
                    CMD_ADD_USER, CMD_ASSIGN_TASK, CMD_LIST_USERS -> new CollaborationExecutor(username, cmd);

            default -> throw new InvalidCommandException("Unknown command");
        };
    }
}
