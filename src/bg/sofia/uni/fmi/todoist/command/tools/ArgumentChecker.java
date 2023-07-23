package bg.sofia.uni.fmi.todoist.command.tools;

import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.exception.InvalidCommandException;

import java.util.Map;

public class ArgumentChecker {

    private ArgumentChecker() {
    }

    public static void assureArgsMatch(Command cmd, Map<String, Boolean> argNames) throws InvalidCommandException {
        if (argNames == null) {
            if (!cmd.arguments().isEmpty()) {
                throw new InvalidCommandException("Presence of incorrect arguments");
            }
            return;
        }

        int argsFound = 0;
        for (var entry : argNames.entrySet()) {
            if (!cmd.arguments().containsKey(entry.getKey()) && entry.getValue()) {
                throw new InvalidCommandException("Missing required argument");
            }

            if (cmd.arguments().containsKey(entry.getKey())) {
                ++argsFound;
            }
        }

        if (argsFound < cmd.arguments().size()) {
            throw new InvalidCommandException("Presence of incorrect arguments");
        }
    }
}
