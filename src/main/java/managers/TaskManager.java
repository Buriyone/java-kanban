package main.java.managers;

import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    public HashMap<Integer, Task> commonTaskStorage = new HashMap<>();
    public HashMap<Integer, Epic> epicTaskStorage = new HashMap<>();
    public HashMap<Integer, Subtask> subtaskStorage = new HashMap<>();
    private int taskIdDealer = 1;

    public ArrayList<Task> viewAllCommonTask() {
        ArrayList<Task> allTask = new ArrayList<>();

        for (Integer numberId : commonTaskStorage.keySet()) {
            if (commonTaskStorage.containsKey(numberId)) {
                allTask.add(commonTaskStorage.get(numberId));
            }
        }
        return allTask;
    }

    public void deleteAllCommonTask() {
        commonTaskStorage.clear();
    }

    public Task getCommonTask(int id) {
        return commonTaskStorage.get(id);
    }

    public void createCommonTask(Task task) {
        task.setTaskId(taskIdDealer++);
        task.setTaskStatus("NEW");

        commonTaskStorage.put(task.getTaskId(), task);
    }

    public void updateCommonTask(Task task, int taskId) {
        if (commonTaskStorage.containsKey(taskId)) {
            task.setTaskId(taskId);
            commonTaskStorage.put(taskId, task);
        }
    }

    public void deleteCommonTask(int id) {
        commonTaskStorage.remove(id);
    }

    public ArrayList<Epic> viewAllEpicTask() {
        ArrayList<Epic> allEpic = new ArrayList<>();

        for (Integer theEpic : epicTaskStorage.keySet()) {
            if (epicTaskStorage.containsKey(theEpic)) {
                allEpic.add(epicTaskStorage.get(theEpic));
            }
        }
        return allEpic;
    }

    public void deleteAllEpicTask() {
        epicTaskStorage.clear();
        subtaskStorage.clear();
    }

    public Epic getEpicTask(int id) {
        return epicTaskStorage.get(id);
    }

    public void createEpicTask(Epic epic) {
        epic.setTaskId(taskIdDealer++);
        epic.setTaskStatus("NEW");

        epicTaskStorage.put(epic.getTaskId(), epic);
    }

    public void updateEpicTask(Epic epic, int epicId) {
        if (epicTaskStorage.get(epicId).getTaskId() == epicId) {
            ArrayList<Subtask> newList = new ArrayList<>(epicTaskStorage.get(epicId).getSubtasksStorage());
            epic.setTaskId(epicId);
            epicTaskStorage.put(epicId, epic);
            epicTaskStorage.get(epicId).setSubtasksStorage(newList);
        }

        epicTaskStorage.get(epicId).setTaskStatus(epicStatusDealer(epicTaskStorage.get(epicId)));
    }

    private String epicStatusDealer(Epic epic) {
        String status;

        if (!epic.getSubtasksStorage().isEmpty()) {
            ArrayList<String> itemStatus = new ArrayList<>();

            for (Subtask subtask : epic.getSubtasksStorage()) {
                itemStatus.add(subtask.getTaskStatus());
            }

            if(!itemStatus.contains("IN_PROGRESS") && !itemStatus.contains("DONE")) {
                status = "NEW";
            } else if (!itemStatus.contains("IN_PROGRESS") && !itemStatus.contains("NEW")) {
                status = "DONE";
            } else {
                status = "IN_PROGRESS";
            }
        } else {
            status = "NEW";
        }
        return status;
    }

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
                }

                epicTaskStorage.remove(theEpic);
            } else {
                epicTaskStorage.remove(theEpic);
            }
        }
    }

    public ArrayList<Subtask> viewAllEpicSubtask(int epicId) {
        ArrayList<Subtask> epicSubtask = new ArrayList<>();

        if (epicTaskStorage.containsKey(epicId)) {
            epicSubtask.addAll(epicTaskStorage.get(epicId).getSubtasksStorage());
        }
        return epicSubtask;
    }

    public ArrayList<Subtask> viewAllSubtask() {
        ArrayList<Subtask> allSubtask = new ArrayList<>();

        for (Integer subtaskId : subtaskStorage.keySet()) {
            if (subtaskStorage.containsKey(subtaskId)) {
                allSubtask.add(subtaskStorage.get(subtaskId));
            }
        }
        return allSubtask;
    }

    public void deleteAllSubtask() {
        for (Integer theEpic: epicTaskStorage.keySet()) {
            epicTaskStorage.get(theEpic).getSubtasksStorage().clear();

            epicTaskStorage.get(theEpic).setTaskStatus(epicStatusDealer(epicTaskStorage.get(theEpic)));
        }
        subtaskStorage.clear();
    }

    public Task getSubtask(int id) {
        return subtaskStorage.get(id);
    }

    public void createSubtask(Subtask subtask, int epicId) {
        subtask.setTaskId(taskIdDealer++);
        subtask.setTaskStatus("NEW");

        for (Integer theEpic : epicTaskStorage.keySet()) {
            if (theEpic == epicId) {
                subtask.setMyEpicId(epicId);

                epicTaskStorage.get(epicId).getSubtasksStorage().add(subtask);

                subtaskStorage.put(subtask.getTaskId(), subtask);

                epicTaskStorage.get(theEpic).setTaskStatus(epicStatusDealer(epicTaskStorage.get(theEpic)));
            }
        }
    }

    public void updateSubtask(Subtask subtask, int subtaskId) {
        for (Integer theEpic : epicTaskStorage.keySet()) {
            for(int i = 0; i < epicTaskStorage.get(theEpic).getSubtasksStorage().size(); i++) {
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

    public void deleteSubtask(int subtaskId) {
        for (Integer theEpic : epicTaskStorage.keySet()) {
            for(int i = 0; i < epicTaskStorage.get(theEpic).getSubtasksStorage().size(); i++) {
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
    }
}
