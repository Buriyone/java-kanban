package main.java.managers;

import main.java.tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
	private static final HistoryManager manager = new InMemoryHistoryManager();
	
	private static int ender = 1;
	
	private static int id = 1;
	
	
	@Test
	public void add() {
		Task task1 = createTask();
		
		assertEquals(0, manager.getHistory().size(), "История не пуста.");
		
		manager.add(task1);
		
		assertEquals(1, manager.getHistory().size(), "Задача не была добавлена.");
		assertEquals(task1, manager.getHistory().get(0), "Задачи отличаются.");
	}
	
	@Test
	public void remove() {
		Task task1 = createTask();
		Task task2 = createTask();
		
		assertEquals(0, manager.getHistory().size(), "История не пуста.");
		
		manager.add(task1);
		manager.add(task2);
		
		assertEquals(2, manager.getHistory().size(), "Задачи не были добавлены.");
		
		manager.remove(task1.getId());
		
		assertEquals(1, manager.getHistory().size(), "Задача не была удалена.");
		assertEquals(task2, manager.getHistory().get(0), "Удалена не та задача.");
	}
	
	@Test
	public void getHistory() {
		Task task1 = createTask();
		Task task2 = createTask();
		Task task3 = createTask();
		
		ArrayList<Task> referenceHistory = new ArrayList<>();
		
		referenceHistory.add(task1);
		referenceHistory.add(task2);
		referenceHistory.add(task3);
		
		assertEquals(0, manager.getHistory().size(), "История не пуста.");
		
		manager.add(task1);
		manager.add(task2);
		manager.add(task3);
		
		assertEquals(3, manager.getHistory().size(), "Задачи не были добавлены.");
		assertEquals(referenceHistory, manager.getHistory(), "История отличается.");
	}
	
	@AfterEach
	public void managerCleaner(){
		while (true) {
			if (!manager.getHistory().isEmpty() && id != 1) {
				manager.remove(id);
				
				id -= 1;
			} else if (!manager.getHistory().isEmpty() && id == 1) {
				manager.remove(id);
			} else {
				break;
			}
		}
	}
	
	private static Task createTask() {
		Task task = new Task("Task_"+ ender, "descriptionTask_" + ender);
		
		task.setId(id);
		
		ender += 1;
		id += 1;
		
		return task;
	}
}