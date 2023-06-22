package main.java.manager;

import main.java.auxiliary.Status;
import main.java.task.Epic;
import main.java.task.Subtask;
import main.java.task.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
	protected static TaskManager manager;
	
	protected static Task task;
	protected static Epic epic;
	protected static Subtask sub1;
	protected static Subtask sub2;
	protected static int taskId;
	protected static int epicId;
	protected static int subId1;
	protected static int subId2;
	
	@Test
	public void getTasksTest() {
		assertNotNull(manager.getTasks(), "Задачи не возвращаются.");
		assertEquals(task, manager.getTasks().get(0), "Вернувшиеся задачи не идентичны.");
		assertEquals(1, manager.getTasks().size(), "Количество задач не совпадает.");
	}
	
	@Test
	public void deleteTasksTest() {
		assertNotNull(manager.getTasks(), "Задачи не возвращаются.");
		assertFalse(manager.getTasks().isEmpty(), "Список задач пуст.");
		assertEquals(1, manager.getTasks().size(), "Количество задач не совпадает.");
		
		manager.deleteTasks();
		
		assertTrue(manager.getTasks().isEmpty(), "Список задач не пуст.");
		assertEquals(0, manager.getTasks().size(), "Количество задач не совпадает.");
	}
	
	@Test
	public void getTaskTest() {
		assertNotNull(manager.getTask(taskId), "Задача не найдена.");
		assertEquals(task, manager.getTask(taskId), "Вернувшаяся задача отличается.");
		assertNotEquals(task, manager.getTask(taskId+31), "Неверный id не влияет на результат.");
	}
	
	@Test
	public void addTaskTest(){
		assertNotNull(manager.getTasks(), "Задачи не найдены.");
		assertEquals(1, manager.getTasks().size(), "Количество задач не совпадает.");
		
		Task tempTask = new Task("name", "description");
		manager.addTask(tempTask);
		final int id = tempTask.getId();
		
		assertNotNull(manager.getTask(id), "Задача не найдена.");
		assertNotNull(manager.getTasks(), "Задачи не найдены.");
		assertEquals(tempTask, manager.getTask(id), "Задачи не идентичны.");
		assertEquals(2, manager.getTasks().size(), "Количество задач не совпадает.");
	}
	
	@Test
	public void updateTaskTest() {
		task.setName("updateName");
		task.setDescription("updateDescription");
		task.setStatus(Status.IN_PROGRESS);
		task.setStartTime(LocalDateTime.now().plusMinutes(86));
		manager.updateTask(task);
		
		assertNotNull(manager.getTask(taskId), "После обновления задача не найдена.");
		assertEquals(task, manager.getTask(taskId), "После обновления задачи не идентичны.");
	}
	
	@Test
	public void deleteTaskTest() {
		assertNotNull(manager.getTask(taskId), "Задача не найдена.");
		assertNotNull(manager.getTasks(), "Задачи не возвращаются.");
		assertEquals(1, manager.getTasks().size(), "Количество задач не совпадает.");
		
		manager.deleteTask(taskId);
		
		assertNull(manager.getTask(taskId), "После удаления задача обнаружена.");
		assertEquals(0, manager.getTasks().size(), "После удаления количество задач не совпадает.");
	}
	
	@Test
	public void getEpicsTest() {
		assertNotNull(manager.getEpics(), "Эпики не возвращаются.");
		assertEquals(epic, manager.getEpics().get(0), "Вернувшиеся эпики не совпадают.");
		assertEquals(1, manager.getEpics().size(), "Количество эпиков не совпадает.");
	}
	
	@Test
	public void deleteEpicsTest() {
		assertNotNull(manager.getEpics(), "Эпики не возвращаются.");
		assertFalse(manager.getEpics().isEmpty(), "Список эпиков пуст.");
		assertEquals(1, manager.getEpics().size(), "Количество эпиков не совпадает.");
		
		manager.deleteEpics();
		
		assertTrue(manager.getEpics().isEmpty(), "Список эпиков не пуст.");
		assertEquals(0, manager.getEpics().size(), "Количество эпиков не совпадает.");
	}
	
	@Test
	public void getEpicTest() {
		assertNotNull(manager.getEpic(epicId), "Эпик не найден.");
		assertEquals(epic, manager.getEpic(epicId), "Вернувшийся эпик отличается.");
		assertNotEquals(epic, manager.getEpic(epicId+31), "Неверный id не влияет на результат.");
	}
	
	@Test
	public void addEpicTest() {
		assertNotNull(manager.getEpics(), "Эпики не найдены.");
		assertEquals(1, manager.getEpics().size(), "Количество эпиков не совпадает.");
		
		Epic tempEpic = new Epic("name", "nameDescription");
		manager.addEpic(tempEpic);
		int id = tempEpic.getId();
		
		assertNotNull(manager.getEpic(id), "Эпик не найден.");
		assertNotNull(manager.getEpics(), "Эпики не найдены.");
		assertEquals(tempEpic, manager.getEpic(id), "Эпики не идентичны.");
		assertEquals(2, manager.getEpics().size(), "Количество эпиков не совпадает.");
	}
	
	@Test
	public void updateEpicTest() {
		epic.setName("updateName");
		epic.setDescription("updateDescription");
		
		manager.updateEpic(epic);
		
		assertNotNull(manager.getEpic(epicId), "Эпик не возвращается.");
		assertEquals(epic, manager.getEpic(epicId), "Эпики не идентичны.");
	}
	
	@Test
	public void updateStatusTest() {
		assertNotNull(manager.getEpic(epicId), "Эпик не найден.");
		assertEquals(manager.getEpic(epicId).getStatus(), Status.NEW, "Неверный статус.");
		
		sub1.setStatus(Status.DONE);
		manager.updateSubtask(sub1);
		
		assertEquals(manager.getEpic(epicId).getStatus(), Status.IN_PROGRESS, "Неверный статус.");
		
		sub2.setStatus(Status.IN_PROGRESS);
		manager.updateSubtask(sub2);
		
		assertEquals(manager.getEpic(epicId).getStatus(), Status.IN_PROGRESS, "Неверный статус.");
		
		manager.deleteSubtask(subId2);
		
		assertEquals(manager.getEpic(epicId).getStatus(), Status.DONE, "Неверный статус.");
		
		manager.deleteSubtasks();
		
		assertEquals(manager.getEpic(epicId).getStatus(), Status.NEW, "Неверный статус.");
	}
	
	@Test
	public void deleteEpicTest() {
		assertNotNull(manager.getEpic(epicId), "Эпик не возвращается.");
		assertNotNull(manager.getEpics(), "Эпики не возвращаются.");
		assertEquals(1, manager.getEpics().size(), "Количество эпиков отличается.");
		assertEquals(2, manager.getSubtasks().size(), "Количество подзадач эпика не совпадает.");
		
		manager.deleteEpic(epicId);
		
		assertNull(manager.getEpic(epicId), "Эпик не был удален.");
		assertEquals(0, manager.getEpics().size(), "После удаления количество эпиков не изменилось.");
		assertEquals(0, manager.getSubtasks().size(),
				"После удаления эпика его подзадачи не были удалены.");
	}
	
	@Test
	public void getEpicSubtasksTest() {
		List<Subtask> expectSubtasks = new ArrayList<>();
		expectSubtasks.add(sub1);
		expectSubtasks.add(sub2);
		
		assertNotNull(manager.getEpicSubtasks(epicId), "Подзадачи эпика не возвращаются.");
		assertEquals(2, manager.getEpicSubtasks(epicId).size(),
				"Количество подзадач эпика не совпадает.");
		assertEquals(expectSubtasks, manager.getEpicSubtasks(epicId), "Подзадачи эпика не идентичны.");
	}
	
	@Test
	public void getSubtasksTest() {
		List<Subtask> expectSubtasks = new ArrayList<>();
		expectSubtasks.add(sub1);
		expectSubtasks.add(sub2);
		
		assertNotNull(manager.getSubtasks(), "Подзадачи не возвращаются.");
		assertEquals(expectSubtasks, manager.getSubtasks(), "Вернувшиеся подзадачи не идентичны.");
		assertEquals(expectSubtasks.size(), manager.getSubtasks().size(),
				"Количество подзадач не совпадает.");
	}
	
	@Test
	public void deleteSubtasksTest() {
		assertNotNull(manager.getSubtasks(), "Подзадачи не возвращаются.");
		assertFalse(manager.getSubtasks().isEmpty(), "Список подзадач пуст.");
		assertEquals(2, manager.getSubtasks().size(), "Количество подзадач не совпадает.");
		assertEquals(2, manager.getEpic(epicId).getSubtasks().size(),
				"Количество подзадач эпика не совпадает.");
		
		manager.deleteSubtasks();
		
		assertTrue(manager.getSubtasks().isEmpty(), "Список подзадач не пуст.");
		assertTrue(manager.getSubtasks().isEmpty(), "Список подзадач внутри эпика не пуст.");
	}
	
	@Test
	public void getSubtaskTest() {
		assertNotNull(manager.getSubtask(subId1), "Подзадача не найдена");
		assertEquals(sub1, manager.getSubtask(subId1), "Вернувшаяся подзадача не идентична.");
		assertNotEquals(sub1, manager.getSubtask(subId1+31), "Неверный id не влияет на результат.");
	}
	
	@Test
	public void addSubtaskTest() {
		assertNotNull(manager.getSubtasks(), "Подзадачи не найдены.");
		assertNotNull(manager.getEpicSubtasks(epicId), "Подзадачи эпика не найдены.");
		assertEquals(2, manager.getSubtasks().size(), "Количество подзадач не совпадает.");
		assertEquals(2, manager.getEpicSubtasks(epicId).size(),
				"Количество подзадач эпика не совпадает.");
		
		Subtask tempSub = new Subtask("tempName", "tempDescription", epicId);
		manager.addSubtask(tempSub);
		final int id = tempSub.getId();
		
		assertNotNull(manager.getSubtask(id), "Подзадача не найдена.");
		assertNotNull(manager.getEpicSubtasks(epicId), "Подзадачи эпика не найдены.");
		assertEquals(tempSub, manager.getSubtask(id), "Подзадачи не идентичны.");
		assertEquals(3, manager.getSubtasks().size(), "Количество подзадач не совпадает.");
		assertEquals(3, manager.getEpicSubtasks(epicId).size(),
				"Количество подзадач эпика не совпадает.");
	}
	
	@Test
	public void updateSubtaskTest() {
		sub1.setName("updateName");
		sub1.setDescription("updateDescription");
		sub1.setStatus(Status.IN_PROGRESS);
		sub1.setStartTime(LocalDateTime.now().plusMinutes(35));
		manager.updateSubtask(sub1);
		
		assertNotNull(manager.getSubtask(subId1), "После обновления подзадача не найдена.");
		assertEquals(sub1, manager.getSubtask(subId1), "После обновления задачи не идентичны.");
	}
	
	@Test
	public void deleteSubtaskTest() {
		assertNotNull(manager.getSubtask(subId1), "Подзадача не найдена.");
		assertNotNull(manager.getSubtasks(), "Подзадачи не найдены.");
		assertNotNull(manager.getEpicSubtasks(epicId), "Подзадачи эпика не найдены.");
		assertEquals(2, manager.getSubtasks().size(), "Количество подзадач не совпадает.");
		assertEquals(2, manager.getEpicSubtasks(epicId).size(),
				"Количество подзадач эпика не совпадает.");
		
		manager.deleteSubtask(subId1);
		
		assertNull(manager.getSubtask(subId1), "После удаления задача обнаружена.");
		assertEquals(1, manager.getSubtasks().size(),
				"После удаления количество подзадач не совпадает.");
		assertEquals(1, manager.getEpicSubtasks(epicId).size(),
				"После удаления количество подзадач эпика не совпадает.");
		assertEquals(sub2, manager.getSubtasks().get(0), "Удалена не та подзадача.");
	}
	
	@Test
	public void getHistoryTest() {
		assertTrue(manager.getHistory().isEmpty(), "История не пуста.");
		
		List<Task> tempHistory = new ArrayList<>();
		tempHistory.add(manager.getSubtask(subId1));
		tempHistory.add(manager.getTask(taskId));
		tempHistory.add(manager.getEpic(epicId));
		
		String expectedHistory = tempHistory.toString();
		
		assertFalse(manager.getHistory().isEmpty(), "Задачи не были добавлены в историю.");
		assertEquals(3, manager.getHistory().size(), "Количество задач в истории не совпадает.");
		assertEquals(expectedHistory, manager.getHistory().toString(), "История не идентична.");
	}
}