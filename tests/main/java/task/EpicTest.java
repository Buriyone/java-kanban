package main.java.task;

import main.java.manager.InMemoryTaskManager;
import main.java.manager.TaskManager;
import main.java.auxiliary.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
	private static final TaskManager manager = new InMemoryTaskManager();
	private static Epic epic;
	private static Subtask sub1;
	private static Subtask sub2;
	
	private static int epicId;
	private static int subId1;
	private static int subId2;
	
	@BeforeEach
	public void initialManager() {
		epic = new Epic("epicName", "descriptionEpic");
		manager.addEpic(epic);
		epicId = epic.getId();
		
		sub1 = new Subtask("sub1", "descriptionSub1", epicId);
		manager.addSubtask(sub1);
		subId1 = sub1.getId();
		
		sub2 = new Subtask("sub2", "descriptionSub2", epicId);
		manager.addSubtask(sub2);
		subId2 = sub2.getId();
	}
	
	@Test
	public void subtasksStatusNewTest() {
		assertNotNull(manager.getEpic(epicId), "Эпик не найден.");
		assertNotNull(manager.getSubtask(subId1), "Подзадача 1 не найдена.");
		assertNotNull(manager.getSubtask(subId2), "Подзадача 2 не найдена.");
		
		sub1.setStatus(Status.NEW);
		manager.updateSubtask(sub1);
		
		sub2.setStatus(Status.NEW);
		manager.updateSubtask(sub2);
		
		assertEquals(Status.NEW, epic.getStatus(), "Статус не идентичен.");
	}
	
	@Test
	public void subtasksStatusDoneTest() {
		assertNotNull(manager.getEpic(epicId), "Эпик не найден.");
		assertNotNull(manager.getSubtask(subId1), "Подзадача 1 не найдена.");
		assertNotNull(manager.getSubtask(subId2), "Подзадача 2 не найдена.");
		
		sub1.setStatus(Status.DONE);
		manager.updateSubtask(sub1);
		
		sub2.setStatus(Status.DONE);
		manager.updateSubtask(sub2);
		
		assertEquals(Status.DONE, epic.getStatus(), "Статус не идентичен.");
	}
	
	@Test
	public void subtasksStatusDoneAndNewTest() {
		assertNotNull(manager.getEpic(epicId), "Эпик не найден.");
		assertNotNull(manager.getSubtask(subId1), "Подзадача 1 не найдена.");
		assertNotNull(manager.getSubtask(subId2), "Подзадача 2 не найдена.");
		
		sub1.setStatus(Status.DONE);
		manager.updateSubtask(sub1);
		
		sub2.setStatus(Status.NEW);
		manager.updateSubtask(sub2);
		
		assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус не идентичен.");
	}
	
	@Test
	public void subtasksStatusInProgressTest() {
		assertNotNull(manager.getEpic(epicId), "Эпик не найден.");
		assertNotNull(manager.getSubtask(subId1), "Подзадача 1 не найдена.");
		assertNotNull(manager.getSubtask(subId2), "Подзадача 2 не найдена.");
		
		sub1.setStatus(Status.IN_PROGRESS);
		manager.updateSubtask(sub1);
		
		sub2.setStatus(Status.IN_PROGRESS);
		manager.updateSubtask(sub2);
		
		assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус не идентичен.");
	}
}