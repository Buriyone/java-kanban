package main.java.manager;

import com.google.gson.*;
import main.java.server.KVTaskClient;
import main.java.task.Epic;
import main.java.task.Subtask;
import main.java.task.Task;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class HttpTaskManager extends FileBackedTasksManager {
	private final KVTaskClient client;
	
	private static final Gson gson = new GsonBuilder().serializeNulls().create();
	public HttpTaskManager(String url) {
		super(Path.of("src/main/resources/file.csv"));
		this.client = new KVTaskClient(url);
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
		String tasks = gson.toJson(getTasks());
		String epics = gson.toJson(getEpics());
		String subtasks = gson.toJson(getSubtasks());
		String history = gson.toJson(getHistory());
		
		client.put("1", gson.toJson(new String[]{tasks, epics, subtasks, history}));
	}
	public static HttpTaskManager loadFromServer(String url) {
		HttpTaskManager manager = new HttpTaskManager(url);
		try {
			int managerId = 1;
			
			JsonElement jsonElement = JsonParser.parseString(manager.client.load("1"));
			if (jsonElement.isJsonArray()) {
				JsonArray jsonArray = jsonElement.getAsJsonArray();
				
				JsonElement jsonTasks = JsonParser.parseString(jsonArray.get(0).getAsString());
				for (JsonElement t : jsonTasks.getAsJsonArray()) {
					Task task = gson.fromJson(t, Task.class);
					manager.id = task.getId();
					manager.addTask(task);
					managerId = Integer.max(managerId, task.getId());
				}
				
				JsonElement jsonEpics = JsonParser.parseString(jsonArray.get(1).getAsString());
				for (JsonElement e : jsonEpics.getAsJsonArray()) {
					Epic epic = gson.fromJson(e, Epic.class);
					manager.id = epic.getId();
					epic.getSubtasks().clear();
					manager.addEpic(epic);
					managerId = Integer.max(managerId, epic.getId());
				}
				
				JsonElement jsonSubtasks = JsonParser.parseString(jsonArray.get(2).getAsString());
				for (JsonElement s : jsonSubtasks.getAsJsonArray()) {
					Subtask subtask = gson.fromJson(s, Subtask.class);
					manager.id = subtask.getId();
					manager.addSubtask(subtask);
					managerId = Integer.max(managerId, subtask.getId());
				}
				
				JsonElement jsonHistory = JsonParser.parseString(jsonArray.get(3).getAsString());
				for (JsonElement t : jsonHistory.getAsJsonArray()) {
					JsonObject content = t.getAsJsonObject();
					int id = content.get("id").getAsInt();
					
					if (manager.tasks.containsKey(id)) {
						manager.getTask(id);
					} else if (manager.epics.containsKey(id)) {
						manager.getEpic(id);
					} else if (manager.subtasks.containsKey(id)) {
						manager.getSubtask(id);
					}
				}
				manager.id = managerId + 1;
				return manager;
			} else {
				System.out.println("Данные отсутствуют на сервере, воспользуйтесь резервной копией.");
			}
		} catch (ExceptionInInitializerError e) {
			System.out.println(e.getMessage());
		} catch (IllegalStateException e) {
			System.out.println("Ошибка при восстановлении.");
		} catch (NullPointerException e) {
			System.out.println("Критическая ошибка при восстановлении.");
		}
		return manager;
	}
	
	@Override
	public Set<Task> getPrioritizedTasks() {
		Set<Task> prioritizedTasks = super.getPrioritizedTasks();
		save();
		return prioritizedTasks;
	}
}
