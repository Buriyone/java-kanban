package main.java.data;

import main.java.managers.FileBackedTasksManager;
import main.java.managers.TaskManager;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DataBaseTest {
	private static int taskEnder = 1;
	private static int epicEnder = 1;
	private static int subtaskEnder = 1;
	
	private static final File testFile = new File("tests/main/resources/testFile.csv");
	private static FileBackedTasksManager manager;
	
	@BeforeEach
	void initialManager() {
		manager = new FileBackedTasksManager(Path.of(testFile.getPath()));
	}
	@AfterEach
	void cleanerManager() {
		manager.deleteTasks();
		manager.deleteEpics();
		
		taskEnder = 1;
		epicEnder = 1;
		subtaskEnder = 1;
	}
	
	@Test
	public void taskTimeTest() {
		Task task = createTask();
		int id = task.getId();
		
		assertFalse(manager.getTasks().isEmpty(), "Задача не была добавлена.");
		assertEquals(task, manager.getTask(id), "Задачи отличаются");
		assertEquals(task.getStartTime(), manager.getTask(id).getStartTime(), "Время начала задачи отличается");
		assertEquals(task.getDuration(), manager.getTask(id).getDuration(),
				"Время выполнения задачи отличается");
		assertEquals(task.getEndTime(), manager.getTask(id).getEndTime(), "Время завершения задачи отличается");
		
		TaskManager managerTest = FileBackedTasksManager.loadFromFile(testFile);
		
		assertFalse(managerTest.getTasks().isEmpty(), "Задача не была восстановлена.");
		assertEquals(task, managerTest.getTasks().get(0), "После восстановления задачи отличаются");
		assertEquals(task.getStartTime(), managerTest.getTask(id).getStartTime(),
				"После восстановления время начала задачи отличается");
		assertEquals(task.getDuration(), managerTest.getTask(id).getDuration(),
				"После восстановления время выполнения задачи отличается");
		assertEquals(task.getEndTime(), managerTest.getTask(id).getEndTime(),
				"После восстановления время завершения задачи отличается");
	}
	
	@Test
	public void subtaskTimeTest() {
		Epic epic = createEpic();
		int epic_id = epic.getId();
		
		Subtask subtask = createSubtask(epic_id);
		int subtask_id = subtask.getId();
		
		assertFalse(manager.getSubtasks().isEmpty(), "Подзадача не была добавлена.");
		assertEquals(subtask, manager.getSubtask(subtask_id), "Подзадачи отличаются");
		assertEquals(subtask.getStartTime(), manager.getSubtask(subtask_id).getStartTime(),
				"Время начала подзадачи отличается");
		assertEquals(subtask.getDuration(), manager.getSubtask(subtask_id).getDuration(),
				"Время выполнения подзадачи отличается");
		assertEquals(subtask.getEndTime(), manager.getSubtask(subtask_id).getEndTime(),
				"Время завершения подзадачи отличается");
		
		TaskManager managerTest = FileBackedTasksManager.loadFromFile(testFile);
		
		assertFalse(managerTest.getSubtasks().isEmpty(), "Подзадача не была восстановлена.");
		assertEquals(subtask, managerTest.getSubtask(subtask_id), "После восстановления подзадачи отличаются");
		assertEquals(subtask.getStartTime(), managerTest.getSubtask(subtask_id).getStartTime(),
				"После восстановления время начала подзадачи отличается");
		assertEquals(subtask.getDuration(), managerTest.getSubtask(subtask_id).getDuration(),
				"После восстановления время выполнения подзадачи отличается");
		assertEquals(subtask.getEndTime(), managerTest.getSubtask(subtask_id).getEndTime(),
				"После восстановления время завершения подзадачи отличается");
	}
	
	@Test
	public void epicTimeTest() {
		Epic epic = createEpic();
		int epic_id = epic.getId();
		
		Subtask sub1 = createSubtask(epic_id);
		
		Subtask sub2 = createSubtask(epic_id);
		
		assertFalse(manager.getEpics().isEmpty(), "Эпик не был добавлен.");
		assertFalse(manager.getSubtasks().isEmpty(), "Подзадачи не были добавлены.");
		assertEquals(epic, manager.getEpic(epic_id), "Эпик отличается.");
		assertEquals(epic.getSubtasks(), manager.getSubtasks(), "Подзадачи отличаются.");
		assertEquals(epic.getStartTime(), manager.getEpic(epic_id).getStartTime(),
				"Время начала эпика отличается.");
		assertEquals(epic.getStartTime(), sub1.getStartTime(),
				"Время начала эпика не соответствует началу первой подзадачи.");
		assertEquals(epic.getEndTime(), sub2.getEndTime(),
				"Время окончания эпика не соответствует окончанию последней подзадачи.");
		assertEquals(epic.getDuration(), manager.getEpic(epic_id).getDuration(),
				"Продолжительность эпиков отличается");
		
		TaskManager managerTest = FileBackedTasksManager.loadFromFile(testFile);
		
		assertFalse(managerTest.getEpics().isEmpty(), "Эпик не был восстановлен.");
		assertFalse(managerTest.getSubtasks().isEmpty(), "Подзадачи не были восстановлены.");
		assertEquals(epic, managerTest.getEpic(epic_id), "После восстановления эпик отличается.");
		assertEquals(epic.getSubtasks(), managerTest.getSubtasks(),
				"После восстановления подзадачи отличаются.");
		assertEquals(epic.getStartTime(), managerTest.getEpic(epic_id).getStartTime(),
				"После восстановления время начала эпика отличается.");
		assertEquals(epic.getDuration(), managerTest.getEpic(epic_id).getDuration(),
				"После восстановления продолжительность эпиков отличается");
	}
	
	@Test
	public void timeInheritance() {
		Task task = createTask();
		int id = task.getId();
		LocalDateTime time = task.getStartTime();
		
		assertFalse(manager.getTasks().isEmpty(), "Задача не была добавлена.");
		assertEquals(task, manager.getTask(id), "Задачи отличаются");
		
		Task taskUpdate = new Task("name", "description");
		manager.updateTask(taskUpdate, id);
		
		assertEquals(time, manager.getTask(id).getStartTime(), "Время начала не было унаследовано.");
		
		task.setStartTime(manager.getFreeTime());
		manager.updateTask(task, id);
		
		assertNotEquals(time, manager.getTask(id).getStartTime(), "Новое время не было установлено.");
	}
	
	@Test
	public void intersectionTest() {
		Task task1 = createTask();
		Task task2 = createTask();
		
		assertEquals(task1.getEndTime(), task2.getStartTime(), "Время пересекается.");
	}
	
	@Test
	public void getPrioritizedTasksTest() {
		Task task1 = createTask();
		Epic epic = createEpic();
		
		Subtask subtask1 = new Subtask("name", "description", epic.getId());
		manager.addSubtask(subtask1);
		
		Subtask subtask2 = createSubtask(epic.getId());
		
		Set<Task> referenceSet = new LinkedHashSet<>();
		referenceSet.add(task1);
		referenceSet.add(subtask2);
		referenceSet.add(subtask1);
		
		String referenceSting = referenceSet.toString();
		String testString = manager.getPrioritizedTasks().toString();
		
		assertFalse(manager.getPrioritizedTasks().isEmpty(), "Список приоритетных задач пуст.");
		assertEquals(referenceSting, testString, "Множества отличаются.");
		
		TaskManager managerTest = FileBackedTasksManager.loadFromFile(testFile);
		
		String testString2 = managerTest.getPrioritizedTasks().toString();
		
		assertFalse(managerTest.getPrioritizedTasks().isEmpty(),
				"Список приоритетных задач не был восстановлен.");
		assertEquals(referenceSting, testString2, "После восстановления множества отличаются.");
	}
	
	private static Task createTask() {
		Task task = new Task("Task_"+ taskEnder, "descriptionTask_" + taskEnder, manager.getFreeTime());
		manager.addTask(task);
		
		taskEnder += 1;
		
		return task;
	}
	
	private static Epic createEpic() {
		Epic epic = new Epic("Epic_"+ epicEnder, "descriptionEpic_" + epicEnder);
		manager.addEpic(epic);
		
		epicEnder += 1;
		
		return epic;
	}
	
	private static Subtask createSubtask(int id) {
		Subtask subtask = new Subtask("Sub_"+ subtaskEnder, "descriptionSub_" + subtaskEnder, id,
				manager.getFreeTime());
		manager.addSubtask(subtask);
		
		subtaskEnder += 1;
		
		return subtask;
	}
}