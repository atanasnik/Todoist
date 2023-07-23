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

public class AddUserCommandTest {
    @BeforeAll
    public static void setUp() throws InvalidCommandException {
        var reg = CommandSelector.select
                (CommandGenerator.newCommand("register --username=tt --password=ew"), null);
        reg.executeCommand();
    }

    @Test
    public void testDeleteTaskInvalidArgument() throws InvalidCommandException {
        Command cmd = CommandGenerator.newCommand("add-user");
        var exec = CommandSelector.select(cmd, "tt");

        assertThrows(InvalidCommandException.class, () -> ArgumentChecker.assureArgsMatch(cmd, exec.getArgs()),
                "Adding a user with invalid parameters should be handled successfully");
    }

    @Test
    public void testAddUser() throws InvalidCommandException {
        var first = CommandSelector.select
                (CommandGenerator.newCommand("add-collaboration --name=qqq"), "tt");
        first.executeCommand();

        var reg = CommandSelector.select
                (CommandGenerator.newCommand("register --username=werwe --password=ewew"),
                        null);
        reg.executeCommand();

        var us = CommandSelector.select
                (CommandGenerator.newCommand("add-user --user=werwe --collaboration=qqq")
                        , "tt");

        assertEquals("User adding successful!",
                us.executeCommand(),
                "A user must be added successfully");
    }

}
