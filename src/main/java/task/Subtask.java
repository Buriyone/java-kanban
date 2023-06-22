package main.java.task;

import main.java.model.Type;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private int epicId;
    
    public Subtask(String taskName, String descriptionTask, int epicId) {
        super(taskName, descriptionTask);
        this.epicId = epicId;
    }
    
    public Subtask(String taskName, String descriptionTask, LocalDateTime time, int epicId) {
        super(taskName, descriptionTask, time);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public Type getType() {
        return Type.SUBTASK;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Subtask subtask = (Subtask) obj;
        return getId() == subtask.getId() && epicId == subtask.getEpicId()
                && Objects.equals(getName(), subtask.getName())
                && Objects.equals(getDescription(), subtask.getDescription())
                && Objects.equals(getStatus(), subtask.getStatus())
                && Objects.equals(getDuration(), subtask.getDuration())
                && Objects.equals(getStartTime(), subtask.getStartTime());
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (getName() != null) {
            hash = hash + getName().hashCode() + getId() * 7 + getEpicId() * 7
                    + getDescription().hashCode() + getStatus().hashCode();
        } else if (getStartTime() != null) {
            hash = hash + getStartTime().hashCode() + getDuration() * 3;
        }
        hash = hash * 31;
        return hash;
    }

    @Override
    public String toString () {
        return getId() + "," + getType() + "," + getName() + "," + getStatus() + "," + getDescription()
                + "," + getStartTime() + "," + epicId;
    }
}
