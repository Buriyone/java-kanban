package TaskManager;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> commonTaskStorage = new HashMap<>();
    HashMap<Integer, Epic> epicTaskStorage = new HashMap<>();
    HashMap<Integer, Subtask> subtaskStorage = new HashMap<>();
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

    public int createCommonTask(Task task) {
        task.setTaskId(taskIdDealer++);
        task.setTaskStatus("NEW");

        commonTaskStorage.put(task.getTaskId(), task);

        return task.getTaskId();
    }

    public void updateCommonTask(Task task) {
        if (commonTaskStorage.containsKey(task.getTaskId())) {
            commonTaskStorage.put(task.getTaskId(), task);
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

    public int createEpicTask(Epic epic) {
        epic.setTaskId(taskIdDealer++);
        epic.setTaskStatus("NEW");

        epicTaskStorage.put(epic.getTaskId(), epic);

        return epic.getTaskId();
    }

    private void updateEpicTask(Epic epic) {
        epic.setTaskStatus(epicStatusDealer(epic));
        epicTaskStorage.put(epic.getTaskId(), epic);
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

                for (Subtask subtaskByEpic : epicTaskStorage.get(theEpic).getSubtasksStorage()) {
                    ArrayList<Integer> subIdForDeleted = new ArrayList<>();

                    for (Integer subtaskID : subtaskStorage.keySet()) {
                        if (subtaskStorage.get(subtaskID).equals(subtaskByEpic)
                                && subtaskStorage.get(subtaskID).hashCode()
                                == subtaskByEpic.hashCode()) {
                            subIdForDeleted .add(subtaskID);
                        }
                    }

                    for (Integer subId: subIdForDeleted) {
                        subtaskStorage.remove(subId);
                    }
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
            updateEpicTask(epicTaskStorage.get(theEpic));
        }
        subtaskStorage.clear();
    }

    public Task getSubtask(int id) {
        return subtaskStorage.get(id);
    }

    public int createSubtask(Subtask subtask, int epicId) {
        subtask.setTaskId(taskIdDealer++);
        subtask.setTaskStatus("NEW");

        for (Integer theEpic : epicTaskStorage.keySet()) {
            if (theEpic == epicId) {
                epicTaskStorage.get(epicId).getSubtasksStorage().add(subtask);

                subtaskStorage.put(subtask.getTaskId(), subtask);

                updateEpicTask(epicTaskStorage.get(epicId));
            }
        }
        return subtask.getTaskId();
    }

    public void updateSubtask(Subtask subtask) {
        for (Integer theEpic : epicTaskStorage.keySet()) {
            for(int i = 0; i < epicTaskStorage.get(theEpic).getSubtasksStorage().size(); i++) {
                if (epicTaskStorage.get(theEpic).getSubtasksStorage().get(i).equals(subtask)
                        && epicTaskStorage.get(theEpic).getSubtasksStorage().get(i).hashCode()
                        == subtask.hashCode()) {
                    epicTaskStorage.get(theEpic).getSubtasksStorage().set(i, subtask);

                    subtaskStorage.put(subtask.getTaskId(), subtask);

                    updateEpicTask(epicTaskStorage.get(theEpic));
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

                    updateEpicTask(epicTaskStorage.get(theEpic));
                }
            }
        }
    }
}
