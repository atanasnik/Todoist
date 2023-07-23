package bg.sofia.uni.fmi.todoist.command.collaborations;

import bg.sofia.uni.fmi.todoist.command.tools.ArgumentChecker;
import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.command.CommandGenerator;
import bg.sofia.uni.fmi.todoist.command.CommandSelector;
import bg.sofia.uni.fmi.todoist.exception.InvalidCommandException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeleteCollaborationCommandTest {
    @BeforeAll
    public static void setUp() throws InvalidCommandException {
        var reg = CommandSelector.select
                (CommandGenerator.newCommand("register --username=qq --password=ew"), null);
        reg.executeCommand();
    }

    @Test
    public void testDeleteCollaborationInvalidArgument() throws InvalidCommandException {
        Command cmd = CommandGenerator.newCommand("delete-collaboration");
        var exec = CommandSelector.select(cmd, null);

        assertThrows(InvalidCommandException.class, () -> ArgumentChecker.assureArgsMatch(cmd, exec.getArgs()),
                "Deleting a collaboration with incorrect parameters must be handled successfully");
    }

    @Test
    public void testDeleteCollaborationCorrect() throws InvalidCommandException {
        var toDelete = CommandSelector.select
                (CommandGenerator.newCommand("add-collaboration --name=todelete"), "qq");
        toDelete.executeCommand();

        var second = CommandSelector.select
                (CommandGenerator.newCommand("delete-collaboration --name=todelete"), "qq");

        assertEquals("Collaboration removal successful!", second.executeCommand(),
                "A collaboration must be deleted successfully");
    }

    @Test
    public void testDeleteCollaborationNonExistent() throws InvalidCommandException {
        var del = CommandSelector.select
                (CommandGenerator.newCommand("delete-collaboration --name=notexist"), "qq");

        assertEquals("There is no collaboration with that name", del.executeCommand(),
                "An attempt to delete a non-existent collaboration must be handled successfully");
    }

    @Test
    public void testDeleteCollaborationUnauthorized() throws InvalidCommandException {
        var first = CommandSelector.select
                (CommandGenerator.newCommand("add-collaboration --name=what"), "qq");
        first.executeCommand();

        var del = CommandSelector.select
                (CommandGenerator.newCommand("delete-collaboration --name=what"), "notThis");

        assertEquals("This operation can be only performed by the owner of the collaboration",
                del.executeCommand(),
                "An unauthorised attempt attempt to delete a collaboration must be handled successfully");
    }
}