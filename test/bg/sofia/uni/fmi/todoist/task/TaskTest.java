package bg.sofia.uni.fmi.todoist.task;

import bg.sofia.uni.fmi.todoist.user.User;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskTest {

    @Test
    public void testTaskBuildInvalidName() {
        assertThrows(IllegalArgumentException.class,
                () -> Task.builder(null).build(),
                "A task name cannot be blank or null");
    }

    @Test
    public void testTaskBuildInvalidDate() {
        assertThrows(IllegalArgumentException.class,
                () -> Task.builder("task").setDate(null),
                "A task date cannot be blank or null");
    }

    @Test
    public void testTaskBuildInvalidDueDate() {
        assertThrows(IllegalArgumentException.class,
                () -> Task.builder("task").setDueDate(null),
                "A task due date cannot be blank or null");
    }

    @Test
    public void testTaskBuildInvalidDescription() {
        assertThrows(IllegalArgumentException.class,
                () -> Task.builder("task").setDescription(null),
                "A task description cannot be blank or null");
    }
}
