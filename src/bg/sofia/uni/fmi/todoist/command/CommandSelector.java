package bg.sofia.uni.fmi.todoist.command;

import bg.sofia.uni.fmi.todoist.command.account.LoginCommand;
import bg.sofia.uni.fmi.todoist.command.account.RegisterCommand;
import bg.sofia.uni.fmi.todoist.command.collaborations.*;
import bg.sofia.uni.fmi.todoist.command.tasks.*;
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
            case CMD_REGISTER -> new RegisterCommand(username, cmd);
            case CMD_LOGIN -> new LoginCommand(username, cmd);

            case CMD_ADD_TASK -> new AddTaskCommand(username, cmd);
            case CMD_DELETE_TASK -> new DeleteTaskCommand(username, cmd);
            case CMD_UPDATE_TASK -> new UpdateTaskCommand(username, cmd);
            case CMD_GET_TASK -> new GetTaskCommand(username, cmd);
            case CMD_LIST_TASKS -> new ListTasksCommand(username, cmd);
            case CMD_LIST_DASHBOARD -> new ListDashboardCommand(username, cmd);
            case CMD_FINISH_TASK -> new FinishTaskCommand(username, cmd);

            case CMD_ADD_COLLABORATION -> new AddCollaborationCommand(username, cmd);
            case CMD_DELETE_COLLABORATION -> new DeleteCollaborationCommand(username, cmd);
            case CMD_LIST_COLLABORATIONS -> new ListCollaborationsCommand(username, cmd);
            case CMD_ADD_USER -> new AddUserCommand(username, cmd);
            case CMD_ASSIGN_TASK -> new AssignTaskCommand(username, cmd);
            case CMD_LIST_USERS -> new ListUsersCommand(username, cmd);

            default -> throw new InvalidCommandException("Unknown command");
        };
    }
}