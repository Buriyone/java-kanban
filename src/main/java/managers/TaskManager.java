package main.java.managers;

import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;

import java.util.ArrayList;

public interface TaskManager {
    ArrayList<Task> getAllCommonTask();

    void deleteAllCommonTask();

    Task getCommonTask(int id);

    void addCommonTask(Task task);

    void updateCommonTask(Task task, int taskId);

    void deleteCommonTask(int id);

    ArrayList<Epic> getAllEpicTask();

    void deleteAllEpicTask();

    Epic getEpicTask(int id);

    void addEpicTask(Epic epic);

    void updateEpicTask(Epic epic, int epicId);

    void deleteEpicTask(int theEpic);

    ArrayList<Subtask> getAllEpicSubtask(int epicId);

    ArrayList<Subtask> getAllSubtask();

    void deleteAllSubtask();

    Task getSubtask(int id);

    void addSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask, int subtaskId);

    void deleteSubtask(int subtaskId);

    ArrayList<Task> getHistory();
}
