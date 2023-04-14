package main.java.tasks;

import main.java.models.Status;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(String taskName, String descriptionEpic) {
        super(taskName, descriptionEpic);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Epic epic = (Epic) obj;
        return getId() == epic.getId() && Objects.equals(getName(), epic.getName())
                && Objects.equals(getDescription(), epic.getDescription())
                && Objects.equals(getStatus(), epic.getStatus())
                && Objects.equals(getSubtasks(), epic.getSubtasks());
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (getName() != null) {
            hash = hash + getName().hashCode() + getId() * 3 + getDescription().hashCode()
                    + getStatus().hashCode() + getSubtasks().hashCode();
        }
        hash = hash * 31;
        return hash;
    }

    @Override
    public String toString () {
        return "Epic{taskName = '" + getName() + "'; " + "description = '" + getDescription() + "'; "
                + "taskStatus = '" + getStatus() + "'; " + "taskId = '" + getId() + "'; "
                + "subtasks = " + subtasks + "'.}";

    }
}
