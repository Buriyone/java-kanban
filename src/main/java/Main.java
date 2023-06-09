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
		FileBackedTasksManager saveManager = new FileBackedTasksManager(Path.of("src/main/resources/file.csv"));
		
		Task task1 = new Task("name", "description");
		saveManager.addTask(task1);
		
		Epic epic = new Epic("name", "description");
		saveManager.addEpic(epic);
		
		Subtask sub1 = new Subtask("name", "description", epic.getId(),
				saveManager.getFreeTime());
		saveManager.addSubtask(sub1);
		
		Subtask sub2 = new Subtask("name", "description", epic.getId());
		saveManager.addSubtask(sub2);
		
		Subtask sub3 = new Subtask("name", "description", epic.getId(), saveManager.getFreeTime());
		saveManager.addSubtask(sub3);
		
		TaskManager managerTest = Managers.getDefault();
		
		for (Task task : managerTest.getPrioritizedTasks()) {
			System.out.println(task);
		}
	}
}