package main.java.managers;

import main.java.exception.ManagerSaveException;
import main.java.models.CSVTaskFormat;
import main.java.models.Status;
import main.java.models.Type;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class FileBackedTasksManager extends InMemoryTaskManager {
	private final Path path;
	
	public FileBackedTasksManager(Path path) {
		this.path = path;
	}
	
	@Override
	public List<Task> getTasks() {
		return super.getTasks();
	}
	
	@Override
	public void deleteTasks() {
		super.deleteTasks();
		save();
	}
	
	@Override
	public Task getTask(int id) {
		final Task task = super.getTask(id);
		save();
		return task;
	}
	
	@Override
	public void addTask(Task task) {
		super.addTask(task);
		save();
	}
	
	@Override
	public void updateTask(Task task) {
		super.updateTask(task);
		save();
	}
	
	@Override
	public void deleteTask(int id) {
		super.deleteTask(id);
		save();
	}
	
	@Override
	public List<Epic> getEpics() {
		return super.getEpics();
	}
	
	@Override
	public void deleteEpics() {
		super.deleteEpics();
		save();
	}
	
	@Override
	public Epic getEpic(int id) {
		Epic epic = super.getEpic(id);
		save();
		return epic;
	}
	
	@Override
	protected void updateStatus(Epic epic) {
		super.updateStatus(epic);
	}
	
	@Override
	public void addEpic(Epic epic) {
		super.addEpic(epic);
		save();
	}
	
	@Override
	public void updateEpic(Epic epic) {
		super.updateEpic(epic);
		save();
	}
	
	@Override
	public void deleteEpic(int theEpic) {
		super.deleteEpic(theEpic);
		save();
	}
	
	@Override
	public List<Subtask> getEpicSubtasks(int epicId) {
		return super.getEpicSubtasks(epicId);
	}
	
	@Override
	public List<Subtask> getSubtasks() {
		return super.getSubtasks();
	}
	
	@Override
	public void deleteSubtasks() {
		super.deleteSubtasks();
		save();
	}
	
	@Override
	public Subtask getSubtask(int id) {
		Subtask subtask = super.getSubtask(id);
		save();
		return subtask;
	}
	
	@Override
	public void addSubtask(Subtask subtask) {
		super.addSubtask(subtask);
		save();
	}
	
	@Override
	public void updateSubtask(Subtask subtask) {
		super.updateSubtask(subtask);
		save();
	}
	
	@Override
	public void deleteSubtask(int subtaskId) {
		super.deleteSubtask(subtaskId);
		save();
	}
	
	@Override
	public List<Task> getHistory() {
		return super.getHistory();
	}
	
	private void save() {
		try (Writer wrt = new FileWriter(path.toFile(), StandardCharsets.UTF_8)) {
			try {
				wrt.write(CSVTaskFormat.getFirstLine() + "\n");
				for (Task task : getTasks()) {
					wrt.write(task + "\n");
				}
				for (Epic epic : getEpics()) {
					wrt.write(epic + "\n");
				}
				for (Subtask subtask : getSubtasks()) {
					wrt.write(subtask + "\n");
				}
				wrt.write("\n");
				wrt.write(CSVTaskFormat.historyToString(historyManager));
			} catch (ManagerSaveException e) {
				System.out.println(e.getMessage());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static FileBackedTasksManager loadFromFile(File file) {
		FileBackedTasksManager manager = new FileBackedTasksManager(file.toPath());
		
		try (Reader rdr = new FileReader(file, StandardCharsets.UTF_8)) {
			int id = manager.id;
			BufferedReader br = new BufferedReader(rdr);
			
			while (br.ready()) {
				String line = br.readLine();
				Task task = CSVTaskFormat.fromString(line);
				
				if (task != null) {
					Type type = task.getType();
					Status status = task.getStatus();
					
					if (type.equals(Type.EPIC)) {
						manager.addEpic((Epic) task);
					} else if (type.equals(Type.SUBTASK)) {
						manager.addSubtask((Subtask) task);
						
						if (!status.equals(Status.NEW)) {
							manager.updateSubtask((Subtask) task);
						}
					} else if (type.equals(Type.TASK)) {
						manager.addTask(task);
						
						if (!status.equals(Status.NEW)) {
							manager.updateTask(task);
						}
					}
					id = getMaxId(task.getId(), id) + 1;
				} else if (line.length() == 0) {
					break;
				}
			}
			
			while (br.ready()) {
				String line = br.readLine();
				
				for (Integer i : CSVTaskFormat.historyFromString(line)) {
						if (manager.tasks.containsKey(i)) {
							manager.getTask(i);
						} else if (manager.epics.containsKey(i)) {
							manager.getEpic(i);
						} else if (manager.subtasks.containsKey(i)) {
							manager.getSubtask(i);
						}
					}
			}
			
			for (Integer epic : manager.epics.keySet()) {
				manager.updateStatus(manager.epics.get(epic));
			}
			manager.id = id;
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return manager;
	}
	
	private static int getMaxId(int id1, int id2) {
		return Integer.max(id1, id2);
	}
	
	@Override
	public Set<Task> getPrioritizedTasks() {
		Set<Task> prioritizedTasks = super.getPrioritizedTasks();
		save();
		return prioritizedTasks;
	}
}