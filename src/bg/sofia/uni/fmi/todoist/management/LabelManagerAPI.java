package bg.sofia.uni.fmi.todoist.management;

import bg.sofia.uni.fmi.todoist.exception.TaskNotFoundException;

import java.util.Collection;

public interface LabelManagerAPI {

    /**
     * Adds a new label to a user's account
     * @param labelName the name of the label
     */
    public void addLabel(String labelName);

    /**
     * Deletes an existing label from the user's account
     * @param labelName the label which would be deleted
     */
    public void deleteLabel(String labelName);

    /**
     * Lists all labels created by the user
     * @return a collection containing all labels on the user's account
     */
    public Collection<String> listLabels();

    /**
     * Sets a label to an existing task
     * @param taskName the name of the task which would be associated with the label
     * @param taskDate the date of the task which would be associated with the label
     * @param labelName the label which would be set ot the task
     * @throws TaskNotFoundException if such task is not present
     */
    public void labelTask(String taskName, String taskDate, String labelName) throws TaskNotFoundException;

    /**
     * Retrieves all tasks associated to a given label
     * @param labelName the label which the tasks are associated to
     * @return a collection of tasks associated to the label
     */
    public Collection<String> listTasks(String labelName);

}
