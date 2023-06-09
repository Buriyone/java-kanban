package main.java.tasks;

import main.java.managers.InMemoryTaskManager;
import main.java.managers.TaskManager;
import main.java.models.Status;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
	private static final TaskManager manager = new InMemoryTaskManager();
	private static Epic epic;
	private static Subtask sub1;
	private static Subtask sub2;
	
	private static int ender = 1;
	
	@BeforeAll
	public static void start() {
		epic = new Epic("epicName", "descriptionEpic");
		
		manager.addEpic(epic);
		
		sub1 = new Subtask("sub1", "descriptionSub1", epic.getId());
		sub2 = new Subtask("sub2", "descriptionSub2", epic.getId());
		
		manager.addSubtask(sub1);
		manager.addSubtask(sub2);
	}
	@AfterAll
	public static void emptyTaskListTest() {
		manager.deleteSubtasks();
		
		assertEquals(Status.NEW, epic.getStatus());
		
		manager.deleteEpics();
	}
	
	@Test
	public void subtasksStatusNew() {
		updateSubtask(Status.NEW, Status.NEW);
		
		assertEquals(Status.NEW, epic.getStatus());
	}
	
	@Test
	public void subtasksStatusDone() {
		updateSubtask(Status.DONE, Status.DONE);
		
		assertEquals(Status.DONE, epic.getStatus());
	}
	
	@Test
	public void subtasksStatusDoneAndNew() {
		updateSubtask(Status.DONE, Status.NEW);
		
		assertEquals(Status.IN_PROGRESS, epic.getStatus());
	}
	
	@Test
	public void subtasksStatusInProgress() {
		updateSubtask(Status.IN_PROGRESS, Status.IN_PROGRESS);
		
		assertEquals(Status.IN_PROGRESS, epic.getStatus());
	}
	
	public static void updateSubtask(Status status1, Status status2) {
		manager.updateSubtask(new Subtask("sub1_" + ender, "descriptionSub1_" + ender,
				status1, epic.getId()), sub1.getId());
		
		manager.updateSubtask(new Subtask("sub2_" + ender, "descriptionSub2_" + ender,
				status2, epic.getId()), sub2.getId());
		
		ender += 1;
	}
}