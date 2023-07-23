package bg.sofia.uni.fmi.todoist.command;


import bg.sofia.uni.fmi.todoist.exception.InvalidCommandException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandGeneratorTest {

    @Test
    public void testExecuteCommandNumberOfArgumentsParsed() throws InvalidCommandException {
        assertEquals(4, CommandGenerator.newCommand("add-task --name=<name> --date=<date>" +
                " --due-date=<due-date> --description=<description>").arguments().size());
    }
}
