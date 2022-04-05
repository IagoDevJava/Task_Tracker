package program_entities;

import java.util.HashMap;
import java.util.Objects;

public class Subtask extends Task {

    private long myEpicID;

    public Subtask(String nameTask, String description) {
        super(nameTask, description);
    }

    public void setMyEpicID(HashMap<Long, Epic> epicHashMap, Epic epic) {
        for (Long aLong : epicHashMap.keySet()) {
            if (epicHashMap.get(aLong).equals(epic)) {
                myEpicID = aLong;
            }
        }
    }

    public long getMyEpicID() {
        return myEpicID;
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

    @Override
    public String toString() {
        return "Subtask{"
                + "Name task: " + getNameTask()
                + ", Description: " + getDescription()
                + ", Status: " + getStatus()
                + "}";
    }
}
