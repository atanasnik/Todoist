package bg.sofia.uni.fmi.todoist.command;

import bg.sofia.uni.fmi.todoist.exception.InvalidCommandException;

import java.util.HashMap;
import java.util.Map;

public class ArgumentChecker {

    private ArgumentChecker() {
    }

    public static void assureArgsMatch(Command cmd, Map<String, Boolean> argNames) throws InvalidCommandException {
        if (argNames == null) {
            if (!cmd.arguments().isEmpty()) {
                throw new InvalidCommandException("Presence of incorrect arguments");
            }
            return;
        }

        int argsFound = 0;
        for (var entry : argNames.entrySet()) {
            if (!cmd.arguments().containsKey(entry.getKey()) && entry.getValue()) {
                throw new InvalidCommandException("Missing required argument");
            }

            if (cmd.arguments().containsKey(entry.getKey())) {
                ++argsFound;
            }
        }

        if (argsFound < cmd.arguments().size()) {
            throw new InvalidCommandException("Presence of incorrect arguments");
        }
    }


    //For every type of command we store the exact name of the argument and whether it is required to have
    private static final Map<String, Boolean> LOGIN_REGISTER_ARGS = new HashMap<>() {
        {
            put(USERNAME, true);
            put(PASSWORD, true);
        }
    };

    private static final Map<String, Boolean> ADD_UPDATE_TASK_ARGS = new HashMap<>() {
        {
            put(NAME, true);
            put(DATE, false);
            put(DUE_DATE, false);
            put(DESCRIPTION, false);
            put(COLLABORATION, false);
        }
    };

    private static final Map<String, Boolean> DELETE_GET_FINISH_TASK_ARGS = new HashMap<>() {
        {
            put(NAME, true);
            put(DATE, false);
            put(COLLABORATION, false);
        }
    };

    private static final Map<String, Boolean> LIST_TASKS_ARGS = new HashMap<>() {
        {
            put(COMPLETED, false);
            put(DATE, false);
            put(COLLABORATION, false);
        }
    };

    private static final Map<String, Boolean> ADD_DELETE_COLLABORATION_ARGS = new HashMap<>() {
        {
            put(NAME, true);
        }
    };

    private static final Map<String, Boolean> ADD_USER_COLLABORATION_ARGS = new HashMap<>() {
        {
            put(COLLABORATION, true);
            put(USER, true);
        }
    };

    private static final Map<String, Boolean> ASSIGN_TASK_COLLABORATION_ARGS = new HashMap<>() {
        {
            put(COLLABORATION, true);
            put(USER, true);
            put(TASK, true);
        }
    };

    private static final Map<String, Boolean> LIST_USERS_COLLABORATION_ARGS = new HashMap<>() {
        {
            put(COLLABORATION, true);
        }
    };


    public static Map<String, Boolean> getLoginRegisterArgs() {
        return LOGIN_REGISTER_ARGS;
    }

    public static Map<String, Boolean> getAddUpdateTaskArgs() {
        return ADD_UPDATE_TASK_ARGS;
    }

    public static Map<String, Boolean> getDeleteGetFinishTaskArgs() {
        return DELETE_GET_FINISH_TASK_ARGS;
    }

    public static Map<String, Boolean> getListTasksArgs() {
        return LIST_TASKS_ARGS;
    }

    public static Map<String, Boolean> getAddDeleteCollaborationArgs() {
        return ADD_DELETE_COLLABORATION_ARGS;
    }

    public static Map<String, Boolean> getAddUserCollaborationArgs() {
        return ADD_USER_COLLABORATION_ARGS;
    }
    public static Map<String, Boolean> getAssignTaskCollaborationArgs() {
        return ASSIGN_TASK_COLLABORATION_ARGS;
    }
    public static Map<String, Boolean> getListUsersCollaborationArgs() {
        return LIST_USERS_COLLABORATION_ARGS;
    }


    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final String DATE = "date";
    private static final String DUE_DATE = "due-date";
    private static final String DESCRIPTION = "description";
    private static final String COLLABORATION = "collaboration";
    private static final String COMPLETED = "completed";
    private static final String USER = "user";
    private static final String TASK = "task";

}
