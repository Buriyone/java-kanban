package main.java.managers;

import main.java.exception.TimeValidationException;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
	@BeforeEach
	public void initialManager(){
		manager = new InMemoryTaskManager();
		
		task = new Task("taskName", "taskDescription", LocalDateTime.now());
		manager.addTask(task);
		taskId = manager.getTasks().get(0).getId();
		
		epic = new Epic("epicName", "epicDescription");
		manager.addEpic(epic);
		epicId = manager.getEpics().get(0).getId();
		
		sub1 = new Subtask("subName", "subDescription", epicId);
		manager.addSubtask(sub1);
		subId1 = manager.getSubtasks().get(0).getId();
		
		sub2 = new Subtask("subName2", "subDescription2",
				task.getStartTime().plusMinutes(16), epicId);
		manager.addSubtask(sub2);
		subId2 = manager.getSubtasks().get(1).getId();
	}
	
	@Test
	public void updatePrioritizedTasksTest() {
		List<Task> expectList = new ArrayList<>();
		expectList.add(task);
		expectList.add(sub2);
		expectList.add(sub1);
		
		assertNotNull(manager.getPrioritizedTasks(), "Приоритетный список не найден.");
		assertFalse(manager.getPrioritizedTasks().isEmpty(), "Приоритетный список пуст.");
		assertEquals(expectList.toString(), manager.getPrioritizedTasks().toString(),
				"Приоритетный список не идентичен.");
	}
	
	@Test
	public void checkTimeValidationTest() throws TimeValidationException {
		assertNotNull(manager.getTask(taskId), "Задача не найдена.");
		assertNotNull(manager.getSubtask(subId2), "Подзадача не найдена.");
		
		String expectedException = "Начало выполнения задачи: "
				+ sub2.getId() + " пересекается с периодом выполнения задачи: " + task.getId();
		
		try {
			sub2.setStartTime(task.getStartTime().plusMinutes(5));
			manager.updateSubtask(sub2);
		} catch (TimeValidationException exception) {
			assertEquals(expectedException, exception.getMessage(), "Ошибки не идентичны.");
		}
		
		expectedException = "Окончание выполнения задачи: "
				+ sub2.getId() + " пересекается с периодом выполнения задачи: " + task.getId();
		
		try {
			sub2.setStartTime(task.getStartTime().minusMinutes(5));
			manager.updateSubtask(sub2);
		} catch (TimeValidationException exception) {
			assertEquals(expectedException, exception.getMessage(), "Ошибки не идентичны.");
		}
		
		expectedException = "Начало выполнения задачи: "
				+ sub2.getId() + " идентично началу выполнения задачи: " + task.getId();
		
		try {
			sub2.setStartTime(task.getStartTime());
			manager.updateSubtask(sub2);
		} catch (TimeValidationException exception) {
			assertEquals(expectedException, exception.getMessage(), "Ошибки не идентичны.");
		}
	}
}