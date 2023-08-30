package bg.sofia.uni.fmi.todoist.task;

import bg.sofia.uni.fmi.todoist.validation.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected final String name;
    protected boolean isCompleted = false;

    //optional parameters
    protected final String date;
    protected final String dueDate;
    protected final String description;

    protected static final String COLON = ":";
    private static final String NAME_PREFIX = "Task name";
    private static final String DATE_PREFIX = "Date";
    private static final String DUE_DATE_PREFIX = "Due date";
    private static final String DESCRIPTION_PREFIX = "Description";
    protected static final String SPACE_CHARACTER = " ";
    private static final String STATUS_PREFIX = "Status";
    private static final String STATUS_COMPLETED = "completed";
    private static final String STATUS_UNCOMPLETED = "uncompleted";

    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public String getDate() {
        return date;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
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
                .append(STATUS_PREFIX)
                .append(COLON)
                .append(SPACE_CHARACTER)
                .append(isCompleted ? STATUS_COMPLETED : STATUS_UNCOMPLETED)
                .append(System.lineSeparator());

        if (date != null) {
            information
                    .append(DATE_PREFIX)
                    .append(COLON)
                    .append(SPACE_CHARACTER)
                    .append(date)
                    .append(System.lineSeparator());
        }

        if (dueDate != null) {
            information
                    .append(DUE_DATE_PREFIX)
                    .append(COLON)
                    .append(SPACE_CHARACTER)
                    .append(dueDate)
                    .append(System.lineSeparator());
        }

        if (description != null) {
            information
                    .append(DESCRIPTION_PREFIX)
                    .append(COLON)
                    .append(SPACE_CHARACTER)
                    .append(description)
                    .append(System.lineSeparator());
        }

        return information.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Task task = (Task) o;

        if (!name.equals(task.name)) {
            return false;
        }
        return Objects.equals(date, task.date);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    public static TaskBuilder builder(String name) {
        Validator.stringArgument(name, NAME_PREFIX);

        return new TaskBuilder(name);
    }

    //builder design pattern realisation bellow
    protected Task(TaskBuilder builder) {
        this.name = builder.name;
        this.date = builder.date;
        this.dueDate = builder.dueDate;
        this.description = builder.description;
    }

    public static class TaskBuilder {

        protected final String name;
        protected String date;
        protected String dueDate;
        protected String description;

        protected TaskBuilder(String name) {
            Validator.stringArgument(name, NAME_PREFIX);

            this.name = name;
        }

        public TaskBuilder setDate(String date) {
            Validator.objectArgument(date, DATE_PREFIX);

            this.date = date;
            return this;
        }

        public TaskBuilder setDueDate(String dueDate) {
            Validator.objectArgument(dueDate, DUE_DATE_PREFIX);

            this.dueDate = dueDate;
            return this;
        }

        public TaskBuilder setDescription(String description) {
            Validator.stringArgument(description, DESCRIPTION_PREFIX);

            this.description = description;
            return this;
        }

        public Task build() {
            return new Task(this);
        }

    }
}
