package tasks_types;

import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {

    public HashMap<Long, Subtask> allSubtasksOfEpic;

    public Epic(String nameTask, String description) {
        super(nameTask, description);
        allSubtasksOfEpic = new HashMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(allSubtasksOfEpic, epic.allSubtasksOfEpic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), allSubtasksOfEpic);
    }

    @Override
    public String toString() {
        return "Epic{"
                + "Name epic: " + getNameTask()
                + ", Description: " + getDescription()
                + ", Status: " + getStatus()
                + "}";
    }
}
