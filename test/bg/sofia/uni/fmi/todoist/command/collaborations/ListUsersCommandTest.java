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

public class ListUsersCommandTest {
    @BeforeAll
    public static void setUp() throws InvalidCommandException {
        var reg = CommandSelector.select
                (CommandGenerator.newCommand("register --username=vv --password=ew"), null);
        reg.executeCommand();
    }

    @Test
    public void testListCollaborationsInvalidArgument() throws InvalidCommandException {
        Command cmd = CommandGenerator.newCommand("list-users --name=yes");
        var exec = CommandSelector.select(cmd, null);

        assertThrows(InvalidCommandException.class, () -> ArgumentChecker.assureArgsMatch(cmd, exec.getArgs()),
                "Listing users with incorrect parameters must be handled successfully");
    }
    @Test
    public void testListCollaborationUsers() throws InvalidCommandException {
        var first = CommandSelector.select
                (CommandGenerator.newCommand("add-collaboration --name=yyy"), "vv");
        first.executeCommand();

        var reg1 = CommandSelector.select
                (CommandGenerator.newCommand("register --username=yy1 --password=ewew"),
                        null);
        reg1.executeCommand();

        var reg2 = CommandSelector.select
                (CommandGenerator.newCommand("register --username=yy2 --password=ewew"),
                        null);
        reg2.executeCommand();

        var us1 = CommandSelector.select
                (CommandGenerator.newCommand("add-user --user=yy1 --collaboration=yyy")
                        , "vv");
        us1.executeCommand();

        var us2 = CommandSelector.select
                (CommandGenerator.newCommand("add-user --user=yy2 --collaboration=yyy")
                        , "vv");
        us2.executeCommand();

        var list = CommandSelector.select
                (CommandGenerator.newCommand("list-users --collaboration=yyy")
                        , "vv");

        assertEquals("vv" + System.lineSeparator() + "yy1" +
                        System.lineSeparator() + "yy2" + System.lineSeparator(),
                list.executeCommand(),
                "All users must be listed successfully");
    }
}
