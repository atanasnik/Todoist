package bg.sofia.uni.fmi.todoist.server;

import bg.sofia.uni.fmi.todoist.command.*;
import bg.sofia.uni.fmi.todoist.command.tools.ArgumentChecker;
import bg.sofia.uni.fmi.todoist.exception.InvalidCommandException;
import bg.sofia.uni.fmi.todoist.storage.CollaborationsStorage;
import bg.sofia.uni.fmi.todoist.storage.UsersStorage;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Server extends Thread {
    private static final int BUFFER_SIZE = 2048;
    private static final String HOST = "localhost";
    Gson gson = new Gson();

    private static final String INVALID_COMMAND = "Invalid command!";
    private static final String FAILED_ACCESS_ATTEMPT = "Failed access attempt. ";
    private static final String FILE_NOT_FOUND = "An error occurred due to a file not being found: ";
    private static final String IO_ERROR = "An error occurred upon performing I/O operation: ";

    private static final String USERS_STORAGE_PATH = "database/usersStorage.json";
    private static final String COLLABORATIONS_STORAGE_PATH = "database/collaborationsStorage.json";
    private static final String LOGS_PATH = "logs/appLogs.txt";

    private static final String COLON = ": ";
    private CommandExecutor commandExecutor;
    private Map<SocketChannel, String> clients;
    private static Logger logger;
    private final int port;
    private boolean isServerWorking;

    private ByteBuffer buffer;
    private Selector selector;

    public Server(int port) {
        this.port = port;
        this.clients = new HashMap<>();

        logger = Logger.getLogger(this.getClass().getName());
        initFileHandler();
    }

    @Override
    public void run() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            System.out.println("Server is working");

            readData();

            selector = Selector.open();
            configureServerSocketChannel(serverSocketChannel, selector);
            this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
            isServerWorking = true;

            while (isServerWorking) {
                try {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) {
                        continue;
                    }

                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();

                        if (key.isReadable()) {
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            String clientInput = null;

                            try {
                                clientInput = getClientInput(clientChannel);
                            } catch (IOException e) {
                                clientChannel.close();
                            }

                            if (clientInput == null) {
                                continue;
                            }

                            Command cmd = null;
                            String output;

                            try {
                                cmd = CommandGenerator.newCommand(clientInput);
                                commandExecutor =
                                        CommandSelector.select(cmd, clients.getOrDefault(clientChannel, null));

                                ArgumentChecker.assureArgsMatch(cmd, commandExecutor.getArgs());

                                output = commandExecutor.executeCommand();

                                if (!output.contains(FAILED_ACCESS_ATTEMPT)) {
                                    renameClient(clientChannel, cmd);
                                    saveData();
                                }
                            } catch (InvalidCommandException e) {
                                output = INVALID_COMMAND;
                            }

                            System.out.println(clients.get(clientChannel) + COLON + clientInput);
                            writeClientOutput(clientChannel, output);
                        } else if (key.isAcceptable()) {
                            accept(selector, key);
                        }

                        keyIterator.remove();
                    }
                } catch (IOException e) {
                    System.out.println("Error occurred while processing client request: " + e.getMessage());
                    logger.log(Level.SEVERE, IO_ERROR + e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, IO_ERROR + e.getMessage(), e);
        }
    }

    private void renameClient(SocketChannel socketChannel, Command cmd) {
        if (cmd.command().equals("register") || cmd.command().equals("login")) {
            clients.put(socketChannel, cmd.arguments().get("username"));
        }
    }

    public void halt() {
        this.isServerWorking = false;

        if (selector.isOpen()) {
            selector.wakeup();
        }

        System.out.println("Server is being stopped");
    }

    private void configureServerSocketChannel(ServerSocketChannel channel, Selector selector) throws IOException {
        channel.bind(new InetSocketAddress(HOST, this.port));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        buffer.clear();
        buffer.put(output.getBytes());
        buffer.flip();

        clientChannel.write(buffer);
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }

    private void saveData() {
        saveUsersStorage();
        saveCollaborationsStorage();
    }

    private void saveUsersStorage() {
        String usersJson = gson.toJson(commandExecutor.usersStorage());

        try (Writer writer = new FileWriter(USERS_STORAGE_PATH)) {
            writer.write(usersJson);
        } catch (IOException e) {
            logger.log(Level.SEVERE, IO_ERROR + e.getMessage(), e);
        }
    }

    private void saveCollaborationsStorage() {
        String usersJson = gson.toJson(commandExecutor.collaborationsStorage());

        try (Writer writer = new FileWriter(COLLABORATIONS_STORAGE_PATH)) {
            writer.write(usersJson);
        } catch (IOException e) {
            logger.log(Level.SEVERE, IO_ERROR + e.getMessage(), e);
        }
    }

    private void readData() {
        createFileIfDoesNotExist(USERS_STORAGE_PATH);
        if (!isFileEmpty(USERS_STORAGE_PATH)) {
            readUsersStorage();
        }

        createFileIfDoesNotExist(COLLABORATIONS_STORAGE_PATH);
        if (!isFileEmpty(COLLABORATIONS_STORAGE_PATH)) {
            readCollaborationsStorage();
        }
    }

    private void readUsersStorage() {
        try (var fileReader = new BufferedReader(new FileReader(USERS_STORAGE_PATH))) {
            String usersJson = "";
            String line;

            while ((line = fileReader.readLine()) != null) {
                usersJson += line;
            }

            CommandExecutor.setUsersStorage(gson.fromJson(usersJson, UsersStorage.class));
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, FILE_NOT_FOUND + e.getMessage(), e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, IO_ERROR + e.getMessage(), e);
        }
    }

    private void readCollaborationsStorage() {
        try (var fileReader = new BufferedReader(new FileReader(COLLABORATIONS_STORAGE_PATH))) {
            String collaborationsJson = "";
            String line;

            while ((line = fileReader.readLine()) != null) {
                collaborationsJson += line;
            }

            CommandExecutor.setCollaborationsStorage(gson.fromJson(collaborationsJson, CollaborationsStorage.class));
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, FILE_NOT_FOUND + e.getMessage(), e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, IO_ERROR + e.getMessage(), e);
        }
    }

    private boolean isFileEmpty(String fileName) {
        Path path = Paths.get(fileName);

        try {
            return (Files.size(path) == 0);
        } catch (IOException e) {
            logger.log(Level.SEVERE, IO_ERROR + e.getMessage(), e);
        }

        return false;
    }

    private void initFileHandler() {
        FileHandler fileHandler = null;
        createFileIfDoesNotExist(LOGS_PATH);

        try {
            fileHandler = new FileHandler(LOGS_PATH);
        } catch (IOException e) {
            throw new RuntimeException(IO_ERROR, e);
        }

        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
    }

    private void createFileIfDoesNotExist(String fileName) {
        Path path = Paths.get(fileName);

        if (Files.exists(path)) {
            return;
        }

        try {
            Path parentDir = path.getParent();

            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }

            Files.createFile(path);
            System.out.println("New file created: " + fileName);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, IO_ERROR + ex.getMessage(), ex);
        }
    }
}