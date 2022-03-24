import java.util.Objects;

public class Task {

    private String nameTask;
    private String status;
    private final String STATUS_NEW = "NEW";
    private final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    private final String STATUS_DONE = "DONE";
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

    public String getStatus() {
        return status;
    }

    public String getStatusNEW() {
        return STATUS_NEW;
    }

    public String getStatusIN_PROGRESS() {
        return STATUS_IN_PROGRESS;
    }

    public String getStatusDONE() {
        return STATUS_DONE;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public void setStatus(String status) {
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
                && Objects.equals(STATUS_NEW, task.STATUS_NEW)
                && Objects.equals(STATUS_IN_PROGRESS, task.STATUS_IN_PROGRESS)
                && Objects.equals(STATUS_DONE, task.STATUS_DONE)
                && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameTask, status, STATUS_NEW, STATUS_IN_PROGRESS, STATUS_DONE, description);
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