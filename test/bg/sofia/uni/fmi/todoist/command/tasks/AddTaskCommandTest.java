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

public class AddTaskCommandTest {
    @BeforeAll
    public static void setUp() throws InvalidCommandException {
        var reg = CommandSelector.select
                (CommandGenerator.newCommand("register --username=jj --password=ew"), null);
        reg.executeCommand();
    }
    @Test
    public void testAddTaskInvalidArgument() throws InvalidCommandException {
        Command cmd = CommandGenerator.newCommand("add-task --name=tasky --duedate=12.04.2023");
        var exec = CommandSelector.select(cmd, null);

        assertThrows(InvalidCommandException.class, () -> ArgumentChecker.assureArgsMatch(cmd, exec.getArgs()),
                "Adding a task with incorrect parameters must be handled successfully");
    }

    @Test
    public void testAddTimedTaskComplex() throws InvalidCommandException {
        var exec = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=tasky --due-date=13.04.2023" +
                        " --date=13.04.2023 --description=\"just a task\""), "jj");

        assertEquals("Task adding successful!", exec.executeCommand(),
                "Task must be added successfully");
    }

    @Test
    public void testAddInboxTask() throws InvalidCommandException {
        var exec = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=taskk" +
                        " --description=\"a description of a task\""), "jj");

        assertEquals("Task adding successful!", exec.executeCommand(),
                "Task must be added successfully");
    }

    @Test
    public void testAddInboxTaskSameName() throws InvalidCommandException {
        var first = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=original" +
                        " --description=\"an original description of a task\""), "jj");
        first.executeCommand();

        var second = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=original" +
                        " --description=\"an original description of a task as well\""), "jj");

        assertEquals("Task already exists.", second.executeCommand(),
                "Cannot have two tasks with the same name in the inbox");
    }

    @Test
    public void testAddTimedTaskSameNameDifferentDate() throws InvalidCommandException {
        var first = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=original" +
                        " --description=\"an original description of a task\" --date=03.03.2023"), "jj");
        first.executeCommand();

        var second = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=original --date=04.03.2023" +
                        " --description=\"an original description of a task as well\""), "jj");

        assertEquals("Task adding successful!", second.executeCommand(),
                "Should be able to have two timed tasks with the same name and different date");
    }

    @Test
    public void testAddTimedTaskSameNameSameDate() throws InvalidCommandException {
        var first = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=well" +
                        " --description=\"an original description of a task\" --date=01.03.2023"), "jj");
        first.executeCommand();

        var second = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=well --date=01.03.2023" +
                        " --description=\"an original description of a task as well\""), "jj");

        assertEquals("Task already exists.", second.executeCommand(),
                "Should not be able to have two timed tasks with the same name and the same date");
    }

    @Test
    public void testAddCollaborationTask() throws InvalidCommandException {
        var collab = CommandSelector.select
                (CommandGenerator.newCommand("add-collaboration --name=collab"), "jj");
        collab.executeCommand();
        var add = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=taskkkkk --collaboration=collab"),
                        "jj");
        assertEquals("Task adding successful!", add.executeCommand(),
                "Task must be added to collaboration successfully");

    }
}
