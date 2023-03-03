package main.java.tasks;

import main.java.managers.Status;

import java.util.Objects;

public class Subtask extends Task {
    private int myEpicId;

    public Subtask(String taskName, String descriptionTask, Status status, int myEpicId) {
        super(taskName, descriptionTask, status);
        this.myEpicId = myEpicId;
    }

    public int getMyEpicId() {
        return myEpicId;
    }

    public void setMyEpicId(int myEpicId) {
        this.myEpicId = myEpicId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Subtask subtask = (Subtask) obj;
        return getTaskId() == subtask.getTaskId() && myEpicId == subtask.getMyEpicId()
                && Objects.equals(getTaskName(), subtask.getTaskName())
                && Objects.equals(getDescriptionTask(), subtask.getDescriptionTask())
                && Objects.equals(getTaskStatus(), subtask.getTaskStatus());
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (getTaskName() != null) {
            hash = hash + getTaskName().hashCode() + getTaskId() * 7 + getMyEpicId() * 7
                    + getDescriptionTask().hashCode() + getTaskStatus().hashCode();
        }
        hash = hash * 31;
        return hash;
    }

    @Override
    public String toString () {
        return "Subtask{taskName = '" + getTaskName() + "'; " + "descriptionTask = '" + getDescriptionTask() + "'; "
                + "taskStatus = '" + getTaskStatus() + "'; " + "taskId = '" + getTaskId() + "'; "
                + "myEpicId = '" + myEpicId + "'.}";
    }
}
