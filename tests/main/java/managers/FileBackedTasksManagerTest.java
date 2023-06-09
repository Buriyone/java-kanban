package main.java.managers;

import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{
	File testFile = new File("tests/main/resources/testFile.csv");
	
	@BeforeEach
	public void initialManager() {
		manager = new FileBackedTasksManager(Path.of(testFile.getPath()));
	}
	
	@AfterEach
	public void cleanerManager() {
		manager.deleteTasks();
		manager.deleteEpics();
		
		taskEnder = 1;
		epicEnder = 1;
		subtaskEnder = 1;
	}
	
	@Test
	public void loadFromFile(){
		TaskManager testManager_1 = FileBackedTasksManager.loadFromFile(testFile);
		
		assertEquals(manager.getTasks(), testManager_1.getTasks(), "Список задач не пуст.");
		assertEquals(manager.getEpics(), testManager_1.getEpics(), "Список эпиков не пуст.");
		assertEquals(manager.getSubtasks(), testManager_1.getSubtasks(), "Список подзадач не пуст.");
		assertEquals(manager.getHistory(), testManager_1.getHistory(), "История не пуста.");
		
		Task task = createTask();
		Epic epic = createEpic();
		
		manager.addTask(task);
		manager.addEpic(epic);
		
		int taskId = task.getId();
		int epicId = epic.getId();
		
		TaskManager testManager_2 = FileBackedTasksManager.loadFromFile(testFile);
		
		assertFalse(testManager_2.getTasks().isEmpty(), "Задача не была восстановлена.");
		assertFalse(testManager_2.getEpics().isEmpty(), "Эпик не был восстановлен.");
		assertEquals(task, testManager_2.getTasks().get(0), "Задачи отличаются.");
		assertEquals(epic, testManager_2.getEpics().get(0), "Эпики отличаются.");
		assertTrue(testManager_2.getHistory().isEmpty(), "История не пуста.");
		
		Subtask sub = createSubtask(epicId);
		
		manager.addSubtask(sub);
		
		int subId = sub.getId();
		
		manager.getTask(taskId);
		manager.getEpic(epicId);
		manager.getSubtask(subId);
		
		TaskManager testManager_3 = FileBackedTasksManager.loadFromFile(testFile);
		
		assertFalse(testManager_3.getTasks().isEmpty(), "Задача не была восстановлена.");
		assertFalse(testManager_3.getEpics().isEmpty(), "Эпик не был восстановлен.");
		assertFalse(testManager_3.getSubtasks().isEmpty(), "подзадача не была восстановлена.");
		assertFalse(testManager_3.getHistory().isEmpty(), "История не была восстановлена.");
		assertEquals(task, testManager_3.getTasks().get(0), "Задачи отличаются.");
		assertEquals(epic, testManager_3.getEpics().get(0), "Эпики отличаются.");
		assertEquals(sub, testManager_3.getSubtasks().get(0), "Подзадачи отличаются.");
		assertEquals(manager.getEpicSubtasks(epicId), testManager_3.getEpicSubtasks(epicId),
				"Подзадачи эпиков отличаются.");
		assertEquals(manager.getHistory(), testManager_3.getHistory(), "Истории отличаются.");
	}
}