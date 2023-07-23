package bg.sofia.uni.fmi.todoist.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CollaborationTaskTest {

    @Test
    public void testTaskBuildInvalidAssignee() {
        assertThrows(IllegalArgumentException.class,
                () -> CollaborationTask.builder("task").build().setAssignee(null),
                "An assignee name cannot be blank or null");
    }

}
