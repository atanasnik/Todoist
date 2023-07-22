package bg.sofia.uni.fmi.todoist.storage;

import bg.sofia.uni.fmi.todoist.exception.NotFoundException;
import bg.sofia.uni.fmi.todoist.task.Task;


public interface TasksStorage extends Storage<Task> {

    //Package-private constants
    static final String TASK_NAME = "Task name";
    static final String TASK_OBJECT = "Task";

    public void remove(String identifier, String date) throws NotFoundException;

    public Task getByIdentifier(String identifier, String date) throws NotFoundException;

    public boolean contains(String name, Task task);

}
