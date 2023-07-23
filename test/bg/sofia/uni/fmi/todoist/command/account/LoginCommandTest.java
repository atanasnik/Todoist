package bg.sofia.uni.fmi.todoist.command.account;

import bg.sofia.uni.fmi.todoist.command.tools.ArgumentChecker;
import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.command.CommandGenerator;
import bg.sofia.uni.fmi.todoist.command.CommandSelector;
import bg.sofia.uni.fmi.todoist.exception.InvalidCommandException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoginCommandTest {
    @Test
    public void testLoginCorrectData() throws InvalidCommandException {
        var setUp = CommandSelector.select
                (CommandGenerator.newCommand("register --username=name --password=pass"), null);
        setUp.executeCommand();

        var exec = CommandSelector.select
                (CommandGenerator.newCommand("login --username=name --password=pass"),
                        null);

        assertEquals("Login successful!", exec.executeCommand(),
                "A valid registration command must be executed successfully");
    }

    @Test
    public void testLoginNoSuchUser() throws InvalidCommandException {
        var exec = CommandSelector.select
                (CommandGenerator.newCommand("login --username=noo --password=nooo"),
                        null);

        assertEquals("Failed access attempt. You have entered an invalid username and/or password. Try again.", exec.executeCommand(),
                "An invalid login attempt must be handled successfully");
    }

    @Test
    public void testLoginIncorrectPassword() throws InvalidCommandException {
        var setUp = CommandSelector.select
                (CommandGenerator.newCommand("register --username=qwerty --password=ytrewq"), null);
        setUp.executeCommand();

        var exec = CommandSelector.select
                (CommandGenerator.newCommand("login --username=qwerty --password=qwerty"), null);

        assertEquals("Failed access attempt. You have entered an invalid username and/or password. Try again.",
                exec.executeCommand(),
                "The user should be notified about a parameter that is not recognised for this command");
    }

    @Test
    public void testLoginIncorrectParameter() throws InvalidCommandException {
        Command cmd = CommandGenerator.newCommand("login --myname=hmm --password=nice");
        var exec = CommandSelector.select(cmd, null);

        assertThrows(InvalidCommandException.class, () -> ArgumentChecker.assureArgsMatch(cmd, exec.getArgs()),
                "The user should be notified about an invalid command format");
    }

    @Test
    public void testLogWhileLogged() throws InvalidCommandException {
        var exec = CommandSelector.select
                (CommandGenerator.newCommand("login --username=name --password=pass"),
                        "alreadyThere");
        assertEquals("You have already logged into an account.", exec.executeCommand(),
                "A login attempt while logged must be halted!");
    }
}
