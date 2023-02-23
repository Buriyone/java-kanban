package main.java.tasks;

public class Task {
    private String taskName;
    private String descriptionTask;
    private String taskStatus;
    private int taskId;

    public Task(String taskName, String descriptionTask, String status) {
        this.taskName = taskName;
        this.descriptionTask = descriptionTask;
        taskStatus = status;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescriptionTask() {
        return descriptionTask;
    }

    public void setDescriptionTask(String descriptionTask) {
        this.descriptionTask = descriptionTask;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return taskId == task.taskId;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (taskName != null) {
            hash = hash + taskName.hashCode() + taskId * 3;
        }
        hash = hash * 31;
        return hash;
    }
}
