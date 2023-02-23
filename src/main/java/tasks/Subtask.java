package main.java.tasks;

public class Subtask extends Task {
    private int myEpicId;

    public Subtask(String taskName, String descriptionTask, String status) {
        super(taskName, descriptionTask, status);
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
        return getTaskId() == subtask.getTaskId() && myEpicId == subtask.getMyEpicId();
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
