package bg.sofia.uni.fmi.todoist.command;

import java.util.Map;

public record Command(String command, Map<String, String> arguments) {
}
