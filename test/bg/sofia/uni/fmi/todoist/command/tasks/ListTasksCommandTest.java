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

public class ListTasksCommandTest {
    @BeforeAll
    public static void setUp() throws InvalidCommandException {
        var reg = CommandSelector.select
                (CommandGenerator.newCommand("register --username=nn --password=pass"), null);
        reg.executeCommand();
    }

    @Test
    public void testListCollaborationTasks() throws InvalidCommandException {
        var collab = CommandSelector.select
                (CommandGenerator.newCommand("add-collaboration --name=ddd"), "fjlg");
        collab.executeCommand();
        var first = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=first --collaboration=ddd"),
                        "fjlg");
        first.executeCommand();
        var second = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=second --collaboration=ddd"),
                        "fjlg");
        second.executeCommand();

        var get = CommandSelector.select
                (CommandGenerator.newCommand("list-tasks --collaboration=ddd"),
                        "fjlg");

        String result = "Task name: first" + System.lineSeparator() +
                "Status: uncompleted" + System.lineSeparator() +
                System.lineSeparator() +
                "Task name: second" + System.lineSeparator() +
                "Status: uncompleted" + System.lineSeparator();

        assertEquals(result, get.executeCommand(),
                "Tasks must be retrieved successfully");
    }

    @Test
    public void testListTasksIncorrectParameters() throws InvalidCommandException {
        Command cmd = CommandGenerator.newCommand("list-tasks --name=name");
        var exec = CommandSelector.select(cmd, "nn");

        assertThrows(InvalidCommandException.class, () -> ArgumentChecker.assureArgsMatch(cmd, exec.getArgs()),
                "The user should be notified about an invalid command format");
    }

    @Test
    public void testListTasks() throws InvalidCommandException {
        var first = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=firsttask" +
                        " --description=\"a short description\""), "nn");
        first.executeCommand();

        var second = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=secondtask" +
                        " --description=\"a short description\""), "nn");
        second.executeCommand();

        var exec = CommandSelector.select
                (CommandGenerator.newCommand("list-tasks"), "nn");

        String result = "Task name: firsttask" + System.lineSeparator() +
                "Status: uncompleted" + System.lineSeparator() +
                "Description: a short description" + System.lineSeparator() +
                System.lineSeparator() +
                "Task name: secondtask" + System.lineSeparator() +
                "Status: uncompleted" + System.lineSeparator() +
                "Description: a short description" + System.lineSeparator();

        assertEquals(result, exec.executeCommand(),
                "Listing tasks with incorrect parameters must be handled successfully");
    }

    @Test
    public void testListDatedCompletedTasks() throws InvalidCommandException {
        var first = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=one --date=29.06.2023"),
                        "nn");
        first.executeCommand();

        var second = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=two --date=30.06.2023"),
                        "nn");
        second.executeCommand();

        var finish = CommandSelector.select
                (CommandGenerator.newCommand("finish-task --name=two --date=30.06.2023"),
                        "nn");
        finish.executeCommand();

        var exec = CommandSelector.select
                (CommandGenerator.newCommand("list-tasks --completed=true --date=30.06.2023"),
                        "nn");

        String result = "Task name: two" + System.lineSeparator() +
                "Status: completed" + System.lineSeparator() +
                "Date: 30.06.2023" + System.lineSeparator();

        assertEquals(result, exec.executeCommand(),
                "Listing tasks with must be handled successfully");
    }

    @Test
    public void testListTasksNoTasks() throws InvalidCommandException {
        var exec = CommandSelector.select
                (CommandGenerator.newCommand("list-tasks"),
                        "nn");

        String result = "No tasks found.";

        assertEquals(result, exec.executeCommand(),
                "Attempting to list tasks when there are none must be handled successfully");
    }
}
