package main.java.manager;

import main.java.auxiliary.Status;
import main.java.task.Epic;
import main.java.task.Subtask;
import main.java.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{
	private static File file;
	
	@BeforeEach
	public void initialManager() {
		try {
			file = File.createTempFile("tempFile", ".csv");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		manager = new FileBackedTasksManager(file.toPath());
		
		task = new Task("taskName", "taskDescription");
		manager.addTask(task);
		taskId = task.getId();
		
		epic = new Epic("epicName", "epicDescription");
		manager.addEpic(epic);
		epicId = epic.getId();
		
		sub1 = new Subtask("subName", "subDescription", epicId);
		manager.addSubtask(sub1);
		subId1 = sub1.getId();
		
		sub2 = new Subtask("subName2", "subDescription2", epicId);
		manager.addSubtask(sub2);
		subId2 = sub2.getId();
	}
	
	@Test
	public void loadFromFileTest(){
		assertNotNull(manager.getTask(taskId), "Задача не найдена.");
		assertNotNull(manager.getEpic(epicId), "Эпик не найден.");
		assertNotNull(manager.getSubtask(subId1), "Подзадача 1 не найдена.");
		assertNotNull(manager.getSubtask(subId2), "Подзадача 2 не найдена.");
		assertNotNull(manager.getTasks(), "Список задач не найден.");
		assertFalse(manager.getTasks().isEmpty(), "Список задач пуст.");
		assertNotNull(manager.getEpics(), "Список эпиков не найден.");
		assertFalse(manager.getEpics().isEmpty(), "Список эпиков пуст.");
		assertNotNull(manager.getSubtasks(), "Список подзадач не найден.");
		assertFalse(manager.getSubtasks().isEmpty(), "Список подзадач пуст");
		assertNotNull(manager.getEpicSubtasks(epicId), "подзадачи эпика не найдены.");
		assertFalse(manager.getEpicSubtasks(epicId).isEmpty(), "Список подзадач эпика пуст.");
		assertNotNull(manager.getPrioritizedTasks(), "Приоритетный список не найден.");
		assertFalse(manager.getPrioritizedTasks().isEmpty(), "Приоритетный список пуст.");
		assertNotNull(manager.getHistory(), "История не найдена.");
		assertFalse(manager.getHistory().isEmpty(), "История пуста.");
		
		task.setStartTime(LocalDateTime.now());
		task.setStatus(Status.IN_PROGRESS);
		manager.updateTask(task);
		
		sub1.setStatus(Status.DONE);
		sub1.setName("newName");
		manager.updateSubtask(sub1);
		
		sub2.setStartTime(LocalDateTime.now());
		sub2.setDescription("newDescription");
		manager.updateSubtask(sub2);
		
		TaskManager testManager = FileBackedTasksManager.loadFromFile(file);
		
		assertNotNull(testManager.getTask(taskId), "Задача не восстановлена.");
		assertNotNull(testManager.getEpic(epicId), "Эпик не восстановлен.");
		assertNotNull(testManager.getSubtask(subId1), "Подзадача 1 не восстановлена.");
		assertNotNull(testManager.getSubtask(subId2), "Подзадача 2 не восстановлена.");
		assertNotNull(testManager.getTasks(), "Список задач не восстановлен.");
		assertFalse(testManager.getTasks().isEmpty(), "Список задач пуст.");
		assertNotNull(testManager.getEpics(), "Список эпиков не восстановлен.");
		assertFalse(testManager.getEpics().isEmpty(), "Список эпиков пуст.");
		assertNotNull(testManager.getSubtasks(), "Список подзадач не восстановлен.");
		assertFalse(testManager.getSubtasks().isEmpty(), "Список подзадач пуст");
		assertNotNull(testManager.getEpicSubtasks(epicId), "подзадачи эпика не восстановлены.");
		assertFalse(testManager.getEpicSubtasks(epicId).isEmpty(), "Список подзадач эпика пуст.");
		assertNotNull(testManager.getPrioritizedTasks(), "Приоритетный список не восстановлен.");
		assertFalse(testManager.getPrioritizedTasks().isEmpty(), "Приоритетный список пуст.");
		assertNotNull(testManager.getHistory(), "История не восстановлена.");
		assertFalse(testManager.getHistory().isEmpty(), "История пуста.");
		
		assertEquals(manager.getHistory().toString(), testManager.getHistory().toString(),
				"История не идентична");
		assertEquals(manager.getTask(taskId), testManager.getTask(taskId), "Задача не идентична.");
		assertEquals(manager.getEpic(epicId), testManager.getEpic(epicId), "Эпик не идентичен.");
		assertEquals(manager.getSubtask(subId1), testManager.getSubtask(subId1), "Подзадача 1 не идентична.");
		assertEquals(manager.getSubtask(subId2), testManager.getSubtask(subId2), "Подзадача 2 не идентична.");
		assertEquals(manager.getTasks(), testManager.getTasks(), "Списки задач не идентичны.");
		assertEquals(manager.getEpics(), testManager.getEpics(), "Списки эпиков не идентичны.");
		assertEquals(manager.getSubtasks(), testManager.getSubtasks(), "Списки подзадач не идентичны.");
		assertEquals(manager.getEpicSubtasks(epicId), testManager.getEpicSubtasks(epicId),
				"Списки подзадач эпика не идентичны.");
		assertEquals(manager.getPrioritizedTasks().toString(), testManager.getPrioritizedTasks().toString(),
				"Списки приоритетных задач не идентичны.");
	}
}