package main.java.tasks;

import java.util.ArrayList;

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
        return getTaskId() == epic.getTaskId();
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (getTaskName() != null) {
            hash = hash + getTaskName().hashCode() + getTaskId() * 3;
        }
        hash = hash * 31;
        return hash;
    }
}
