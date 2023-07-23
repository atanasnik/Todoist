package bg.sofia.uni.fmi.todoist.command.account;

import bg.sofia.uni.fmi.todoist.command.tools.ArgumentChecker;
import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.command.CommandGenerator;
import bg.sofia.uni.fmi.todoist.command.CommandSelector;
import bg.sofia.uni.fmi.todoist.exception.InvalidCommandException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RegisterCommandTest {
    @Test
    public void testRegisterCorrectData() throws InvalidCommandException {
        var exec = CommandSelector.select
                (CommandGenerator.newCommand("register --username=yes --password=no"), null);
        assertEquals("Registration successful!", exec.executeCommand(),
                "A valid registration command must be executed successfully!");
    }

    @Test
    public void testRegisterWhileLogged() throws InvalidCommandException {
        var exec = CommandSelector.select
                (CommandGenerator.newCommand("register --username=name --password=pass"),
                        "alreadyThere");
        assertEquals("You have already logged into an account.", exec.executeCommand(),
                "A registration attempt while logged must be halted!");
    }

    @Test
    public void testRegisterAnExistingAccount() throws InvalidCommandException {
        var setUp = CommandSelector.select
                (CommandGenerator.newCommand("register --username=hmm --password=nice"), null);
        setUp.executeCommand();

        var exec = CommandSelector.select
                (CommandGenerator.newCommand("register --username=hmm --password=nice"), null);

        assertEquals("Failed access attempt. Username \"hmm\" already exists. Try another username.", exec.executeCommand(),
                "A username that already exists should not be replaced by a new registration");
    }

    @Test
    public void testRegisterIncorrectParameter() throws InvalidCommandException {
        Command cmd = CommandGenerator.newCommand("register --myname=hmm --password=nice");
        var exec = CommandSelector.select(cmd, null);

        assertThrows(InvalidCommandException.class, () -> ArgumentChecker.assureArgsMatch(cmd, exec.getArgs()),
                "The user should be notified about an invalid command format");
    }
}
