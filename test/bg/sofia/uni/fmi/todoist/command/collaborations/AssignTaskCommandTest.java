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

public class AssignTaskCommandTest {
    @BeforeAll
    public static void setUp() throws InvalidCommandException {
        var reg = CommandSelector.select
                (CommandGenerator.newCommand("register --username=uu --password=ew"), null);
        reg.executeCommand();
    }

    @Test
    public void testDeleteTaskInvalidArgument() throws InvalidCommandException {
        Command cmd = CommandGenerator.newCommand("assign-task");
        var exec = CommandSelector.select(cmd, "uu");

        assertThrows(InvalidCommandException.class, () -> ArgumentChecker.assureArgsMatch(cmd, exec.getArgs()),
                "Assigning a task with invalid parameters should be handled successfully");
    }

    @Test
    public void testAssignTask() throws InvalidCommandException {
        var first = CommandSelector.select
                (CommandGenerator.newCommand("add-collaboration --name=ppp"), "uu");
        first.executeCommand();

        var reg = CommandSelector.select
                (CommandGenerator.newCommand("register --username=eeee --password=ewew"),
                        null);
        reg.executeCommand();

        var us = CommandSelector.select
                (CommandGenerator.newCommand("add-user --user=eeee --collaboration=ppp")
                        , "uu");
        us.executeCommand();

        var task = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=ddd --collaboration=ppp"),
                        "uu");
        task.executeCommand();

        var assign = CommandSelector.select
                (CommandGenerator.newCommand("assign-task --user=eeee --collaboration=ppp " +
                                "--task=ddd"),
                        "uu");

        assertEquals("Task assignment successful!",
                assign.executeCommand(),
                "A user must be assigned a task successfully");
    }
}