package bg.sofia.uni.fmi.todoist.user;

import bg.sofia.uni.fmi.todoist.authentication.Hasher;
import bg.sofia.uni.fmi.todoist.storage.CollaborationsStorage;
import bg.sofia.uni.fmi.todoist.storage.InboxStorage;
import bg.sofia.uni.fmi.todoist.storage.TimedTasksStorage;
import bg.sofia.uni.fmi.todoist.validation.Validator;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class User {
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    private static final String TIMED_TASKS_STORAGE = "Timed tasks storage";
    private static final String INBOX_STORAGE = "Inbox storage";
    private static final String COLLABORATIONS_STORAGE = "Collaborations storage";

    private String username;
    private String password;
    private TimedTasksStorage timedTasks;
    private InboxStorage inbox;

    public User(String username, String password,
                TimedTasksStorage timedTasks, InboxStorage inbox, CollaborationsStorage collaborations) {

        Validator.stringArgument(username, USERNAME);
        Validator.stringArgument(password, PASSWORD);
        Validator.objectArgument(timedTasks, TIMED_TASKS_STORAGE);
        Validator.objectArgument(inbox, INBOX_STORAGE);
        Validator.objectArgument(collaborations, COLLABORATIONS_STORAGE);

        this.username = username;
        this.password = Hasher.hashPassword(password);
        this.timedTasks = timedTasks;
        this.inbox = inbox;
    }

    public boolean checkPassword(String password) {
        return Hasher.checkPassword(password, this.password);
    }

    public String username() {
        return username;
    }

    public TimedTasksStorage timedTasks() {
        return timedTasks;
    }

    public InboxStorage inbox() {
        return inbox;
    }
}







