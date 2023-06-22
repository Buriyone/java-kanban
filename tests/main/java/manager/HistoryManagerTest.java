package main.java.manager;

import main.java.task.Epic;
import main.java.task.Subtask;
import main.java.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
	private static TaskManager manager;
	private static int taskId;
	private static int epicId;
	private static int subId;
	
	@BeforeEach
	public void initialManager() {
		manager = new InMemoryTaskManager();
		
		manager.addTask(new Task("taskName", "taskDescription"));
		taskId = manager.getTasks().get(0).getId();
		
		manager.addEpic(new Epic("epicName", "epicDescription"));
		epicId = manager.getEpics().get(0).getId();
		
		manager.addSubtask(new Subtask("subName", "subDescription", epicId));
		subId = manager.getSubtasks().get(0).getId();
	}
	
	
	@Test
	public void addToHistoryTest() {
		assertTrue(manager.getHistory().isEmpty(), "История не пуста.");
		
		manager.getTask(taskId);
		manager.getTask(taskId);
		
		assertFalse(manager.getHistory().isEmpty(), "Задача не была добавлена в историю.");
		assertEquals(1, manager.getHistory().size(), "Происходит дублирование задачи в истории.");
		assertEquals(manager.getTask(taskId), manager.getHistory().get(0), "Задача в истории отличается.");
	}
	
	@Test
	public void removeFromHistoryTest() {
		assertTrue(manager.getHistory().isEmpty(), "История не пуста.");
		
		manager.getTask(taskId);
		manager.getEpic(epicId);
		
		assertFalse(manager.getHistory().isEmpty(), "Задачи не были добавлены в историю.");
		
		manager.deleteTask(taskId);
		
		assertEquals(1, manager.getHistory().size(), "Задача не была удалена из истории.");
		assertEquals(manager.getEpic(epicId), manager.getHistory().get(0), "Удалена не та задача в истории.");
	}
	
	@Test
	public void getHistoryTest() {
		assertNotNull(manager.getHistory(), "История не найдена.");
		assertTrue(manager.getHistory().isEmpty(), "История не пуста.");
		
		List<Task> tempHistory = new ArrayList<>();
		tempHistory.add(manager.getTask(taskId));
		tempHistory.add(manager.getSubtask(subId));
		tempHistory.add(manager.getEpic(epicId));
		
		String expectHistory = tempHistory.toString();
		
		assertEquals(3, manager.getHistory().size(), "Количество задач в истории не совпадает.");
		assertEquals(expectHistory, manager.getHistory().toString(), "История не идентична.");
		
		tempHistory.remove(1);
		manager.deleteSubtask(subId);
		expectHistory = tempHistory.toString();
		
		assertEquals(2, manager.getHistory().size(),
				"После удаления из середины - количество задач в истории не совпадает.");
		assertEquals(expectHistory, manager.getHistory().toString(),
				"После удаления из середины - история не идентична.");
		
		tempHistory.remove(1);
		manager.deleteEpic(epicId);
		expectHistory = tempHistory.toString();
		
		assertEquals(1, manager.getHistory().size(),
				"После удаления из конца - количество задач в истории не совпадает.");
		assertEquals(expectHistory, manager.getHistory().toString(),
				"После удаления из конца - история не идентична.");
		
		manager.getTask(taskId);
		manager.getTask(taskId);
		
		assertEquals(expectHistory, manager.getHistory().toString(), "Дублирование влияет на историю.");
	}
}