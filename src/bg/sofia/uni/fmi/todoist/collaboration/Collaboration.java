package bg.sofia.uni.fmi.todoist.collaboration;

import bg.sofia.uni.fmi.todoist.storage.CollaborationsStorage;
import bg.sofia.uni.fmi.todoist.storage.InboxStorage;
import bg.sofia.uni.fmi.todoist.storage.TimedTasksStorage;
import bg.sofia.uni.fmi.todoist.task.CollaborationTask;
import bg.sofia.uni.fmi.todoist.user.User;
import bg.sofia.uni.fmi.todoist.validation.Validator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Collaboration {

    private static final String COLON = ":";
    private static final String NAME_PREFIX = "Collaboration name";
    private static final String OWNER_PREFIX = "Owner name";
    private static final String TASKS_PREFIX = "Tasks";
    private static final String COLLABORATORS_PREFIX = "Collaborators";
    private static final String SPACE_CHARACTER = " ";
    private static final String NAME = "Name";
    private static final String OWNER = "Owner";
    private String name;
    private String owner;
    private Map<String, CollaborationTask> tasks;
    private Set<String> collaborators;

    public Collaboration(String name, String owner) {

        Validator.stringArgument(name, NAME);
        Validator.stringArgument(owner, OWNER);

        this.name = name;
        this.owner = owner;


        tasks = new HashMap<>();
        collaborators = new HashSet<>();
    }

    public String name() {
        return name;
    }

    public String owner() {
        return owner;
    }

    public Map<String, CollaborationTask> tasks() {
        return tasks;
    }

    public Set<String> collaborators() {
        return collaborators;
    }

    @Override
    public String toString() {
        StringBuffer information = new StringBuffer(NAME_PREFIX);

        information
                .append(COLON)
                .append(SPACE_CHARACTER)
                .append(name)
                .append(System.lineSeparator());

        information
                .append(OWNER_PREFIX)
                .append(COLON)
                .append(SPACE_CHARACTER)
                .append(owner)
                .append(System.lineSeparator());

        information
                .append(TASKS_PREFIX)
                .append(COLON)
                .append(System.lineSeparator());

        tasks.values().stream().forEach(task -> information.append(task.toString()));

        information
                .append(System.lineSeparator())
                .append(COLLABORATORS_PREFIX)
                .append(COLON)
                .append(System.lineSeparator());

        collaborators.stream().forEach(information::append);

        information.append(System.lineSeparator());

        return information.toString();
    }
}
