package tasks_types;

import java.util.Objects;

public class Task {

    private String nameTask;
    private Status status;
    private String description;
    private long numberId;

    public Task(String nameTask, String description) {
        this.nameTask = nameTask;
        this.description = description;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getNumberId() {
        return numberId;
    }

    public void numberId(long numberId) {
        this.numberId = numberId;
    }

    public TypesTasks getType() {
        return TypesTasks.TASK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return numberId == task.numberId
                && Objects.equals(nameTask, task.nameTask)
                && status == task.status
                && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameTask, status, description, numberId);
    }

    @Override
    public String toString() {
        return numberId + "," +
                getType() + "," +
                nameTask + "," +
                status + "," +
                description;
    }
}