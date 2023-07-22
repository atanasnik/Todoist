package bg.sofia.uni.fmi.todoist.task;

import bg.sofia.uni.fmi.todoist.user.User;
import bg.sofia.uni.fmi.todoist.validation.Validator;

import java.util.Set;

public class CollaborationTask extends Task {

    private static final String ASSIGNEE_NAME = "Assignee name";

    private String assignee;

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        Validator.stringArgument(assignee, ASSIGNEE_NAME);

        this.assignee = assignee;
    }

    @Override
    public String toString() {
        String result = super.toString();
        if (assignee != null) {
            result += ASSIGNEE_NAME + COLON + SPACE_CHARACTER + assignee + System.lineSeparator();
        }

        return result;
    }

    public static CollaborationTask of(Task task) {
        var build = CollaborationTask.builder(task.getName());

        if (task.getDate() != null) {
            build.setDate(task.getDate());
        }
        if (task.getDueDate() != null) {
            build.setDueDate(task.getDueDate());
        }
        if (task.getDescription() != null) {
            build.setDescription(task.getDescription());
        }

        CollaborationTask collaborationTask = build.build();
        collaborationTask.setCompleted(task.isCompleted());

        return collaborationTask;
    }

    public static CollaborationTaskBuilder builder(String name) {
        return new CollaborationTaskBuilder(name);
    }

    private CollaborationTask(CollaborationTaskBuilder builder) {
        super(builder);
        this.assignee = builder.assignee;
    }

    public static class CollaborationTaskBuilder extends TaskBuilder {

        private String assignee;

        private CollaborationTaskBuilder(String name) {
            super(name);
        }

        @Override
        public CollaborationTask build() {
            return new CollaborationTask(this);
        }
    }

}
