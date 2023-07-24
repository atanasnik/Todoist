package bg.sofia.uni.fmi.todoist;

import bg.sofia.uni.fmi.todoist.command.Command;
import bg.sofia.uni.fmi.todoist.command.CommandGenerator;
import bg.sofia.uni.fmi.todoist.exception.InvalidCommandException;
import bg.sofia.uni.fmi.todoist.server.Server;

import java.util.Scanner;

public class Todoist {
    private static final String START = "start";
    private static final String STOP = "stop";
    private static final String INVALID_COMMAND = "Invalid command!";
    private static final int PORT = 8437;
    public static void main(String... args) throws InvalidCommandException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        Server server = new Server(PORT);

        String clientInput;

        while (true) {
            clientInput = scanner.nextLine();

            Command cmd = CommandGenerator.newCommand(clientInput);

            if (cmd.command().equals(START)) {
                server.start();
            } else if (cmd.command().equals(STOP)) {
                server.halt();
                server.join();
                break;
            } else {
                System.out.println(INVALID_COMMAND);
            }
        }
    }

}
