package bg.sofia.uni.fmi.todoist.collaboration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CollaborationTest {

    @Test
    public void testCollaborationCreationInvalidName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Collaboration("", "me"),
                "A collaboration's name cannot be blank or null");
    }

    @Test
    public void testCollaborationCreationInvalidOwner() {
        assertThrows(IllegalArgumentException.class,
                () -> new Collaboration("name", null),
                "The name of the owner of a collaboration cannot be blank or null");
    }
}
