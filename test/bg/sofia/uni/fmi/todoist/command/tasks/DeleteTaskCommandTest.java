package bg.sofia.uni.fmi.todoist.command.tasks;

import bg.sofia.uni.fmi.todoist.command.tools.ArgumentChecker;
import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.command.CommandGenerator;
import bg.sofia.uni.fmi.todoist.command.CommandSelector;
import bg.sofia.uni.fmi.todoist.exception.InvalidCommandException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeleteTaskCommandTest {
    @BeforeAll
    public static void setUp() throws InvalidCommandException {
        var reg = CommandSelector.select
                (CommandGenerator.newCommand("register --username=kk --password=ew"), null);
        reg.executeCommand();
    }

    @Test
    public void testDeleteTaskInvalidArgument() throws InvalidCommandException {
        Command cmd = CommandGenerator.newCommand("delete-task --na=del");
        var exec = CommandSelector.select(cmd, "kk");

        assertThrows(InvalidCommandException.class, () -> ArgumentChecker.assureArgsMatch(cmd, exec.getArgs()),
                "The user should be notified about an invalid command format");
    }

    @Test
    public void testDeleteInboxTask() throws InvalidCommandException {
        var first = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=toDelete" +
                        " --description=\"an original description of a task\""), "kk");
        first.executeCommand();

        var exec = CommandSelector.select
                (CommandGenerator.newCommand("delete-task --name=toDelete"), "kk");

        assertEquals("Task deleting successful!", exec.executeCommand(),
                "Task must be deleted successfully");
    }

    @Test
    public void testDeleteCollaborationTask() throws InvalidCommandException {
        var collab = CommandSelector.select
                (CommandGenerator.newCommand("add-collaboration --name=sss"), "kk");
        collab.executeCommand();
        var add = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=todelete --collaboration=sss"), "kk");
        add.executeCommand();
        var del = CommandSelector.select
                (CommandGenerator.newCommand("delete-task --name=todelete --collaboration=sss"),
                        "kk");

        assertEquals("Task deleting successful!", del.executeCommand(),
                "Task must be deleted successfully");

    }

    @Test
    public void testDeleteTimedTask() throws InvalidCommandException {
        var first = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=toDelete --date=11.11.2023" +
                        " --description=\"an original description of a task\""), "kk");
        first.executeCommand();

        var exec = CommandSelector.select
                (CommandGenerator.newCommand("delete-task --name=toDelete --date=11.11.2023"), "kk");

        assertEquals("Task deleting successful!", exec.executeCommand(),
                "Task must be deleted successfully");
    }

    @Test
    public void testDeleteNonExistingTask() throws InvalidCommandException {
        var exec = CommandSelector.select
                (CommandGenerator.newCommand("delete-task --name=notthere --date=11.11.2023"), "kk");

        assertEquals("Task not found.", exec.executeCommand(),
                "A non-existing task cannot be deleted");
    }
}
