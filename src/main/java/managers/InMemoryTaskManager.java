package main.java.managers;

import main.java.exception.TimeValidationException;
import main.java.models.Status;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Set<Task> prioritizedTasks = new TreeSet<>();
    
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    
    protected int id = 1;
    
    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }
    
    @Override
    public void deleteTasks() {
        for (Integer id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
        updatePrioritizedTasks();
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
        updatePrioritizedTasks();
    }
    
    @Override
    public void updateTask(Task task) {
        final int id = task.getId();
        
        tasks.get(id).setName(task.getName());
        tasks.get(id).setDescription(task.getDescription());
        tasks.get(id).setStatus(task.getStatus());
        tasks.get(id).setStartTime(task.getStartTime());
        updatePrioritizedTasks();
    }
    
    @Override
    public void deleteTask(int id) {
        historyManager.remove(id);
        tasks.remove(id);
        updatePrioritizedTasks();
    }
    
    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }
    
    @Override
    public void deleteEpics() {
        for (Integer id : subtasks.keySet()) {
            historyManager.remove(id);
        }
        
        for (Integer id : epics.keySet()) {
            historyManager.remove(id);
        }
        subtasks.clear();
        epics.clear();
        updatePrioritizedTasks();
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
        updatePrioritizedTasks();
    }
    
    @Override
    public void updateEpic(Epic epic) {
        final int id = epic.getId();
        epics.get(id).setName(epic.getName());
        epics.get(id).setDescription(epic.getDescription());
        updateStatus(epics.get(id));
        updatePrioritizedTasks();
    }
    
    protected void updateStatus(Epic epic) {
        Status status;
        
        if (!epic.getSubtasks().isEmpty()) {
            List<Status> itemStatus = new ArrayList<>();
            
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
        epics.get(epic.getId()).setStatus(status);
    }
    
    @Override
    public void deleteEpic(int id) {
        List<Integer> subId = new ArrayList<>();
        
        for (Subtask subtask : epics.get(id).getSubtasks()) {
            subId.add(subtask.getId());
        }
        
        for (Integer i : subId) {
            subtasks.remove(i);
            historyManager.remove(i);
        }
        epics.remove(id);
        historyManager.remove(id);
        updatePrioritizedTasks();
    }
    
    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        return epics.get(epicId).getSubtasks();
    }
    
    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }
    
    @Override
    public void deleteSubtasks() {
        for (Integer epicId : epics.keySet()) {
            epics.get(epicId).getSubtasks().clear();
            
            updateStatus(epics.get(epicId));
            epics.get(epicId).recalculationEpicTime();
        }
        
        for (Integer integer : subtasks.keySet()) {
            historyManager.remove(integer);
        }
        subtasks.clear();
        updatePrioritizedTasks();
    }
    
    @Override
    public Subtask getSubtask(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }
    
    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setId(id++);
        final int epicId = subtask.getEpicId();
        epics.get(epicId).getSubtasks().add(subtask);
        subtasks.put(subtask.getId(), subtask);
        updateStatus(epics.get(epicId));
        epics.get(epicId).recalculationEpicTime();
        updatePrioritizedTasks();
    }
    
    @Override
    public void updateSubtask(Subtask subtask) {
        final int subId = subtask.getId();
        final int epicId = subtask.getEpicId();
        
        for (Subtask sub : epics.get(epicId).getSubtasks()) {
            if (sub.equals(subtask)) {
                sub.setName(subtask.getName());
                sub.setDescription(subtask.getDescription());
                sub.setStatus(subtask.getStatus());
                sub.setStartTime(subtask.getStartTime());
            }
        }
        subtasks.put(subId, subtask);
        updateStatus(epics.get(epicId));
        epics.get(epicId).recalculationEpicTime();
        updatePrioritizedTasks();
    }
    
    @Override
    public void deleteSubtask(int subId) {
        final int epicId = subtasks.get(subId).getEpicId();
        
        for (int i = 0; i < epics.get(epicId).getSubtasks().size(); i++) {
            if (epics.get(epicId).getSubtasks().get(i).getId() == subId) {
                epics.get(epicId).getSubtasks().remove(epics.get(epicId).getSubtasks().get(i));
                
                subtasks.remove(subId);
                
                updateStatus(epics.get(epicId));
                epics.get(epicId).recalculationEpicTime();
                
                historyManager.remove(subId);
            }
        }
        updatePrioritizedTasks();
    }
    
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
    
    private void updatePrioritizedTasks() {
        prioritizedTasks.clear();
        
        for (Integer id : tasks.keySet()) {
            checkTimeValidation(tasks.get(id));
            prioritizedTasks.add(tasks.get(id));
        }
    
        for (Integer id : subtasks.keySet()) {
            checkTimeValidation(subtasks.get(id));
            prioritizedTasks.add(subtasks.get(id));
        }
    }
    
    private void checkTimeValidation(Task task) {
        if (task.getStartTime() != null) {
            for (Task t : prioritizedTasks) {
                if (t.getStartTime() != null) {
                    try {
                        if (task.getStartTime().isAfter(t.getStartTime())
                                && task.getStartTime().isBefore(t.getEndTime())) {
                            throw new TimeValidationException("Начало выполнения задачи: "
                                    + task.getId() + " пересекается с периодом выполнения задачи: " + t.getId());
                        } else if (task.getEndTime().isAfter(t.getStartTime())
                                && task.getEndTime().isBefore(t.getEndTime())) {
                            throw new TimeValidationException("Окончание выполнения задачи: "
                                    + task.getId() + " пересекается с периодом выполнения задачи: " + t.getId());
                        } else if (task.getStartTime().equals(t.getStartTime())){
                            throw new TimeValidationException("Начало выполнения задачи: "
                                    + task.getId() + " идентично началу выполнения задачи: " + t.getId());
                        }
                    } catch (TimeValidationException e) {
                        return;
                    }
                }
            }
        }
    }
    
    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }
}
