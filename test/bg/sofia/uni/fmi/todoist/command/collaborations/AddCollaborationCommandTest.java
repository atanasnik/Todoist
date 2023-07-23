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

public class AddCollaborationCommandTest {
    @BeforeAll
    public static void setUp() throws InvalidCommandException {
        var reg = CommandSelector.select
                (CommandGenerator.newCommand("register --username=rr --password=ew"), null);
        reg.executeCommand();
    }

    @Test
    public void testAddCollaborationInvalidArgument() throws InvalidCommandException {
        Command cmd = CommandGenerator.newCommand("add-collaboration");
        var exec = CommandSelector.select(cmd, null);

        assertThrows(InvalidCommandException.class, () -> ArgumentChecker.assureArgsMatch(cmd, exec.getArgs()),
                "Adding a collaboration with incorrect parameters must be handled successfully");
    }

    @Test
    public void testAddCollaborationCorrect() throws InvalidCommandException {
        var exec = CommandSelector.select
                (CommandGenerator.newCommand("add-collaboration --name=collab"), "rr");

        assertEquals("Collaboration adding successful!", exec.executeCommand(),
                "A collaboration must be added successfully");
    }

    @Test
    public void testAddCollaborationAlreadyExists() throws InvalidCommandException {
        var first = CommandSelector.select
                (CommandGenerator.newCommand("add-collaboration --name=first"), "rr");
        first.executeCommand();

        var second = CommandSelector.select
                (CommandGenerator.newCommand("add-collaboration --name=first"), "rr");

        assertEquals("Collaboration already exists.", second.executeCommand(),
                "An attempt to add a collaboration that exists must be handled successfully");
    }
}
