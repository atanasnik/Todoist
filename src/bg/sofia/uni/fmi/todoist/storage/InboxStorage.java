package bg.sofia.uni.fmi.todoist.storage;

import bg.sofia.uni.fmi.todoist.exception.AlreadyExistsException;
import bg.sofia.uni.fmi.todoist.exception.NotFoundException;
import bg.sofia.uni.fmi.todoist.exception.TaskAlreadyExistsException;
import bg.sofia.uni.fmi.todoist.exception.TaskNotFoundException;
import bg.sofia.uni.fmi.todoist.task.Task;
import bg.sofia.uni.fmi.todoist.validation.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InboxStorage implements TasksStorage {

    private final Map<String, Task> tasks;

    {
        tasks = new HashMap<>();
    }

    @Override
    public void add(String identifier, Task toAdd) throws AlreadyExistsException {
        Validator.stringArgument(identifier, TASK_NAME);
        Validator.objectArgument(toAdd, TASK_OBJECT);

        if (tasks.containsKey(identifier)) {
            throw new TaskAlreadyExistsException("Task already exists");
        }

        tasks.put(identifier, toAdd);
    }

    @Override
    public Set<Task> list() {
        return Set.copyOf(tasks.values());
    }

    @Override
    public void remove(String identifier, String date) throws NotFoundException {
        Validator.stringArgument(identifier, TASK_NAME);

        if (!tasks.containsKey(identifier)) {
            throw new TaskNotFoundException("Task not found");
        }

        tasks.remove(identifier);
    }

    @Override
    public Task getByIdentifier(String identifier, String date) throws NotFoundException {
        Validator.stringArgument(identifier, TASK_NAME);

        if (!tasks.containsKey(identifier)) {
            throw new TaskNotFoundException("Task not found");
        }

        return tasks.get(identifier);
    }

    @Override
    public boolean contains(String name, Task task) {
        return tasks.containsKey(name);
    }
}
