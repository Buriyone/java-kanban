package main.java.managers;

import main.java.data.DataBase;
import main.java.models.Status;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    
    protected int id = 1;
    
    protected final DataBase data = new DataBase();
    
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
        updateData();
    }
    
    @Override
    public Task getTask(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        } else {
            return null;
        }
    }
    
    @Override
    public void addTask(Task task) {
        task.setId(id++);
        tasks.put(task.getId(), task);
        
        if (task.getStartTime() != null) {
            data.addTime(task.getStartTime());
        }
        updateData();
    }
    
    @Override
    public void updateTask(Task task, int taskId) {
        if (tasks.containsKey(taskId)) {
            if (task.getStartTime() != null) {
                data.addTime(task.getStartTime());
            } else if (tasks.get(taskId).getStartTime() != null && task.getStartTime() == null) {
                task.setStartTime(tasks.get(taskId).getStartTime());
            }
            
            task.setId(taskId);
            tasks.put(taskId, task);
        }
        updateData();
    }
    
    @Override
    public void deleteTask(int id) {
        historyManager.remove(id);
        
        tasks.remove(id);
        updateData();
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
        for (Integer id : subtasks.keySet()) {
            historyManager.remove(id);
        }
        
        for (Integer id : epics.keySet()) {
            historyManager.remove(id);
        }
        
        epics.clear();
        subtasks.clear();
        updateData();
    }
    
    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }
    
    @Override
    public void addEpic(Epic epic) {
        epic.setId(id++);
        
        epics.put(epic.getId(), epic);
        updateData();
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
        updateData();
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
        updateData();
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
        updateData();
    }
    
    @Override
    public Subtask getSubtask(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }
    
    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setId(id++);
        int epicId = subtask.getEpicId();
        epics.get(epicId).getSubtasks().add(subtask);
        subtasks.put(subtask.getId(), subtask);
        updateStatus(epics.get(epicId));
    
        if (subtask.getStartTime() != null) {
            data.addTime(subtask.getStartTime());
        }
        updateData();
    }
    
    @Override
    public void updateSubtask(Subtask subtask, int subtaskId) {
        int epicId = subtask.getEpicId();
        
        for (int i = 0; i < epics.get(epicId).getSubtasks().size(); i++) {
            if (epics.get(epicId).getSubtasks().get(i).getId() == subtaskId) {
                if (subtask.getStartTime() != null) {
                    data.addTime(subtask.getStartTime());
                } else if (subtasks.get(subtaskId).getStartTime() != null && subtask.getStartTime() == null) {
                    subtask.setStartTime(subtasks.get(subtaskId).getStartTime());
                }
                
                subtask.setId(subtaskId);
                
                subtask.setEpicId(epics.get(epicId).getId());
                
                epics.get(epicId).getSubtasks().set(i, subtask);
                
                subtasks.put(subtaskId, subtask);
                
                updateStatus(epics.get(epicId));
            }
        }
        updateData();
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
        updateData();
    }
    
    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }
    
    protected void updateData() {
        data.updatePrioritizedTasks(tasks, subtasks);
    }
    
    @Override
    public LocalDateTime getFreeTime(){
        return data.getFreeTime();
    }
    
    @Override
    public Set<Task> getPrioritizedTasks() {
        return data.getPrioritizedTasks();
    }
}
