package org.example.tasks;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm");
    private long id;
    private TypesTasks type;
    private String name;
    private String description;
    private Status status;
    private LocalDateTime startTime;
    private Duration duration;


    public Task(long id, String name, String description) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(String name, String description, String startDateTime, int hoursDuration, int minutesDuration) {
        this.name = name;
        this.description = description;
        startTime = getStartTimeFromString(startDateTime);
        duration = getDurationFromString(hoursDuration, minutesDuration);
        getEndTime();
    }

    /**
     * Получаем начало выполнения задачи из строки
     */
    public LocalDateTime getStartTimeFromString(String startDateTime) {
        return LocalDateTime.parse(startDateTime, FORMATTER);
    }

    /**
     * Получаем продолжительность выполнения задачи из строки
     */
    public Duration getDurationFromString(int hours, int minutes) {
        Duration thisDuration = Duration.ZERO;
        if (hours >= 0 && minutes >= 0) {
            if (minutes == 0 && hours != 0) {
                thisDuration = Duration.ofHours(hours);
            } else if (hours == 0 && minutes != 0) {
                thisDuration = Duration.ofMinutes(minutes);
            } else {
                thisDuration = Duration.ofHours(hours).plusMinutes(minutes);
            }
        }
        return thisDuration;
    }

    /**
     * Получаем конец выполнения задачи по продолжительности
     */
    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public String getNameTask() {
        return name;
    }

    public void setNameTask(String name) {
        this.name = name;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TypesTasks getType() {
        return TypesTasks.TASK;
    }

    public void setType(TypesTasks type) {
        this.type = type;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public long getEpicId() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id
                && Objects.equals(name, task.name)
                && status == task.status
                && Objects.equals(description, task.description)
                && Objects.equals(startTime, task.startTime)
                && Objects.equals(duration, task.duration)
                && Objects.equals(FORMATTER, task.FORMATTER);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, status, description, id, startTime, duration, FORMATTER);
    }

    @Override
    public String toString() {
        return id + "," +
                getType() + "," +
                name + "," +
                status + "," +
                description + "," +
                startTime + "," +
                duration + "," +
                getEndTime();
    }
}