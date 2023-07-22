package bg.sofia.uni.fmi.todoist.storage;

import bg.sofia.uni.fmi.todoist.exception.AlreadyExistsException;
import bg.sofia.uni.fmi.todoist.exception.NotFoundException;
import bg.sofia.uni.fmi.todoist.exception.TaskAlreadyExistsException;
import bg.sofia.uni.fmi.todoist.exception.TaskNotFoundException;
import bg.sofia.uni.fmi.todoist.task.Task;
import bg.sofia.uni.fmi.todoist.validation.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TimedTasksStorage implements TasksStorage {

    private static final String DATE = "Date";

    private final Map<String, Map<String, Task>> tasks;

    {
        tasks = new HashMap<>();
    }

    @Override
    public void add(String identifier, Task toAdd) throws AlreadyExistsException {
        Validator.stringArgument(identifier, TASK_NAME);
        Validator.objectArgument(toAdd, TASK_OBJECT);

        if (tasks.containsKey(toAdd.getDate()) && tasks.get(toAdd.getDate()).containsKey(identifier))
            throw new TaskAlreadyExistsException("Task with this name and this date already exists");

        tasks.putIfAbsent(toAdd.getDate(), new HashMap<>());
        tasks.get(toAdd.getDate()).put(toAdd.getName(), toAdd);
    }

    @Override
    public void remove(String identifier, String date) throws NotFoundException {
        Validator.stringArgument(identifier, TASK_NAME);
        Validator.objectArgument(date, DATE);

        if (!tasks.containsKey(date)) {
            throw new TaskNotFoundException("Task with such date not found");
        }

        if (!tasks.get(date).containsKey(identifier)) {
            throw new TaskNotFoundException("Task with such name not found");
        }

        tasks.get(date).remove(identifier);
    }

    @Override
    public Task getByIdentifier(String identifier, String date) throws NotFoundException {
        Validator.stringArgument(identifier, TASK_NAME);
        Validator.objectArgument(date, DATE);

        if (!tasks.containsKey(date)) {
            throw new TaskNotFoundException("Task with such date not found");
        }

        if (!tasks.get(date).containsKey(identifier)) {
            throw new TaskNotFoundException("Task with such name not found");
        }

        return tasks.get(date).get(identifier);
    }

    @Override
    public boolean contains(String name, Task task) {
        return tasks.containsKey(task.getDate()) && tasks.get(task.getDate()).containsKey(name);
    }

    public Task containsName(String name) {
        return tasks.values()
                .stream()
                .flatMap(map -> map.values().stream())
                .filter(task -> task.getName().equals(name))
                .findAny().orElse(null);

        //Note: finds any name
    }

    @Override
    public Set<Task> list() {
        return tasks.values()
                .stream()
                .flatMap(map -> map.values().stream())
                .collect(Collectors.toSet());
    }

}
