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

public class ListCollaborationsCommandTest {
    @BeforeAll
    public static void setUp() throws InvalidCommandException {
        var reg = CommandSelector.select
                (CommandGenerator.newCommand("register --username=ss --password=ew"), null);
        reg.executeCommand();
    }

    @Test
    public void testListCollaborationsInvalidArgument() throws InvalidCommandException {
        Command cmd = CommandGenerator.newCommand("list-collaborations --name=yes");
        var exec = CommandSelector.select(cmd, null);

        assertThrows(InvalidCommandException.class, () -> ArgumentChecker.assureArgsMatch(cmd, exec.getArgs()),
                "Listing collaborations with incorrect parameters must be handled successfully");
    }

    @Test
    public void testListCollaborations() throws InvalidCommandException {
        var first = CommandSelector.select
                (CommandGenerator.newCommand("add-collaboration --name=ggg"), "ss");
        first.executeCommand();

        var add = CommandSelector.select
                (CommandGenerator.newCommand("add-task --name=atask --collaboration=ggg"),
                        "ss");
        add.executeCommand();

        var second = CommandSelector.select
                (CommandGenerator.newCommand("add-collaboration --name=ddd"), "ss");
        second.executeCommand();

        var list = CommandSelector.select
                (CommandGenerator.newCommand("list-collaborations"), "ss");

        String result = "Collaboration name: ddd" + System.lineSeparator() +
                "Owner name: ss" + System.lineSeparator() +
                "Tasks:" + System.lineSeparator() + System.lineSeparator() +
                "Collaborators:" + System.lineSeparator() +
                "ss" + System.lineSeparator() + System.lineSeparator() +
                "Collaboration name: ggg" + System.lineSeparator() +
                "Owner name: ss" + System.lineSeparator() +
                "Tasks:" + System.lineSeparator() +
                "Task name: atask" + System.lineSeparator() +
                "Status: uncompleted" + System.lineSeparator() + System.lineSeparator() +
                "Collaborators:" + System.lineSeparator() +
                "ss" + System.lineSeparator();

        assertEquals(result,
                list.executeCommand(),
                "Retrieving collaborations must be done successfully");
    }
}
