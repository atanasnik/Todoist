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

public class GetTaskCommandTest {
    @BeforeAll
    public static void setUp() throws InvalidCommandException {
        var reg = CommandSelector.select
                (CommandGenerator.newCommand("register --username=mm --password=pass"), null);
        reg.executeCommand();
    }

    @Test
    public void testGetTaskInvalidArgument() throws InvalidCommandException {
        Command cmd = CommandGenerator.newCommand("get-task --ame=invalid");
        var exec = CommandSelector.select(cmd, "mm");

        assertThrows(InvalidCommandException.class, () -> ArgumentChecker.assureArgsMatch(cmd, exec.getArgs()),
                "The user should be notified about an invalid command format");
    }

    @Test
    public void testGetTaskCorrect() throws InvalidCommandException {
        var first = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=smalltask" +
                        " --description=\"a short description\""), "mm");
        first.executeCommand();

        var second = CommandSelector.select
                (CommandGenerator.newCommand("get-task --name=smalltask"), "mm");

        assertEquals("Task name: smalltask" + System.lineSeparator() +
                        "Status: uncompleted" + System.lineSeparator() +
                        "Description: a short description" + System.lineSeparator(), second.executeCommand(),
                "Task must be presented correctly");
    }

    @Test
    public void testGetCollaborationTask() throws InvalidCommandException {
        var collab = CommandSelector.select
                (CommandGenerator.newCommand("add-collaboration --name=fff"), "mm");
        collab.executeCommand();
        var add = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=toget --collaboration=fff"), "mm");
        add.executeCommand();
        var get = CommandSelector.select
                (CommandGenerator.newCommand("get-task --name=toget --collaboration=fff"), "mm");

        String result = "Task name: toget" + System.lineSeparator() +
                "Status: uncompleted" + System.lineSeparator();
        assertEquals(result, get.executeCommand(),
                "Task must be retrieved successfully");
    }

    @Test
    public void testGetTimedTaskCorrect() throws InvalidCommandException {
        var first = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=bigtask" +
                        " --date=30.04.2024"), "mm");
        first.executeCommand();

        var second = CommandSelector.select
                (CommandGenerator.newCommand("get-task --name=bigtask --date=30.04.2024"), "mm");

        assertEquals("Task name: bigtask" + System.lineSeparator() +
                        "Status: uncompleted" + System.lineSeparator() +
                        "Date: 30.04.2024" + System.lineSeparator(), second.executeCommand(),
                "Task must be presented correctly");
    }

    @Test
    public void testGetTaskNonExisting() throws InvalidCommandException {
        var exec = CommandSelector.select
                (CommandGenerator.newCommand("get-task --name=nothere"), "mm");

        assertEquals("Task not found.", exec.executeCommand(),
                "Non-existing task cannot be retrieved");
    }
}
