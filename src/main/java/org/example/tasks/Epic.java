package org.example.tasks;

import org.example.manager.interfaces_and_utilities.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private final List<Long> idsOfSubtasksEpic = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description, TaskManager manager) {
        super(name, description);
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public List<Long> getIdsOfSubtasksEpic() {
        return idsOfSubtasksEpic;
    }

    public TypesTasks getType() {
        return TypesTasks.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public Duration getDuration() {
        return super.getDuration();
    }

    @Override
    public void setDuration(Duration duration) {
        super.setDuration(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(idsOfSubtasksEpic, epic.idsOfSubtasksEpic) && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idsOfSubtasksEpic, endTime);
    }

    @Override
    public String toString() {
        return super.getId() + "," +
                getType() + "," +
                super.getNameTask() + "," +
                super.getStatus() + "," +
                super.getDescription() + "," +
                getStartTime() + "," +
                getDuration() + "," +
                getEndTime();
    }
}