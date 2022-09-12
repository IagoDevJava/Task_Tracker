package org.example.tasks_types;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {

    private String nameTask;
    private Status status;
    private String description;
    private long numberId;
    private LocalDateTime startTime;
    private Duration duration;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm");

    public Task(String nameTask, String description, String startDateTime, int hoursDuration, int minutesDuration) {
        this.nameTask = nameTask;
        this.description = description;
        startTime = getStartTimeFromString(startDateTime);
        duration = getDurationFromString(hoursDuration, minutesDuration);
        getEndTime();
    }

    public Task(String nameTask, String description) {
        this.nameTask = nameTask;
        this.description = description;
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

    public void setNumberId(long numberId) {
        this.numberId = numberId;
    }

    public TypesTasks getType() {
        return TypesTasks.TASK;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return numberId == task.numberId
                && Objects.equals(nameTask, task.nameTask)
                && status == task.status
                && Objects.equals(description, task.description)
                && Objects.equals(startTime, task.startTime)
                && Objects.equals(duration, task.duration)
                && Objects.equals(FORMATTER, task.FORMATTER);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameTask, status, description, numberId, startTime, duration, FORMATTER);
    }

    @Override
    public String toString() {
        return numberId + "," +
                getType() + "," +
                nameTask + "," +
                status + "," +
                description + "," +
                startTime + "," +
                duration + "," +
                getEndTime();
    }
}