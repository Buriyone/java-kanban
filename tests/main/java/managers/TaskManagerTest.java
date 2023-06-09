package main.java.managers;

import main.java.models.Status;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
	protected TaskManager manager;
	
	protected static int taskEnder = 1;
	protected static int epicEnder = 1;
	protected static int subtaskEnder = 1;
	
	@Test
	public void getTasksTest() {
		Task task1 = createTask();
		Task task2 = createTask();
		
		manager.addTask(task1);
		manager.addTask(task2);
		
		ArrayList<Task> referenceTasks = new ArrayList<>();
		referenceTasks.add(task1);
		referenceTasks.add(task2);
		
		assertNotNull(manager.getTasks(), "Задачи не возвращаются.");
		assertEquals(referenceTasks, manager.getTasks(), "Вернувшиеся задачи не совпадают.");
		assertEquals(referenceTasks.size(), manager.getTasks().size(), "Неверное количество задач.");
	}
	
	@Test
	public void deleteTasksTest() {
		Task task1 = createTask();
		Task task2 = createTask();
		
		manager.addTask(task1);
		manager.addTask(task2);
		
		assertNotNull(manager.getTasks(), "Задачи не возвращаются.");
		assertFalse(manager.getTasks().isEmpty(), "Список задач пуст.");
		
		manager.deleteTasks();
		
		assertTrue(manager.getTasks().isEmpty(), "Список задач не пуст.");
		assertEquals(0, manager.getTasks().size(), "Количество задач не совпадает.");
	}
	
	@Test
	public void getTaskTest() {
		Task task1 = createTask();
		
		manager.addTask(task1);
		
		int id = task1.getId();
		
		assertNotNull(manager.getTasks(), "Задачи не возвращаются.");
		assertEquals(task1, manager.getTask(id), "Вернувшаяся задача отличается.");
		assertNotEquals(task1, manager.getTask(id+31), "Неверный id не влияет на результат.");
	}
	
	@Test
	public void addTaskTest(){
		Task task1 = createTask();
		
		manager.addTask(task1);
		
		int id = task1.getId();
		
		assertNotNull(task1, "Задача не найдена.");
		assertEquals(task1, manager.getTask(id), "Задачи не совпадают.");
		assertNotNull(manager.getTasks(), "Задачи на возвращаются.");
		assertEquals(1, manager.getTasks().size(), "Неверное количество задач.");
		assertEquals(task1, manager.getTasks().get(0), "Задачи не совпадают.");
	}
	
	@Test
	public void updateTaskTest() {
		Task task1 = createTask();
		
		manager.addTask(task1);
		
		int id = task1.getId();
		
		assertNotNull(manager.getTask(id), "Задача не возвращается.");
		assertEquals(task1, manager.getTask(id), "Задачи не совпадают.");
		
		Task task1_1 = new Task("taskName1", "descriptionTask1", Status.DONE);
		
		manager.updateTask(task1_1, id);
		
		assertNotNull(manager.getTask(id), "После обновления задача не возвращается.");
		assertEquals(task1_1, manager.getTask(id), "После обновления задачи не совпадают.");
	}
	
	@Test
	public void deleteTaskTest() {
		Task task1 = createTask();
		Task task2 = createTask();
		
		manager.addTask(task1);
		manager.addTask(task2);
		
		int id = task1.getId();
		
		assertNotNull(manager.getTask(id), "Задача не возвращается.");
		assertNotNull(manager.getTasks(), "Задачи не возвращаются.");
		assertEquals(2, manager.getTasks().size(), "Количество задач отличается.");
		
		manager.deleteTask(id);
		
		assertNull(manager.getTask(id), "После удаления задача возвращается.");
		assertEquals(1, manager.getTasks().size(), "После удаления количество задач не изменилось");
	}
	
	@Test
	public void getEpicsTest() {
		Epic epic1 = createEpic();
		Epic epic2 = createEpic();
		
		manager.addEpic(epic1);
		manager.addEpic(epic2);
		
		ArrayList<Epic> referenceEpics = new ArrayList<>();
		referenceEpics.add(epic1);
		referenceEpics.add(epic2);
		
		assertNotNull(manager.getEpics(), "Эпики не возвращаются.");
		assertEquals(referenceEpics, manager.getEpics(), "Вернувшиеся эпики не совпадают.");
		assertEquals(referenceEpics.size(), manager.getEpics().size(), "Неверное количество эпиков.");
	}
	
	@Test
	public void deleteEpicsTest() {
		Epic epic1 = createEpic();
		Epic epic2 = createEpic();
		
		manager.addEpic(epic1);
		manager.addEpic(epic2);
		
		assertNotNull(manager.getEpics(), "Эпики не возвращаются.");
		assertFalse(manager.getEpics().isEmpty(), "Список эпиков пуст.");
		
		manager.deleteEpics();
		
		assertTrue(manager.getEpics().isEmpty(), "Список эпиков не пуст.");
		assertEquals(0, manager.getEpics().size(), "Количество эпиков не совпадает.");
	}
	
	@Test
	public void getEpicTest() {
		Epic epic1 = createEpic();
		
		manager.addEpic(epic1);
		
		int id = epic1.getId();
		
		assertNotNull(manager.getEpics(), "Эпики не возвращаются.");
		assertEquals(epic1, manager.getEpic(id), "Вернувшийся эпик отличается.");
		assertNotEquals(epic1, manager.getEpic(id+31), "Неверный id не влияет на результат.");
	}
	
	@Test
	public void addEpicTest() {
		Epic epic1 = createEpic();
		
		manager.addEpic(epic1);
		
		int id = epic1.getId();
		
		assertNotNull(manager.getEpic(id), "Эпик не найден.");
		assertEquals(epic1, manager.getEpic(id), "Эпики не совпадают.");
		assertNotNull(manager.getEpics(), "Эпики на возвращаются.");
		assertEquals(1, manager.getEpics().size(), "Неверное количество эпиков.");
		assertEquals(epic1, manager.getEpics().get(0), "Эпики не совпадают.");
	}
	
	@Test
	public void updateEpicTest() {
		Epic epic1 = createEpic();
		
		manager.addEpic(epic1);
		
		int id = epic1.getId();
		
		manager.addSubtask(createSubtask(id));
		manager.addSubtask(createSubtask(id));
		
		assertNotNull(manager.getEpic(id), "Эпик не возвращается.");
		assertEquals(epic1, manager.getEpic(id), "Эпики не совпадают.");
		
		Epic epic1_1 = createEpic();
		
		manager.updateEpic(epic1_1, id);
		
		assertNotNull(manager.getEpic(id), "После обновления эпик не возвращается.");
		assertEquals(epic1_1, manager.getEpic(id), "После обновления эпики не совпадают.");
		assertEquals(epic1.getId(), manager.getEpic(id).getId(), "После обновления id не совпадает.");
		assertEquals(epic1.getStatus(), manager.getEpic(id).getStatus(),
				"После обновления статус не совпадает.");
		assertEquals(epic1.getSubtasks(), manager.getEpic(id).getSubtasks(),
				"После обновления список подзадач не совпадает.");
	}
	
	@Test
	public void deleteEpicTest() {
		Epic epic1 = createEpic();
		Epic epic2 = createEpic();
		
		manager.addEpic(epic1);
		manager.addEpic(epic2);
		
		int id = epic1.getId();
		
		manager.addSubtask(createSubtask(id));
		manager.addSubtask(createSubtask(id));
		
		assertNotNull(manager.getEpic(id), "Эпик не возвращается.");
		assertNotNull(manager.getEpics(), "Эпики не возвращаются.");
		assertEquals(2, manager.getEpics().size(), "Количество эпиков отличается.");
		assertEquals(2, manager.getSubtasks().size(), "Количество подзадач отличается.");
		assertEquals(2, manager.getEpic(id).getSubtasks().size(),
				"Количество подзадач внутри эпика отличается.");
		assertEquals(epic1.getSubtasks(), manager.getEpic(id).getSubtasks(), "Подзадачи отличаются.");
		
		manager.deleteEpic(id);
		
		assertNull(manager.getEpic(id), "Эпик не был удален.");
		assertEquals(1, manager.getEpics().size(), "После удаления количество эпиков не изменилось.");
		assertEquals(0, manager.getSubtasks().size(),
				"После удаления эпика его подзадачи не были удалены.");
	}
	
	@Test
	public void getEpicSubtasksTest() {
		Epic epic1 = createEpic();
		
		manager.addEpic(epic1);
		
		int id = epic1.getId();
		
		Subtask sub1 = createSubtask(id);
		Subtask sub2 = createSubtask(id);
		
		ArrayList<Subtask> referenceSubtasks = new ArrayList<>();
		referenceSubtasks.add(sub1);
		referenceSubtasks.add(sub2);
		
		manager.addSubtask(sub1);
		manager.addSubtask(sub2);
		
		assertNotNull(manager.getEpicSubtasks(id), "Подзадачи эпика не возвращаются.");
		assertEquals(2, manager.getEpicSubtasks(id).size(), "Количество подзадач эпика отличается.");
		assertEquals(referenceSubtasks, manager.getEpicSubtasks(id), "Подзадачи эпиков отличаются.");
		assertEquals(manager.getSubtasks(), manager.getEpic(id).getSubtasks(),
				"Списки подзадач эпика и подзадач в мапе отличаются.");
	}
	
	@Test
	public void getSubtasksTest() {
		Epic epic = createEpic();
		
		manager.addEpic(epic);
		
		int id = epic.getId();
		
		Subtask sub1 = createSubtask(id);
		Subtask sub2 = createSubtask(id);
		
		ArrayList<Subtask> referenceSubtask = new ArrayList<>();
		referenceSubtask.add(sub1);
		referenceSubtask.add(sub2);
		
		manager.addSubtask(sub1);
		manager.addSubtask(sub2);
		
		assertNotNull(manager.getSubtasks(), "Подзадачи не возвращаются.");
		assertEquals(referenceSubtask, manager.getSubtasks(), "Вернувшиеся подзадачи не совпадают.");
		assertEquals(referenceSubtask.size(), manager.getSubtasks().size(),
				"Количество подзадач не совпадает.");
	}
	
	@Test
	public void deleteSubtasksTest() {
		Epic epic = createEpic();
		
		manager.addEpic(epic);
		
		int id = epic.getId();
		
		Subtask sub1 = createSubtask(id);
		Subtask sub2 = createSubtask(id);
		
		manager.addSubtask(sub1);
		manager.addSubtask(sub2);
		
		assertNotNull(manager.getSubtasks(), "Подзадачи не возвращаются.");
		assertFalse(manager.getSubtasks().isEmpty(), "Список подзадач пуст.");
		assertEquals(2, manager.getSubtasks().size(), "Количество подзадач отличается.");
		assertEquals(2, manager.getEpic(id).getSubtasks().size(),
				"Количество подзадач эпика отличается.");
		
		manager.deleteSubtasks();
		
		assertTrue(manager.getSubtasks().isEmpty(), "Список подзадач не пуст.");
		assertTrue(manager.getSubtasks().isEmpty(), "Список подзадач внутри эпика не пуст.");
	}
	
	@Test
	public void getSubtaskTest() {
		Epic epic = createEpic();
		
		manager.addEpic(epic);
		
		int id = epic.getId();
		
		Subtask sub1 = createSubtask(id);
		
		manager.addSubtask(sub1);
		
		int subId = sub1.getId();
		
		assertEquals(sub1, manager.getSubtask(subId), "Вернувшаяся подзадача отличается.");
		assertNotEquals(sub1, manager.getSubtask(subId+31), "Неверный id не влияет на результат.");
	}
	
	@Test
	public void addSubtaskTest() {
		Epic epic = createEpic();
		
		manager.addEpic(epic);
		
		int id = epic.getId();
		
		Subtask sub1 = createSubtask(id);
		
		manager.addSubtask(sub1);
		
		int subId = sub1.getId();
		
		assertNotNull(manager.getSubtask(subId), "Подзадача не найдена.");
		assertEquals(sub1, manager.getSubtask(subId), "Подзадачи не совпадают.");
		assertEquals(1, manager.getSubtasks().size(), "Неверное количество подзадач.");
		assertEquals(epic.getSubtasks().get(0), manager.getSubtask(subId),
				"Подзадача внутри эпика и в мапе отличается.");
	}
	
	@Test
	public void updateSubtaskTest() {
		Epic epic = createEpic();
		
		manager.addEpic(epic);
		
		int epicId = epic.getId();
		
		Subtask sub1 = createSubtask(epicId);
		Subtask sub2 = createSubtask(epicId);
		
		manager.addSubtask(sub1);
		manager.addSubtask(sub2);
		
		int subId_1 = sub1.getId();
		int subId_2 = sub2.getId();
		
		assertNotNull(manager.getSubtask(subId_1), "Подзадача не возвращается.");
		assertEquals(sub1, manager.getSubtask(subId_1), "Подзадачи не совпадают.");
		assertEquals(Status.NEW, manager.getEpic(epicId).getStatus(), "Статусы не совпадают");
		
		sub1.setStatus(Status.IN_PROGRESS);
		
		manager.updateSubtask(sub1, subId_1);
		
		assertNotNull(manager.getSubtask(subId_1), "После обновления 1 подзадача не возвращается.");
		assertEquals(sub1, manager.getSubtask(subId_1), "После обновления 1 подзадачи не совпадают.");
		assertEquals(Status.IN_PROGRESS, manager.getEpic(epicId).getStatus(),
				"После обновления 1 статусы не совпадают");
		
		sub1.setStatus(Status.DONE);
		
		manager.updateSubtask(sub1, subId_1);
		
		assertNotNull(manager.getSubtask(subId_1), "После обновления 2 подзадача не возвращается.");
		assertEquals(sub1, manager.getSubtask(subId_1), "После обновления 2 подзадачи не совпадают.");
		assertEquals(Status.IN_PROGRESS, manager.getEpic(epicId).getStatus(), "Статусы не совпадают");
		
		sub2.setStatus(Status.IN_PROGRESS);
		
		manager.updateSubtask(sub2, subId_2);
		
		assertNotNull(manager.getSubtask(subId_2), "После обновления 3 подзадача не возвращается.");
		assertEquals(sub2, manager.getSubtask(subId_2), "После обновления 3 подзадачи не совпадают.");
		assertEquals(Status.IN_PROGRESS, manager.getEpic(epicId).getStatus(), "Статусы не совпадают");
		
		sub2.setStatus(Status.DONE);
		
		manager.updateSubtask(sub2, subId_2);
		
		assertNotNull(manager.getSubtask(subId_2), "После обновления 4 подзадача не возвращается.");
		assertEquals(sub2, manager.getSubtask(subId_2), "После обновления 4 подзадачи не совпадают.");
		assertEquals(Status.DONE, manager.getEpic(epicId).getStatus(), "Статусы не совпадают");
	}
	
	@Test
	public void deleteSubtaskTest() {
		Epic epic = createEpic();
		
		manager.addEpic(epic);
		
		int epicId = epic.getId();
		
		Subtask sub1 = createSubtask(epicId);
		Subtask sub2 = createSubtask(epicId);
		
		manager.addSubtask(sub1);
		manager.addSubtask(sub2);
		
		int subId_1 = sub1.getId();
		int subId_2 = sub2.getId();
		
		sub1.setStatus(Status.IN_PROGRESS);
		sub2.setStatus(Status.DONE);
		
		manager.updateSubtask(sub1, subId_1);
		manager.updateSubtask(sub2, subId_2);
		
		assertNotNull(manager.getSubtasks(), "Подзадачи не возвращаются.");
		assertEquals(2, manager.getSubtasks().size(), "Количество подзадач отличается.");
		assertEquals(Status.IN_PROGRESS, manager.getEpic(epicId).getStatus(), "Статусы отличаются.");
		
		manager.deleteSubtask(subId_1);
		
		assertNull(manager.getSubtask(subId_1), "Подзадача не была удалена.");
		assertEquals(1, manager.getSubtasks().size(), "После удаления количество задач не изменилось");
		assertEquals(Status.DONE, manager.getEpic(epicId).getStatus(), "Статусы отличаются.");
		
		manager.deleteSubtask(subId_2);
		
		assertNull(manager.getSubtask(subId_2), "Подзадача не была удалена.");
		assertEquals(0, manager.getSubtasks().size(), "После удаления количество задач не изменилось");
		assertEquals(Status.NEW, manager.getEpic(epicId).getStatus(), "Статусы отличаются.");
	}
	
	@Test
	public void getHistoryTest() {
		Task task1 = createTask();
		Task task2 = createTask();
		Task task3 = createTask();
		
		manager.addTask(task1);
		manager.addTask(task2);
		manager.addTask(task3);
		
		int taskId_1 = task1.getId();
		int taskId_2 = task2.getId();
		int taskId_3 = task3.getId();
		
		assertEquals(0, manager.getHistory().size(), "История не пуста.");
		
		ArrayList<Task> referenceHistory = new ArrayList<>();
		
		referenceHistory.add(manager.getTask(taskId_1));
		referenceHistory.add(manager.getTask(taskId_2));
		referenceHistory.add(manager.getTask(taskId_3));
		
		assertNotNull(manager.getHistory(), "История не возвращается.");
		assertEquals(referenceHistory, manager.getHistory(), "История отличается.");
		
		referenceHistory.remove(1);
		manager.deleteTask(taskId_2);
		
		assertEquals(referenceHistory, manager.getHistory(),
				"После удаления из середины - история отличается.");
		
		referenceHistory.remove(1);
		manager.deleteTask(taskId_3);
		
		assertEquals(referenceHistory, manager.getHistory(),
				"После удаления из конца - история отличается.");
		
		manager.getTask(taskId_1);
		manager.getTask(taskId_1);
		
		assertEquals(referenceHistory, manager.getHistory(), "Дублирование влияет на историю.");
	}
	
	protected static Task createTask() {
		Task task = new Task("Task_"+ taskEnder, "descriptionTask_" + taskEnder);
		
		taskEnder += 1;
		
		return task;
	}
	
	protected static Epic createEpic() {
		Epic epic = new Epic("Epic_"+ epicEnder, "descriptionEpic_" + epicEnder);
		
		epicEnder += 1;
		
		return epic;
	}
	
	protected static Subtask createSubtask(int id) {
		Subtask subtask = new Subtask("Sub_"+ subtaskEnder, "descriptionSub_" + subtaskEnder, id);
		
		subtaskEnder += 1;
		
		return subtask;
	}
}