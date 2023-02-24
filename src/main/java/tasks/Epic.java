package main.java.tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Subtask> subtasksStorage = new ArrayList<>();

    public Epic(String taskName, String descriptionEpic, String status) {
        super(taskName, descriptionEpic, status);
    }

    public ArrayList<Subtask> getSubtasksStorage() {
        return subtasksStorage;
    }

    public void setSubtasksStorage(ArrayList<Subtask> subtasksStorage) {
        this.subtasksStorage = subtasksStorage;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Epic epic = (Epic) obj;
        return getTaskId() == epic.getTaskId() && Objects.equals(getTaskName(), epic.getTaskName())
                && Objects.equals(getDescriptionTask(), epic.getDescriptionTask())
                && Objects.equals(getTaskStatus(), epic.getTaskStatus())
                && Objects.equals(getSubtasksStorage(), epic.getSubtasksStorage());
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (getTaskName() != null) {
            hash = hash + getTaskName().hashCode() + getTaskId() * 3 + getDescriptionTask().hashCode()
                    + getTaskStatus().hashCode() + getSubtasksStorage().hashCode();
        }
        hash = hash * 31;
        return hash;
    }

    @Override
    public String toString () {
        return "Epic{taskName = '" + getTaskName() + "'; " + "descriptionTask = '" + getDescriptionTask() + "'; "
                + "taskStatus = '" + getTaskStatus() + "'; " + "taskId = '" + getTaskId() + "'; "
                + "subtasksStorage = " + subtasksStorage + "'.}";

    }
}
