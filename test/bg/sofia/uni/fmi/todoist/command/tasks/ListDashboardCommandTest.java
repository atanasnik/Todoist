package bg.sofia.uni.fmi.todoist.command.tasks;

import bg.sofia.uni.fmi.todoist.command.tools.ArgumentChecker;
import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.command.CommandGenerator;
import bg.sofia.uni.fmi.todoist.command.CommandSelector;
import bg.sofia.uni.fmi.todoist.exception.InvalidCommandException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ListDashboardCommandTest {
    @BeforeAll
    public static void setUp() throws InvalidCommandException {
        var reg = CommandSelector.select
                (CommandGenerator.newCommand("register --username=oo --password=pass"), null);
        reg.executeCommand();
    }

    @Test
    public void testListDashboardIncorrectParameters() throws InvalidCommandException {
        Command cmd = CommandGenerator.newCommand("list-dashboard --name=name");
        var exec = CommandSelector.select(cmd, "oo");

        assertThrows(InvalidCommandException.class, () -> ArgumentChecker.assureArgsMatch(cmd, exec.getArgs()),
                "The user should be notified about an invalid command format");
    }

    @Test
    public void testListDashboardCorrect() throws InvalidCommandException {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = now.format(formatter);

        var add = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=one --date=" + formattedDate),
                        "oo");
        add.executeCommand();

        var exec = CommandSelector.select
                (CommandGenerator.newCommand("list-dashboard"), "oo");

        String result = "Task name: one" + System.lineSeparator() +
                "Status: uncompleted" + System.lineSeparator() +
                "Date: " + LocalDate.now().format(formatter) + System.lineSeparator();

        assertEquals(result, exec.executeCommand(),
                "Listing dashboard must be performed successfully");
    }

    @Test
    public void testListDashboardEmpty() throws InvalidCommandException {
        var reg = CommandSelector.select
                    (CommandGenerator.newCommand("register --username=cc --password=pass"), null);
        reg.executeCommand();

        var exec = CommandSelector.select
                (CommandGenerator.newCommand("list-dashboard"), "cc");

        assertEquals("No tasks found.", exec.executeCommand(),
                "Attempting to list dashboard while there are no tasks must be handled successfully");
    }
}
