package bg.sofia.uni.fmi.todoist.user;

import bg.sofia.uni.fmi.todoist.storage.CollaborationsStorage;
import bg.sofia.uni.fmi.todoist.storage.InboxStorage;
import bg.sofia.uni.fmi.todoist.storage.TimedTasksStorage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    @Test
    public void testUserCreationInvalidUsername() {
        assertThrows(IllegalArgumentException.class,
                () -> new User("", "me",
                        new TimedTasksStorage(), new InboxStorage(), new CollaborationsStorage()),
                "A username cannot be blank or null");
    }

    @Test
    public void testUserCreationInvalidPassword() {
        assertThrows(IllegalArgumentException.class,
                () -> new User("name", null,
                        new TimedTasksStorage(), new InboxStorage(), new CollaborationsStorage()),
                "A user's password cannot be blank or null");
    }

    @Test
    public void testUserCreationInvalidTimedTasksContainer() {
        assertThrows(IllegalArgumentException.class,
                () -> new User("name", "password",
                        null, new InboxStorage(), new CollaborationsStorage()),
                "A user's timed tasks container cannot be null");
    }

    @Test
    public void testUserCreationInvalidInboxContainer() {
        assertThrows(IllegalArgumentException.class,
                () -> new User("name", "password",
                        new TimedTasksStorage(), null, new CollaborationsStorage()),
                "A user's inbox container cannot be null");
    }

    @Test
    public void testUserCreationInvalidCollaborationsContainer() {
        assertThrows(IllegalArgumentException.class,
                () -> new User("name", "password",
                        new TimedTasksStorage(), new InboxStorage(), null),
                "A user's collaborations container cannot be null");
    }

}
