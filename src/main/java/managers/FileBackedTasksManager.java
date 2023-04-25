package main.java.managers;

import main.java.exception.ManagerSaveException;
import main.java.models.CSVTaskFormat;
import main.java.models.Type;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;

public class FileBackedTasksManager extends InMemoryTaskManager {
	private final Path path;
	
	public FileBackedTasksManager(Path path) {
		this.path = path;
	}
	
	@Override
	public ArrayList<Task> getTasks() {
		return super.getTasks();
	}
	
	@Override
	public void deleteTasks() {
		super.deleteTasks();
		save();
	}
	
	@Override
	public Task getTask(int id) {
		try {
			return super.getTask(id);
		} finally {
			save();
		}
	}
	
	@Override
	public void addTask(Task task) {
		super.addTask(task);
		save();
	}
	
	@Override
	public void updateTask(Task task, int taskId) {
		super.updateTask(task, taskId);
		save();
	}
	
	@Override
	public void deleteTask(int id) {
		super.deleteTask(id);
		save();
	}
	
	@Override
	public ArrayList<Epic> getEpics() {
		return super.getEpics();
	}
	
	@Override
	public void deleteEpics() {
		super.deleteEpics();
		save();
	}
	
	@Override
	public Epic getEpic(int id) {
		try {
			return super.getEpic(id);
		} finally {
			save();
		}
	}
	
	@Override
	public void addEpic(Epic epic) {
		super.addEpic(epic);
		save();
	}
	
	@Override
	public void updateEpic(Epic epic, int epicId) {
		super.updateEpic(epic, epicId);
		save();
	}
	
	@Override
	public void deleteEpic(int theEpic) {
		super.deleteEpic(theEpic);
		save();
	}
	
	@Override
	public ArrayList<Subtask> getEpicSubtasks(int epicId) {
		return super.getEpicSubtasks(epicId);
	}
	
	@Override
	public ArrayList<Subtask> getSubtasks() {
		return super.getSubtasks();
	}
	
	@Override
	public void deleteSubtasks() {
		super.deleteSubtasks();
		save();
	}
	
	@Override
	public Task getSubtask(int id) {
		try {
			return super.getSubtask(id);
		} finally {
			save();
		}
	}
	
	@Override
	public void addSubtask(Subtask subtask) {
		super.addSubtask(subtask);
		save();
	}
	
	@Override
	public void updateSubtask(Subtask subtask, int subtaskId) {
		super.updateSubtask(subtask, subtaskId);
		save();
	}
	
	@Override
	public void deleteSubtask(int subtaskId) {
		super.deleteSubtask(subtaskId);
		save();
	}
	
	@Override
	public ArrayList<Task> getHistory() {
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
		FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file.toPath());
		try (Reader rdr = new FileReader(file, StandardCharsets.UTF_8)) {
			int id = 1;
			boolean switchT = true;
			BufferedReader br = new BufferedReader(rdr);
			
			while (br.ready()) {
				String line = br.readLine();
				
				if (CSVTaskFormat.fromString(line) != null && switchT == true) {
					Type type = CSVTaskFormat.fromString(line).getType();
					
					if (type.equals(Type.EPIC)) {
						fileBackedTasksManager.epics.put(CSVTaskFormat
								.fromString(line).getId(), (Epic) CSVTaskFormat.fromString(line));
						
						id = comp(CSVTaskFormat.fromString(line).getId(), id);
					} else if (type.equals(Type.SUBTASK)) {
						fileBackedTasksManager.subtasks
								.put(CSVTaskFormat.fromString(line).getId(), (Subtask) CSVTaskFormat.fromString(line));
						fileBackedTasksManager.epics.get(((Subtask) CSVTaskFormat.fromString(line)).getEpicId())
								.getSubtasks().add((Subtask) CSVTaskFormat.fromString(line));
						
						id = comp(CSVTaskFormat.fromString(line).getId(), id);
					} else if (type.equals(Type.TASK)) {
						fileBackedTasksManager.tasks.
								put(CSVTaskFormat.fromString(line).getId(), CSVTaskFormat.fromString(line));
						
						id = comp(CSVTaskFormat.fromString(line).getId(), id);
					}
				} else if (line.length() == 0) {
					switchT = false;
					break;
				}
			}
			
			while (br.ready()) {
				String line = br.readLine();
				if (switchT == false) {
					for (Integer i : CSVTaskFormat.historyFromString(line)) {
						if (fileBackedTasksManager.tasks.containsKey(i)) {
							fileBackedTasksManager.getTask(i);
						} else if (fileBackedTasksManager.epics.containsKey(i)) {
							fileBackedTasksManager.getEpic(i);
						} else if (fileBackedTasksManager.subtasks.containsKey(i)) {
							fileBackedTasksManager.getSubtask(i);
						}
					}
				}
			}
			
			for (Integer epic : fileBackedTasksManager.epics.keySet()) {
				fileBackedTasksManager.updateStatus(fileBackedTasksManager.epics.get(epic));
			}
			fileBackedTasksManager.id = id + 1;
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return fileBackedTasksManager;
	}
	
	private static int comp(int id1, int id2) {
		return Integer.max(id1, id2);
	}
}
