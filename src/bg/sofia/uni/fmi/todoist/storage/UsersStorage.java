package bg.sofia.uni.fmi.todoist.storage;

import bg.sofia.uni.fmi.todoist.exception.AlreadyExistsException;
import bg.sofia.uni.fmi.todoist.exception.NotFoundException;
import bg.sofia.uni.fmi.todoist.exception.UserAlreadyExistsException;
import bg.sofia.uni.fmi.todoist.exception.UserNotFoundException;
import bg.sofia.uni.fmi.todoist.exception.WrongPasswordException;
import bg.sofia.uni.fmi.todoist.user.User;
import bg.sofia.uni.fmi.todoist.validation.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UsersStorage {

    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";

    private final Map<String, User> users;

    {
        users  = new HashMap<>();
    }

    public void add(String identifier, User toAdd) throws UserAlreadyExistsException {
        if (users.containsKey(identifier)) {
            throw new UserAlreadyExistsException("User already exists");
        }

        users.put(identifier, toAdd);
    }

    public Set<User> list() {
        return Set.copyOf(users.values());
    }

    public User getByIdentifier(String identifier) {
        return users.get(identifier);
    }

    public void register(String username, String password) throws UserAlreadyExistsException {
        Validator.stringArgument(username, USERNAME);
        Validator.stringArgument(password, PASSWORD);

        if (users.containsKey(username)) {
            throw new UserAlreadyExistsException("The user account already exists");
        }

        users.put(username, new User(username, password,
                new TimedTasksStorage(), new InboxStorage(), new CollaborationsStorage()));
    }

    public void login(String username, String password) throws UserNotFoundException, WrongPasswordException {
        Validator.stringArgument(username, USERNAME);
        Validator.stringArgument(password, PASSWORD);

        if (!users.containsKey(username)) {
            throw new UserNotFoundException("The user account does not exist");
        }

        if (!users.get(username).checkPassword(password)) {
            throw new WrongPasswordException("Password is incorrect");
        }
    }

}
