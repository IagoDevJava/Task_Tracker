package tasks_types;

import java.util.Objects;

public class Task {

    private String nameTask;
    private Status status;
    private String description;

    public Task(String nameTask, String description) {
        this.nameTask = nameTask;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getNameTask() {
        return nameTask;
    }

    public Status getStatus() {
        return status;
    }

    public Status getStatusNEW() {
        return Status.NEW;
    }

    public Status getStatusIN_PROGRESS() {
        return Status.IN_PROGRESS;
    }

    public Status getStatusDONE() {
        return Status.DONE;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(nameTask, task.nameTask)
                && Objects.equals(status, task.status)
                && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameTask, status, description);
    }

    @Override
    public String toString() {
        return "Task{"
                + "Name task: " + getNameTask()
                + ", Description: " + getDescription()
                + ", Status: " + getStatus()
                + "}";
    }
}