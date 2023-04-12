package main.java.managers;

import main.java.models.Status;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> commonTaskStorage = new HashMap<>();
    private final HashMap<Integer, Epic> epicTaskStorage = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskStorage = new HashMap<>();
    private int id = 1;

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public ArrayList<Task> getAllCommonTask() {
        ArrayList<Task> allTask = new ArrayList<>();

        for (Integer numberId : commonTaskStorage.keySet()) {
            if (commonTaskStorage.containsKey(numberId)) {
                allTask.add(commonTaskStorage.get(numberId));
            }
        }
        return allTask;
    }

    @Override
    public void deleteAllCommonTask() {
        for (Integer integer : commonTaskStorage.keySet()) {
            historyManager.remove(integer);
        }
        commonTaskStorage.clear();
    }

    @Override
    public Task getCommonTask(int id) {
        historyManager.add(commonTaskStorage.get(id));
        return commonTaskStorage.get(id);
    }

    @Override
    public void addCommonTask(Task task) {
        task.setTaskId(id++);
        task.setTaskStatus(Status.NEW);

        commonTaskStorage.put(task.getTaskId(), task);
    }

    @Override
    public void updateCommonTask(Task task, int taskId) {
        if (commonTaskStorage.containsKey(taskId)) {
            task.setTaskId(taskId);
            commonTaskStorage.put(taskId, task);
        }
    }

    @Override
    public void deleteCommonTask(int id) {
        historyManager.remove(id);
        commonTaskStorage.remove(id);
    }

    @Override
    public ArrayList<Epic> getAllEpicTask() {
        ArrayList<Epic> allEpic = new ArrayList<>();

        for (Integer theEpic : epicTaskStorage.keySet()) {
            if (epicTaskStorage.containsKey(theEpic)) {
                allEpic.add(epicTaskStorage.get(theEpic));
            }
        }
        return allEpic;
    }

    @Override
    public void deleteAllEpicTask() {
        for (Integer integer : epicTaskStorage.keySet()) {
            historyManager.remove(integer);
        }
        for (Integer integer : subtaskStorage.keySet()) {
            historyManager.remove(integer);
        }
        epicTaskStorage.clear();
        subtaskStorage.clear();
    }

    @Override
    public Epic getEpicTask(int id) {
        historyManager.add(epicTaskStorage.get(id));
        return epicTaskStorage.get(id);
    }

    @Override
    public void addEpicTask(Epic epic) {
        epic.setTaskId(id++);
        epic.setTaskStatus(Status.NEW);

        epicTaskStorage.put(epic.getTaskId(), epic);
    }

    @Override
    public void updateEpicTask(Epic epic, int epicId) {
        if (epicTaskStorage.get(epicId).getTaskId() == epicId) {
            ArrayList<Subtask> newList = new ArrayList<>(epicTaskStorage.get(epicId).getSubtasksStorage());
            epic.setTaskId(epicId);
            epicTaskStorage.put(epicId, epic);
            epicTaskStorage.get(epicId).setSubtasksStorage(newList);
        }

        epicTaskStorage.get(epicId).setTaskStatus(epicStatusDealer(epicTaskStorage.get(epicId)));
    }

    private Status epicStatusDealer(Epic epic) {
        Status status;

        if (!epic.getSubtasksStorage().isEmpty()) {
            ArrayList<Status> itemStatus = new ArrayList<>();

            for (Subtask subtask : epic.getSubtasksStorage()) {
                itemStatus.add(subtask.getTaskStatus());
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
        return status;
    }

    @Override
    public void deleteEpicTask(int theEpic) {
        if (epicTaskStorage.containsKey(theEpic)) {
            if (!epicTaskStorage.get(theEpic).getSubtasksStorage().isEmpty()) {
                ArrayList<Integer> subId = new ArrayList<>();

                for (Integer subtaskId : subtaskStorage.keySet()) {
                    if (subtaskStorage.get(subtaskId).getMyEpicId() == theEpic) {
                        subId.add(subtaskStorage.get(subtaskId).getTaskId());
                    }
                }

                for (Integer forDeleted : subId) {
                    subtaskStorage.remove(forDeleted);
                    historyManager.remove(forDeleted);
                }

                epicTaskStorage.remove(theEpic);
            } else {
                epicTaskStorage.remove(theEpic);
            }
            historyManager.remove(theEpic);
        }
    }

    @Override
    public ArrayList<Subtask> getAllEpicSubtask(int epicId) {
        ArrayList<Subtask> epicSubtask = new ArrayList<>();

        if (epicTaskStorage.containsKey(epicId)) {
            epicSubtask.addAll(epicTaskStorage.get(epicId).getSubtasksStorage());
        }
        return epicSubtask;
    }

    @Override
    public ArrayList<Subtask> getAllSubtask() {
        ArrayList<Subtask> allSubtask = new ArrayList<>();

        for (Integer subtaskId : subtaskStorage.keySet()) {
            if (subtaskStorage.containsKey(subtaskId)) {
                allSubtask.add(subtaskStorage.get(subtaskId));
            }
        }
        return allSubtask;
    }

    @Override
    public void deleteAllSubtask() {
        for (Integer theEpic : epicTaskStorage.keySet()) {
            epicTaskStorage.get(theEpic).getSubtasksStorage().clear();

            epicTaskStorage.get(theEpic).setTaskStatus(epicStatusDealer(epicTaskStorage.get(theEpic)));
        }
        for (Integer integer : subtaskStorage.keySet()) {
            historyManager.remove(integer);
        }
        subtaskStorage.clear();
    }

    @Override
    public Task getSubtask(int id) {
        historyManager.add(subtaskStorage.get(id));
        return subtaskStorage.get(id);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setTaskId(id++);
        subtask.setTaskStatus(Status.NEW);
        int epicId = subtask.getMyEpicId();

        for (Integer theEpic : epicTaskStorage.keySet()) {
            if (theEpic == epicId) {
                subtask.setMyEpicId(epicId);

                epicTaskStorage.get(epicId).getSubtasksStorage().add(subtask);

                subtaskStorage.put(subtask.getTaskId(), subtask);

                epicTaskStorage.get(theEpic).setTaskStatus(epicStatusDealer(epicTaskStorage.get(theEpic)));
            }
        }
    }

    @Override
    public void updateSubtask(Subtask subtask, int subtaskId) {
        for (Integer theEpic : epicTaskStorage.keySet()) {
            for (int i = 0; i < epicTaskStorage.get(theEpic).getSubtasksStorage().size(); i++) {
                if (epicTaskStorage.get(theEpic).getSubtasksStorage().get(i).getTaskId() == subtaskId) {
                    subtask.setTaskId(subtaskId);

                    subtask.setMyEpicId(epicTaskStorage.get(theEpic).getTaskId());

                    epicTaskStorage.get(theEpic).getSubtasksStorage().set(i, subtask);

                    subtaskStorage.put(subtaskId, subtask);

                    epicTaskStorage.get(theEpic).setTaskStatus(epicStatusDealer(epicTaskStorage.get(theEpic)));
                }
            }
        }
    }

    @Override
    public void deleteSubtask(int subtaskId) {
        for (Integer theEpic : epicTaskStorage.keySet()) {
            for (int i = 0; i < epicTaskStorage.get(theEpic).getSubtasksStorage().size(); i++) {
                if (epicTaskStorage.get(theEpic).getSubtasksStorage().get(i).equals(subtaskStorage.get(subtaskId))
                        && epicTaskStorage.get(theEpic).getSubtasksStorage().get(i).hashCode()
                        == subtaskStorage.get(subtaskId).hashCode()) {
                    epicTaskStorage.get(theEpic).getSubtasksStorage()
                            .remove(epicTaskStorage.get(theEpic).getSubtasksStorage().get(i));

                    subtaskStorage.remove(subtaskId);

                    epicTaskStorage.get(theEpic).setTaskStatus(epicStatusDealer(epicTaskStorage.get(theEpic)));
                }
            }
        }
        historyManager.remove(subtaskId);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }
}
