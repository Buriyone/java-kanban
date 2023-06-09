package main.java.managers;

import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

public interface TaskManager {
    ArrayList<Task> getTasks();

    void deleteTasks();

    Task getTask(int id);

    void addTask(Task task);

    void updateTask(Task task, int taskId);

    void deleteTask(int id);

    ArrayList<Epic> getEpics();

    void deleteEpics();

    Epic getEpic(int id);

    void addEpic(Epic epic);

    void updateEpic(Epic epic, int epicId);

    void deleteEpic(int theEpic);

    ArrayList<Subtask> getEpicSubtasks(int epicId);

    ArrayList<Subtask> getSubtasks();

    void deleteSubtasks();

    Task getSubtask(int id);

    void addSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask, int subtaskId);

    void deleteSubtask(int subtaskId);

    ArrayList<Task> getHistory();
    
    LocalDateTime getFreeTime();
    
    Set<Task> getPrioritizedTasks();
}
