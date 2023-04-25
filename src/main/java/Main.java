package main.java;

import main.java.managers.Managers;
import main.java.models.Status;
import main.java.managers.TaskManager;
import main.java.tasks.Task;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.managers.FileBackedTasksManager;

import java.nio.file.Path;

import static main.java.managers.FileBackedTasksManager.loadFromFile;

public class Main {
	
	public static void main(String[] args) {
		System.out.println("Тест нового менеджера.");
		TaskManager manager = Managers.getDefault();

        /*FileBackedTasksManager saveManager = new FileBackedTasksManager(Path.of("src/main/resources/file.csv"));
        
        Task task1 = new Task("TaskName1", "TaskDescription1");
        saveManager.addTask(task1);
        int taskId1 = saveManager.getTasks().get(0).getId();

        Epic epic1 = new Epic("EpicName1", "EpicDescription1");
        saveManager.addEpic(epic1);
        int epicId1 = saveManager.getEpics().get(0).getId();

        Subtask subtask1 = new Subtask("SubtaskName1", "SubDescription1", Status.NEW, epicId1);
        saveManager.addSubtask(subtask1);
        int sub1 = saveManager.getSubtasks().get(0).getId();
        Subtask subtask1_1 = new Subtask("SubtaskName1", "SubDescription1", Status.DONE, epicId1);
        saveManager.updateSubtask(subtask1_1, sub1);

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
		
		System.out.println(manager.getTask(1));
		System.out.println(manager.getEpic(2));
		System.out.println(manager.getSubtask(3));
		System.out.println(manager.getEpicSubtasks(2));
		
		System.out.println("\n");
		for (Task task : manager.getHistory()) {
			System.out.println(task);
		}
	}
}
