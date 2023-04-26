package main.java;

import main.java.managers.Managers;
import main.java.models.Status;
import main.java.managers.TaskManager;
import main.java.tasks.Task;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.managers.FileBackedTasksManager;

import java.nio.file.Path;

public class Main {
	
	public static void main(String[] args) {
		System.out.println("Тест нового менеджера.");
		TaskManager manager = Managers.getDefault();
		
		//Блок создания нового файла с задачами.
		/*FileBackedTasksManager saveManager = new FileBackedTasksManager(Path.of("src/main/resources/file.csv"));
		
		Task task1 = new Task("TaskName1", "TaskDescription1");
		saveManager.addTask(task1);
		int taskId1 = saveManager.getTasks().get(0).getId();
		Task task1_1 = new Task("TaskName1", "TaskDescription1", Status.IN_PROGRESS);
		saveManager.updateTask(task1_1, taskId1);
		
		Epic epic1 = new Epic("EpicName1", "EpicDescription1");
		saveManager.addEpic(epic1);
		int epicId1 = saveManager.getEpics().get(0).getId();
		
		Subtask subtask1 = new Subtask("SubtaskName1", "SubDescription1", epicId1);
		saveManager.addSubtask(subtask1);
		int sub1 = saveManager.getSubtasks().get(0).getId();
		Subtask subtask1_1 = new Subtask("SubtaskName1", "SubDescription1", Status.DONE, epicId1);
		saveManager.updateSubtask(subtask1_1, sub1);
		
		//блок проверки работы InMemoryHistoryManager
		System.out.println(saveManager.getTask(taskId1));
		System.out.println(saveManager.getEpic(epicId1));
		System.out.println(saveManager.getSubtask(sub1));
		System.out.println(saveManager.getEpic(epicId1));
		System.out.println(saveManager.getTask(taskId1));
		System.out.println(saveManager.getSubtask(sub1));
		System.out.println(saveManager.getTask(taskId1));
		System.out.println(saveManager.getTask(taskId1));
		
		
		System.out.println("\n");
		for (Task task : saveManager.getHistory()) {
			System.out.println(task);
		}*/
		
		//Блок удаления всех задач. Результатом выполнения будет
		//пустой файл, и сброс счетчика id.
		/*saveManager.deleteEpics();
		saveManager.deleteTasks();*/
		
		//Проверочный блок, если задачи не были удалены то можно проверить
		//что они были восстановлены с нужными данными.
		System.out.println("\n");
		System.out.println(manager.getTask(1));
		System.out.println(manager.getEpic(2));
		System.out.println(manager.getSubtask(3));
		System.out.println(manager.getEpicSubtasks(2));
		
		//Блок проверки создания новых задач после очищения и без.
		/*Task task2 = new Task("TaskName2", "TaskDescription2");
		manager.addTask(task2);
		int taskId2 = manager.getTasks().get(0).getId();
		
		Epic epic2 = new Epic("EpicName2", "EpicDescription2");
		manager.addEpic(epic2);
		int epicId2 = manager.getEpics().get(0).getId();
		
		Subtask subtask2 = new Subtask("SubtaskName1", "SubDescription1", epicId2);
		manager.addSubtask(subtask2);
		int sub2 = manager.getSubtasks().get(0).getId();
		
		System.out.println("\n");
		System.out.println(manager.getTask(taskId2));
		System.out.println(manager.getEpic(epicId2));
		System.out.println(manager.getSubtask(sub2));*/
	}
}