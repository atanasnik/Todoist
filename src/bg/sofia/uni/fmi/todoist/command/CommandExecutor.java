package bg.sofia.uni.fmi.todoist.command;

import bg.sofia.uni.fmi.todoist.storage.CollaborationsStorage;
import bg.sofia.uni.fmi.todoist.storage.UsersStorage;
import bg.sofia.uni.fmi.todoist.validation.Validator;

public abstract class CommandExecutor {

    private static final String COMMAND = "Command";

    /*private static final String INVALID_ARGS_COUNT_MESSAGE_FORMAT =
            "Invalid count of arguments: \"%s\" expects %d arguments.";*/
    protected static final String OPERATION_SUCCESSFUL_MESSAGE_FORMAT = "%s successful!";

    protected static UsersStorage usersStorage;
    protected static CollaborationsStorage collaborationsStorage;

    protected Command cmd;
    protected String username;

    static {
        usersStorage = new UsersStorage();
        collaborationsStorage = new CollaborationsStorage();
    }

    public CommandExecutor(String username, Command cmd) {
        Validator.objectArgument(cmd, COMMAND);
        this.username = username;
        this.cmd = cmd;
    }

    public UsersStorage usersStorage() {
        return usersStorage;
    }

    public CollaborationsStorage collaborationsStorage() {
        return collaborationsStorage;
    }

    public static void setUsersStorage(UsersStorage usersStorage) {
        CommandExecutor.usersStorage = usersStorage;
    }

    public static void setCollaborationsStorage(CollaborationsStorage collaborationsStorage) {
        CommandExecutor.collaborationsStorage = collaborationsStorage;
    }

    public abstract String executeCommand();
}
