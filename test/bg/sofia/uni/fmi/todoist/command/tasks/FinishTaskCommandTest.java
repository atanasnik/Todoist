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

public class FinishTaskCommandTest {
    @BeforeAll
    public static void setUp() throws InvalidCommandException {
        var reg = CommandSelector.select
                (CommandGenerator.newCommand("register --username=pp --password=pass"), null);
        reg.executeCommand();
    }

    @Test
    public void testFinishTaskIncorrectParameters() throws InvalidCommandException {
        Command cmd = CommandGenerator.newCommand("finish-task");
        var exec = CommandSelector.select(cmd, "pp");

        assertThrows(InvalidCommandException.class, () -> ArgumentChecker.assureArgsMatch(cmd, exec.getArgs()),
                "The user should be notified about an invalid command format");
    }

    @Test
    public void testFinishInboxTaskCorrect() throws InvalidCommandException {
        var add = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=tofinish"), "pp");
        add.executeCommand();

        var exec = CommandSelector.select
                (CommandGenerator.newCommand("finish-task --name=tofinish"), "pp");

        String result = "Task completed!";

        assertEquals(result, exec.executeCommand(),
                "Task must be completed successfully");
    }

    @Test
    public void testFinishTaskNonExistent() throws InvalidCommandException {
        var exec = CommandSelector.select
                (CommandGenerator.newCommand("finish-task --name=notcreatedyet"), "pp");

        String result = "Task not found.";

        assertEquals(result, exec.executeCommand(),
                "Attempt to finish non-existent task must be handled successfully");
    }

    @Test
    public void testFinishCollaborationTask() throws InvalidCommandException {
        var collab = CommandSelector.select
                (CommandGenerator.newCommand("add-collaboration --name=zzz"), "pp");
        collab.executeCommand();
        var add = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=tttask --collaboration=zzz"),
                        "pp");
        add.executeCommand();

        var fin = CommandSelector.select
                (CommandGenerator.newCommand("finish-task --name=tttask " +
                        "--collaboration=zzz"), "pp");

        assertEquals("Task completed!", fin.executeCommand(),
                "Task must be completed successfully");
    }
}
