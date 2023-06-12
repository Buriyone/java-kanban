package main.java;

import main.java.managers.Managers;
import main.java.managers.TaskManager;
import main.java.models.Status;
import main.java.tasks.Task;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.managers.FileBackedTasksManager;

import java.nio.file.Path;
import java.time.LocalDateTime;

public class Main {
	
	public static void main(String[] args) {
		FileBackedTasksManager saveManager = new FileBackedTasksManager(Path.of("src/main/resources/file.csv"));
		
		LocalDateTime time1 = LocalDateTime.now();
		LocalDateTime time2 = time1.plusMinutes(15);
		LocalDateTime time3 = time2.plusMinutes(15);
		
		Task task1 = new Task("name", "description");
		saveManager.addTask(task1);
		
		Epic epic = new Epic("name", "description");
		saveManager.addEpic(epic);
		
		Subtask sub1 = new Subtask("name", "description", time1, epic.getId());
		saveManager.addSubtask(sub1);
		
		Subtask sub2 = new Subtask("name", "description", epic.getId());
		saveManager.addSubtask(sub2);
		
		Subtask sub3 = new Subtask("name", "description", time2, epic.getId());
		saveManager.addSubtask(sub3);
		
		Subtask sub4 = new Subtask("name", "description", time3, epic.getId());
		saveManager.addSubtask(sub4);
		
		
		for (Task task : saveManager.getPrioritizedTasks()) {
			System.out.println(task);
		}
		
		TaskManager testManager = Managers.getDefault();
		
		System.out.println("\n");
		for (Task task : testManager.getPrioritizedTasks()) {
			System.out.println(task);
		}
	}
}