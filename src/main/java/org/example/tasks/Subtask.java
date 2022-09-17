package org.example.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {

    private long epicId;

    public Subtask(String name, String description, String startDateTime,
                   int hoursDuration, int minutesDuration, long epicId) {
        super(name, description, startDateTime, hoursDuration, minutesDuration);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, long epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    /**
     * получить айди сабтаски
     */
    @Override
    public long getEpicId() {
        return epicId;
    }

    /**
     * установить айди сабтаски
     */
    public void setEpicId(long epicId) {
        this.epicId = epicId;
    }

    /**
     * получить продолжительность сабтаски
     */
    @Override
    public Duration getDuration() {
        return super.getDuration();
    }

    /**
     * установить продолжительность сабтаски
     */
    @Override
    public void setDuration(Duration duration) {
        super.setDuration(duration);
    }

    /**
     * получить время старта сабтаски
     */
    @Override
    public LocalDateTime getStartTime() {
        return super.getStartTime();
    }

    /**
     * установить время старта сабтаски
     */
    @Override
    public void setStartTime(LocalDateTime startTime) {
        super.setStartTime(startTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    public TypesTasks getType() {
        return TypesTasks.SUBTASK;
    }

    @Override
    public String toString() {
        return super.getId() + "," +
                getType() + "," +
                super.getNameTask() + "," +
                super.getStatus() + "," +
                super.getDescription() + "," +
                epicId + "," +
                getStartTime() + "," +
                getDuration() + "," +
                getEndTime();
    }
}