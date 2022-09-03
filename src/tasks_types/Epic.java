package tasks_types;

import interfaces_and_utilities.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<Long> idsOfSubtasksEpic = new ArrayList<>();
    private Duration duration;
    private LocalDateTime startTime = LocalDateTime.MIN;
    private LocalDateTime endTime = LocalDateTime.MAX;

    public Epic(String nameTask, String description, TaskManager manager) {
        super(nameTask, description);
        startTime = setStartTime(manager);
        super.setStartTime(startTime);
        endTime = setEndTime(manager);
        super.setEndTime(endTime);
    }

    public Epic(String nameTask, String description) {
        super(nameTask, description);
    }

    public ArrayList<Long> getIdsOfSubtasksEpic() {
        return idsOfSubtasksEpic;
    }

    /**
     * Получаем конец выполнения задачи по продолжительности
     */
    @Override
    public LocalDateTime getEndTimeFromDuration() {
        return startTime.plus(duration);
    }

    //обновить время старта
    public LocalDateTime setStartTime(TaskManager manager) {
        if (!idsOfSubtasksEpic.isEmpty()) {
            for (Long idSubtaskThisEpic : idsOfSubtasksEpic) {
                Subtask subtask = manager.getSubtaskByIdWithoutStory(idSubtaskThisEpic);
                LocalDateTime startSubtask = subtask.getStartTime();
                startTime = startSubtask;
                if (startSubtask.isBefore(startTime)) {
                    startTime = startSubtask;
                }
            }
        }
        return startTime;
    }

    //обновить время окончания
    public LocalDateTime setEndTime(TaskManager manager) {
        if (!idsOfSubtasksEpic.isEmpty()) {
            for (Long idSubtaskThisEpic : idsOfSubtasksEpic) {
                Subtask subtask = manager.getSubtaskByIdWithoutStory(idSubtaskThisEpic);
                LocalDateTime endSubtask = subtask.getEndTime();
                endTime = endSubtask;
                if (endSubtask.isAfter(endTime)) {
                    endTime = endSubtask;
                }
            }
        }

        return endTime;
    }

    //обновить продолжительность
    public void updateTimeForEpic(TaskManager manager) {
        setStartTime(manager);
        setEndTime(manager);
        duration = Duration.between(startTime, endTime);
        super.setDuration(duration);
    }

    public TypesTasks getType() {
        return TypesTasks.EPIC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(idsOfSubtasksEpic, epic.idsOfSubtasksEpic)
                && Objects.equals(startTime, epic.startTime)
                && Objects.equals(duration, epic.duration)
                && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idsOfSubtasksEpic);
    }

    @Override
    public String toString() {
        return super.getNumberId() + "," +
                getType() + "," +
                super.getNameTask() + "," +
                super.getStatus() + "," +
                super.getDescription() + "," +
                startTime + "," +
                duration + "," +
                endTime;
    }
}