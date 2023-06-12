package main.java.tasks;

import main.java.models.Status;
import main.java.models.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task implements Comparable<Task>{
    private String name;
    private String description;
    private Status status;
    private int id;
    private int duration;
    private LocalDateTime startTime;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.duration = 15;
    }
    public Task(String name, String description, LocalDateTime time) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.startTime = time;
        this.duration = 15;
    }
    
    @Override
    public int compareTo(Task task) {
        if (this.getStartTime() == null) {
            return 1;
        } else if(task.getStartTime() == null){
            return -1;
        } else if(this.getStartTime() == null && task.getStartTime() == null){
            return 1;
        } else {
            if (this.startTime.isBefore(task.startTime)) {
                return -1;
            } else {
                return 1;
            }
        }
    }
    
    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plusMinutes(duration);
        } else {
            return null;
        }
    }
    
    public LocalDateTime getStartTime() {
        return this.startTime;
    }
    
    public int getDuration() {
        return this.duration;
    }
    
    protected void setDuration(int duration){
        this.duration = duration;
    }
    
    public void setStartTime(LocalDateTime time) {
        this.startTime = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status Status) {
        this.status = Status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Type getType() {
        return Type.TASK;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return id == task.id && Objects.equals(name, task.name)
                && Objects.equals(description, task.description)
                && Objects.equals(status, task.status)
                && Objects.equals(duration, task.duration)
                && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (name != null) {
            hash = hash + name.hashCode() + id * 3 + status.hashCode() + description.hashCode();
        } else if (startTime != null) {
            hash = hash + startTime.hashCode() + duration * 3;
        }
        hash = hash * 31;
        return hash;
    }

    @Override
    public String toString () {
        return id + "," + getType() + "," + name + "," + status + "," + description + "," + startTime;
    }
}
