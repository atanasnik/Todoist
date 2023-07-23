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

public class UpdateTaskCommandTest {
    @BeforeAll
    public static void setUp() throws InvalidCommandException {
        var reg = CommandSelector.select
                (CommandGenerator.newCommand("register --username=ll --password=ew"), null);
        reg.executeCommand();
    }
    @Test
    public void testUpdateTaskIncorrectParameters() throws InvalidCommandException {
        Command cmd = CommandGenerator.newCommand("update-task");
        var exec = CommandSelector.select(cmd, "ll");

        assertThrows(InvalidCommandException.class, () -> ArgumentChecker.assureArgsMatch(cmd, exec.getArgs()),
                "The user should be notified about an invalid command format");
    }

    @Test
    public void testUpdateTaskCorrect() throws InvalidCommandException {
        var add = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=toupdate"), "ll");
        add.executeCommand();
        var exec = CommandSelector.select
                (CommandGenerator
                        .newCommand("update-task --name=toupdate --date=14.03.2023"), "ll");

        assertEquals("Task updating successful!", exec.executeCommand(),
                "Attempt to finish non-existent task must be handled successfully");
    }

    @Test
    public void testUpdateCollaborationTask() throws InvalidCommandException {
        var collab = CommandSelector.select
                (CommandGenerator.newCommand("add-collaboration --name=eee"), "ll");
        collab.executeCommand();
        var add = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=taskkkkk --collaboration=eee"), "ll");
        add.executeCommand();

        var upd = CommandSelector.select
                (CommandGenerator.newCommand("update-task --name=taskkkkk " +
                        "--date=12.04.2023 --collaboration=eee"), "ll");

        assertEquals("Task updating successful!", upd.executeCommand(),
                "Task must be added to collaboration successfully");
    }
    @Test
    public void testUpdateTimedTaskCorrect() throws InvalidCommandException {
        var add = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=toupdate2 --date=15.03.2023"), "ll");
        add.executeCommand();
        var exec = CommandSelector.select
                (CommandGenerator
                        .newCommand("update-task --name=toupdate2"), "ll");

        assertEquals("Task updating successful!", exec.executeCommand(),
                "Attempt to finish non-existent task must be handled successfully");
    }
}
