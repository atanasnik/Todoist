package bg.sofia.uni.fmi.todoist.command;

import bg.sofia.uni.fmi.todoist.exception.InvalidCommandException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandGenerator {

    private static final String EQUALS = "=";
    private static final Character DASH = '-';

    private static List<String> getCommandArguments(String input) {
        List<String> tokens = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();

        boolean insideQuote = false;

        for (char c : input.toCharArray()) {
            if (c == '"') {
                insideQuote = !insideQuote;
            }
            if (c == ' ' && !insideQuote) { //when space is not inside quote split
                tokens.add(buffer.toString().replace("\"", "")); //token is ready, lets add it to list
                buffer.delete(0, buffer.length()); //and reset StringBuilder`s content
            } else {
                buffer.append(c); //else add character to token
            }
        }
        //lets not forget about last token that doesn't have space after it
        tokens.add(buffer.toString().replace("\"", ""));

        return tokens;
    }

    public static Command newCommand(String clientInput) throws InvalidCommandException {
        List<String> tokens = getCommandArguments(clientInput);
        String[] args = tokens.subList(1, tokens.size()).toArray(new String[0]);
        Map<String, String> parsedArgs = parseEquations(args);

        return new Command(tokens.get(0).strip().toLowerCase(), parsedArgs);
    }

    private static Map<String, String> parseEquations(String[] equations) throws InvalidCommandException {
        Map<String, String> result = new HashMap<>();
        for (String equation : equations) {
            var pair = equation.strip().split(EQUALS);
            if (pair.length == 2 && pair[0].charAt(0) == DASH && pair[0].charAt(1) == DASH) {
                result.put(pair[0].toLowerCase().substring(2), pair[1].toLowerCase());
            } else {
                throw new InvalidCommandException("Invalid command");
            }
        }
        return result;
    }

}
