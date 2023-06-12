package main.java.managers;

import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {
    List<Task> getTasks();

    void deleteTasks();

    Task getTask(int id);

    void addTask(Task task);

    void updateTask(Task task);

    void deleteTask(int id);

    List<Epic> getEpics();

    void deleteEpics();

    Epic getEpic(int id);

    void addEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpic(int theEpic);

    List<Subtask> getEpicSubtasks(int epicId);

    List<Subtask> getSubtasks();

    void deleteSubtasks();

    Task getSubtask(int id);

    void addSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void deleteSubtask(int subtaskId);

    List<Task> getHistory();
    
    Set<Task> getPrioritizedTasks();
}
