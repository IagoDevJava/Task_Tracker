package tasks_types;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<Long> idsOfSubtasksEpic = new ArrayList<>();

    public Epic(String nameTask, String description) {
        super(nameTask, description);
    }

    public ArrayList<Long> getIdsOfSubtasksEpic() {
        return idsOfSubtasksEpic;
    }

    public void setIdsOfSubtasksEpic(ArrayList<Long> idsOfSubtasksEpic) {
        this.idsOfSubtasksEpic = idsOfSubtasksEpic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(idsOfSubtasksEpic, epic.idsOfSubtasksEpic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idsOfSubtasksEpic);
    }

    public TypesTasks getType() {
        return TypesTasks.EPIC;
    }

    @Override
    public String toString() {
        return super.getNumberId() + "," +
                getType() + "," +
                super.getNameTask() + "," +
                super.getStatus() + "," +
                super.getDescription();
    }
}