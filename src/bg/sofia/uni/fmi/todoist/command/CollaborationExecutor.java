package bg.sofia.uni.fmi.todoist.command;

import bg.sofia.uni.fmi.todoist.collaboration.Collaboration;
import bg.sofia.uni.fmi.todoist.exception.AlreadyExistsException;
import bg.sofia.uni.fmi.todoist.exception.InvalidCommandException;
import bg.sofia.uni.fmi.todoist.exception.NotFoundException;
import bg.sofia.uni.fmi.todoist.task.CollaborationTask;

import java.util.stream.Collectors;

public class CollaborationExecutor extends CommandExecutor {

    private static final String CMD_ADD_COLLABORATION = "add-collaboration";
    private static final String CMD_DELETE_COLLABORATION = "delete-collaboration";
    private static final String CMD_LIST_COLLABORATIONS = "list-collaborations";
    private static final String CMD_ADD_USER = "add-user";
    private static final String CMD_ASSIGN_TASK = "assign-task";
    private static final String CMD_LIST_USERS = "list-users";

    private static final String COLLABORATION = "collaboration";
    private static final String USER = "user";
    private static final String NAME = "name";
    private static final String TASK = "task";
    private static final String ALREADY_EXISTS = "Collaboration already exists.";
    private static final String NO_SUCH_COLLABORATION = "There is no collaboration with that name";
    private static final String NO_COLLABORATIONS_FOUND = "No collaborations found";
    private static final String NO_SUCH_USER = "There is no such user";
    private static final String NO_SUCH_TASK = "There is no such task";
    private static final String COLLABORATION_ADDING = "Collaboration adding";
    private static final String USER_ADDING = "User adding";
    private static final String TASK_ASSIGNMENT = "Task assignment";
    private static final String COLLABORATION_REMOVAL = "Collaboration removal";
    private static final String UNAUTHORIZED_OPERATION = "This operation can be only performed" +
            " by the owner of the collaboration";

    private static final String UNAUTHORIZED_COMMAND = "Unauthorized command. Register or login.";
    private static final String INVALID_ADD_PARAMETERS = "Insufficient/unwanted parameters to add a collaboration.";
    private static final String INVALID_USER_PARAMETERS = "Insufficient/unwanted parameters to add a collaboration.";
    private static final String INVALID_LIST_USERS_PARAMETERS = "Insufficient/unwanted parameters to" +
            " list users.";
    private static final String INVALID_DELETE_PARAMETERS =
            "Insufficient/unwanted parameters to delete a collaboration.";
    private static final String INVALID_LIST_PARAMETERS = "Insufficient/unwanted parameters to list collaborations.";
    private static final String INVALID_ASSIGN_PARAMETERS = "Insufficient/unwanted parameters to assign a task";



    public CollaborationExecutor(String username, Command cmd) {
        super(username, cmd);
    }

    @Override
    public String executeCommand() {
        if (username == null) {
            return UNAUTHORIZED_COMMAND;
        }

        return switch(cmd.command()) {
            case CMD_ADD_COLLABORATION -> addCollaboration();
            case CMD_DELETE_COLLABORATION -> deleteCollaboration();
            case CMD_LIST_COLLABORATIONS -> listCollaborations();
            case CMD_ADD_USER -> addUser();
            case CMD_ASSIGN_TASK -> assignTask();
            case CMD_LIST_USERS -> listUsers();
            default -> "";
        };
    }


    private String addCollaboration() {
        try {
            ArgumentChecker.assureArgsMatch(cmd, ArgumentChecker.getAddDeleteCollaborationArgs());
        } catch (InvalidCommandException e) {
            // e.printStackTrace();
            return INVALID_ADD_PARAMETERS;
        }

        Collaboration collaboration = new Collaboration(cmd.arguments().get(NAME), username);

        collaboration.collaborators().add(username);

        try {
            collaborationsStorage.add(collaboration.name(), collaboration);
        } catch (AlreadyExistsException e) {
            return ALREADY_EXISTS;
        }

        return String.format(OPERATION_SUCCESSFUL_MESSAGE_FORMAT, COLLABORATION_ADDING);
    }

    private String deleteCollaboration() {
        try {
            ArgumentChecker.assureArgsMatch(cmd, ArgumentChecker.getAddDeleteCollaborationArgs());
        } catch (InvalidCommandException e) {
            // e.printStackTrace();
            return INVALID_DELETE_PARAMETERS;
        }

        try {
            if (!collaborationsStorage.getByIdentifier(cmd.arguments().get(NAME)).owner().equals(username)) {
                return UNAUTHORIZED_OPERATION;
            }

            collaborationsStorage.remove(cmd.arguments().get(NAME));

        } catch (NotFoundException e) {
            //e.printStackTrace();
            return NO_SUCH_COLLABORATION;
        }

        return String.format(OPERATION_SUCCESSFUL_MESSAGE_FORMAT, COLLABORATION_REMOVAL);
    }

    private String listCollaborations() {
        try {
            ArgumentChecker.assureArgsMatch(cmd, null);
        } catch (InvalidCommandException e) {
            // e.printStackTrace();
            return INVALID_LIST_PARAMETERS;
        }

        var listed = collaborationsStorage.list();
        if (listed.isEmpty()) {
            return NO_COLLABORATIONS_FOUND;
        }

        return listed
                .stream()
                .filter(collaboration -> collaboration.collaborators().contains(username))
                .map(Collaboration::toString).sorted().collect(Collectors.joining(System.lineSeparator()));
    }

    private String addUser() {
        try {
            ArgumentChecker.assureArgsMatch(cmd, ArgumentChecker.getAddUserCollaborationArgs());
        } catch (InvalidCommandException e) {
            // e.printStackTrace();
            return INVALID_USER_PARAMETERS;
        }

        if (usersStorage.getByIdentifier(cmd.arguments().get(USER)) == null) {
            return NO_SUCH_USER;
        }


        try {
            collaborationsStorage.getByIdentifier(cmd.arguments().get(COLLABORATION))
                    .collaborators().add(cmd.arguments().get(USER));
        } catch (NotFoundException e) {
            //throw new RuntimeException(e);
            return NO_SUCH_COLLABORATION;
        }

        return String.format(OPERATION_SUCCESSFUL_MESSAGE_FORMAT, USER_ADDING);
    }

    private String assignTask() {
        try {
            ArgumentChecker.assureArgsMatch(cmd, ArgumentChecker.getAssignTaskCollaborationArgs());
        } catch (InvalidCommandException e) {
            // e.printStackTrace();
            return INVALID_ASSIGN_PARAMETERS;
        }

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
            //throw new RuntimeException(e);
            return NO_SUCH_COLLABORATION;
        }

        return String.format(OPERATION_SUCCESSFUL_MESSAGE_FORMAT, TASK_ASSIGNMENT);
    }

    private String listUsers() {
        try {
            ArgumentChecker.assureArgsMatch(cmd, ArgumentChecker.getListUsersCollaborationArgs());
        } catch (InvalidCommandException e) {
            // e.printStackTrace();
            return INVALID_LIST_USERS_PARAMETERS;
        }

        String users;

        try {
            var set = collaborationsStorage.getByIdentifier(cmd.arguments()
                    .get(COLLABORATION)).collaborators();

            users = set.stream().sorted().collect(Collectors.joining(System.lineSeparator()));
        } catch (NotFoundException e) {
            // e.printStackTrace();
            return NO_SUCH_COLLABORATION;
        }

        return users + System.lineSeparator();
    }

}
