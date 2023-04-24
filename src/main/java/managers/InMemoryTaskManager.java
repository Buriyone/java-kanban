package main.java.managers;

import main.java.models.Status;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected int id = 1;

    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> allTask = new ArrayList<>();

        for (Integer numberId : tasks.keySet()) {
            if (tasks.containsKey(numberId)) {
                allTask.add(tasks.get(numberId));
            }
        }
        return allTask;
    }

    @Override
    public void deleteTasks() {
        for (Integer integer : tasks.keySet()) {
            historyManager.remove(integer);
        }
        tasks.clear();
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public void addTask(Task task) {
        task.setId(id++);
        task.setStatus(Status.NEW);
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateTask(Task task, int taskId) {
        if (tasks.containsKey(taskId)) {
            task.setId(taskId);
            tasks.put(taskId, task);
        }
    }

    @Override
    public void deleteTask(int id) {
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> allEpic = new ArrayList<>();

        for (Integer theEpic : epics.keySet()) {
            if (epics.containsKey(theEpic)) {
                allEpic.add(epics.get(theEpic));
            }
        }
        return allEpic;
    }

    @Override
    public void deleteEpics() {
        for (Integer integer : subtasks.keySet()) {
            historyManager.remove(integer);
        }
        for (Integer integer : epics.keySet()) {
            historyManager.remove(integer);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(id++);
        epic.setStatus(Status.NEW);

        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateEpic(Epic epic, int epicId) {
        if (epics.get(epicId).getId() == epicId) {
            ArrayList<Subtask> newList = new ArrayList<>(epics.get(epicId).getSubtasks());

            epic.setId(epicId);

            epics.put(epicId, epic);

            epics.get(epicId).setSubtasks(newList);
        }
        updateStatus(epics.get(epicId));
    }

    protected void updateStatus(Epic epic) {
        Status status;

        if (!epic.getSubtasks().isEmpty()) {
            ArrayList<Status> itemStatus = new ArrayList<>();

            for (Subtask subtask : epic.getSubtasks()) {
                itemStatus.add(subtask.getStatus());
            }

            if (!itemStatus.contains(Status.IN_PROGRESS) && !itemStatus.contains(Status.DONE)) {
                status = Status.NEW;
            } else if (!itemStatus.contains(Status.IN_PROGRESS) && !itemStatus.contains(Status.NEW)) {
                status = Status.DONE;
            } else {
                status = Status.IN_PROGRESS;
            }
        } else {
            status = Status.NEW;
        }
        epic.setStatus(status);
    }

    @Override
    public void deleteEpic(int theEpic) {
        ArrayList<Integer> subId = new ArrayList<>();

        for (Integer subtaskId : subtasks.keySet()) {
            if (subtasks.get(subtaskId).getEpicId() == theEpic) {
                subId.add(subtasks.get(subtaskId).getId());
            }
        }

        for (Integer forDeleted : subId) {
            subtasks.remove(forDeleted);
            historyManager.remove(forDeleted);
        }
        epics.remove(theEpic);
        historyManager.remove(theEpic);
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Subtask> epicSubtask = new ArrayList<>();

        if (epics.containsKey(epicId)) {
            epicSubtask.addAll(epics.get(epicId).getSubtasks());
        }
        return epicSubtask;
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> allSubtask = new ArrayList<>();

        for (Integer subtaskId : subtasks.keySet()) {
            allSubtask.add(subtasks.get(subtaskId));
        }
        return allSubtask;
    }

    @Override
    public void deleteSubtasks() {
        for (Integer theEpic : epics.keySet()) {
            epics.get(theEpic).getSubtasks().clear();

            updateStatus(epics.get(theEpic));
        }
        for (Integer integer : subtasks.keySet()) {
            historyManager.remove(integer);
        }
        subtasks.clear();
    }

    @Override
    public Task getSubtask(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setId(id++);
        subtask.setStatus(Status.NEW);
        int epicId = subtask.getEpicId();
        epics.get(epicId).getSubtasks().add(subtask);
        subtasks.put(subtask.getId(), subtask);
        updateStatus(epics.get(epicId));
    }

    @Override
    public void updateSubtask(Subtask subtask, int subtaskId) {
        int epicId = subtask.getEpicId();

        for (int i = 0; i < epics.get(epicId).getSubtasks().size(); i++) {
            if (epics.get(epicId).getSubtasks().get(i).getId() == subtaskId) {
                subtask.setId(subtaskId);

                subtask.setEpicId(epics.get(epicId).getId());

                epics.get(epicId).getSubtasks().set(i, subtask);

                subtasks.put(subtaskId, subtask);

                updateStatus(epics.get(epicId));
            }
        }
    }

    @Override
    public void deleteSubtask(int subtaskId) {
        int epicId = subtasks.get(subtaskId).getEpicId();

        for (int i = 0; i < epics.get(epicId).getSubtasks().size(); i++) {
            if (epics.get(epicId).getSubtasks().get(i).equals(subtasks.get(subtaskId))) {
                epics.get(epicId).getSubtasks().remove(epics.get(epicId).getSubtasks().get(i));

                subtasks.remove(subtaskId);

                updateStatus(epics.get(epicId));
                
                historyManager.remove(subtaskId);
            }
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }
}
