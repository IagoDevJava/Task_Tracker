package program_entities;

import java.util.HashMap;
import java.util.Objects;

public class Subtask extends Task {

    private Epic epic;
    private long myEpicID;

    public Subtask(String nameTask, String description) {
        super(nameTask, description);
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    public long getMyEpicID(HashMap<Long, Epic> epicHashMap, Epic epic) {
        for (Long aLong : epicHashMap.keySet()) {
            if (epicHashMap.get(aLong).equals(epic)) {
                myEpicID = aLong;
            }
        }
        return myEpicID;
    }

    public void setMyEpicID(long myEpicID) {
        this.myEpicID = myEpicID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(epic, subtask.epic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epic);
    }

    @Override
    public String toString() {
        return "program_entities.Subtask{"
                + "Name task: " + getNameTask()
                + ", Description: " + getDescription()
                + ", Status: " + getStatus()
                + "}";
    }
}
