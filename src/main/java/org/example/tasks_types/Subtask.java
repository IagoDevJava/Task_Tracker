package org.example.tasks_types;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {

    private long myEpicID;

    public Subtask(String nameTask, String description, String startDateTime,
                   int hoursDuration, int minutesDuration, long myEpicID) {
        super(nameTask, description, startDateTime, hoursDuration, minutesDuration);
        this.myEpicID = myEpicID;
    }

    public Subtask(String nameTask, String description, long myEpicID) {
        super(nameTask, description);
        this.myEpicID = myEpicID;
    }

    /**
     * получить айди сабтаски
     */
    public long getMyEpicID() {
        return myEpicID;
    }

    /**
     * установить айди сабтаски
     */
    public void setMyEpicID(long myEpicID) {
        this.myEpicID = myEpicID;
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
        return myEpicID == subtask.myEpicID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), myEpicID);
    }

    public TypesTasks getType() {
        return TypesTasks.SUBTASK;
    }

    @Override
    public String toString() {
        return super.getNumberId() + "," +
                getType() + "," +
                super.getNameTask() + "," +
                super.getStatus() + "," +
                super.getDescription() + "," +
                myEpicID + "," +
                getStartTime() + "," +
                getDuration() + "," +
                getEndTime();
    }
}