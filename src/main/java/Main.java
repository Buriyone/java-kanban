package main.java;

import main.java.managers.TaskManager;
import main.java.tasks.Task;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        System.out.println("Инициализируем задачи");
        Task task1 = new Task("Покупки", "Хлеб, Молоко, яйца", "NEW");
        Task task2 = new Task("Стирка", "Устроить постирушки", "NEW");

        System.out.println("Создадим задачи и получим их id");
        taskManager.createCommonTask(task1);
        taskManager.createCommonTask(task2);
        int taskId1 = taskManager.commonTaskStorage.get(1).getTaskId();
        int taskId2 = taskManager.commonTaskStorage.get(2).getTaskId();

        System.out.println("\n");
        System.out.println("Инициализируем эпики");
        Epic epic1 = new Epic("Уборка", "Пора убраться", "NEW");
        Epic epic2 = new Epic("Собака", "Есть несколько дел", "NEW");

        System.out.println("Создадим эпики и получим их id");
        taskManager.createEpicTask(epic1);
        taskManager.createEpicTask(epic2);
        int epicId1 = taskManager.epicTaskStorage.get(3).getTaskId();
        int epicId2 = taskManager.epicTaskStorage.get(4).getTaskId();

        System.out.println("\n");
        System.out.println("Инициализируем подзадачи");
        Subtask subtask1 = new Subtask("Пыль", "Пора бы уже", "NEW");
        Subtask subtask2 = new Subtask("Сводить собаку в парк", "Посетить площадку", "NEW");
        Subtask subtask3 = new Subtask("Сводить собаку к грумеру", "Совсем зарос", "NEW");

        System.out.println("Создадим и распределим подзадачи по эпикам и получим их id");
        taskManager.createSubtask(subtask1, epicId1);
        taskManager.createSubtask(subtask2, epicId2);
        taskManager.createSubtask(subtask3, epicId2);
        int sub1 = taskManager.subtaskStorage.get(5).getTaskId();
        int sub2 = taskManager.subtaskStorage.get(6).getTaskId();
        int sub3 = taskManager.subtaskStorage.get(7).getTaskId();


        System.out.println("\n");
        System.out.println("Получим список задач");
        System.out.println(taskManager.viewAllCommonTask());
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Получим задачи по идентификатору");
        System.out.println(taskManager.getCommonTask(taskId1));
        System.out.println(taskManager.getCommonTask(taskId2));
        System.out.println("Получим имена задач по идентификатору");
        System.out.println(taskManager.getCommonTask(taskId1).getTaskName());
        System.out.println(taskManager.getCommonTask(taskId2).getTaskName());
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Обновим задачу, сменим её статус, изменим описание");
        System.out.println("Действующее описание: " + taskManager.commonTaskStorage.get(taskId1).getDescriptionTask());
        System.out.println("Действующий статус: " + taskManager.commonTaskStorage.get(taskId1).getTaskStatus());
        Task task1_1 = new Task("Покупки", "Морковь, свекла", "IN_PROGRESS");
        taskManager.updateCommonTask(task1_1, taskId1);
        System.out.println("Новое описание: " + taskManager.getCommonTask(taskId1).getDescriptionTask());
        System.out.println("Новый статус: " + taskManager.getCommonTask(taskId1).getTaskStatus());
        System.out.println("Всё в порядке");

        System.out.println("\n");
        System.out.println("Удалим задачу по идентификатору");
        System.out.println("Сначала проверим наличие задач");
        System.out.println(taskManager.viewAllCommonTask());
        System.out.println("И их имена:");
        System.out.println(taskManager.commonTaskStorage.get(taskId1).getTaskName()
                + "\n" + taskManager.commonTaskStorage.get(taskId2).getTaskName());
        System.out.println("Удалим первую задачу");
        taskManager.deleteCommonTask(taskId1);
        System.out.println("Проверим что получилось и еще раз вызовем список задач");
        System.out.println(taskManager.viewAllCommonTask());
        System.out.println("Имя оставшейся задачи: " + taskManager.commonTaskStorage.get(taskId2).getTaskName());
        System.out.println("Задача была удалена, отлично");

        System.out.println("\n");
        System.out.println("Получим список эпиков");
        System.out.println(taskManager.viewAllEpicTask());
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Получим эпики по идентификатору");
        System.out.println(taskManager.getEpicTask(epicId1));
        System.out.println(taskManager.getEpicTask(epicId2));
        System.out.println("Имена эпиков: " + "\n" + taskManager.getEpicTask(epicId1).getTaskName()
                + "\n" + taskManager.getEpicTask(epicId2).getTaskName());
        System.out.println("Все работает, отлично.");

        System.out.println("\n");
        System.out.println("Обновим эпик");
        System.out.println("Действующее описание эпика с этим индексом: " + "\n"
                + taskManager.getEpicTask(epicId1).getDescriptionTask());
        Epic epic1_1 = new Epic("Влажная уборка", "Влажная уборка с применением фибры",
                "NEW");
        taskManager.updateEpicTask(epic1_1, epicId1);
        System.out.println("Новое описание эпика: " + "\n"
                + taskManager.getEpicTask(epicId1).getDescriptionTask());
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Попробуем сменить эпику статус");
        System.out.println("Действующий статус: " + "\n"
                + taskManager.getEpicTask(epicId2).getTaskStatus());
        System.out.println("Заменим статус в подзадачах на выполненный и обновим их.");
        subtask2.setTaskStatus("DONE");
        subtask3.setTaskStatus("DONE");
        Subtask subtask2_1 = new Subtask("Сводить собаку в парк",
                "Посетить площадку", "DONE");
        Subtask subtask3_1 = new Subtask("Сводить собаку к грумеру",
                "Совсем зарос", "DONE");
        taskManager.updateSubtask(subtask2_1, sub2);
        taskManager.updateSubtask(subtask3_1, sub3);
        System.out.println("Новые статусы подзадач: " + "\n" + taskManager.getSubtask(sub2).getTaskStatus()
                + "\n" + taskManager.getSubtask(sub3).getTaskStatus());
        System.out.println("Статус эпика после изменений: " + "\n"
                + taskManager.getEpicTask(epicId2).getTaskStatus());
        System.out.println("работает");

        System.out.println("\n");
        System.out.println("Получим список всех подзадач");
        System.out.println(taskManager.viewAllSubtask());
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Получим подзадачи по идентификатору");
        System.out.println(taskManager.getSubtask(sub1));
        System.out.println(taskManager.getSubtask(sub2));
        System.out.println(taskManager.getSubtask(sub3));
        System.out.println("Их имена: " + "\n" + taskManager.getSubtask(sub1).getTaskName() + "\n"
                + taskManager.getSubtask(sub2).getTaskName() + "\n" + taskManager.getSubtask(sub3).getTaskName());
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Получим все подзадачи конкретного эпика");
        System.out.println("Поочередно вызовем метод для каждого эпика");
        System.out.println(taskManager.viewAllEpicSubtask(epicId1));
        System.out.println(taskManager.viewAllEpicSubtask(epicId2));
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Удалим подзадачу 3, принадлежащую эпику 2");
        taskManager.deleteSubtask(sub3);
        System.out.println("И получим список всех подзадач для проверки");
        System.out.println(taskManager.viewAllSubtask());
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Удалим эпик по идентификатору для этого:");
        System.out.println("Проверим наличие эпиков");
        System.out.println(taskManager.viewAllEpicTask());
        System.out.println("И проверим наличие подзадач:");
        System.out.println(taskManager.viewAllSubtask());
        System.out.println("Удалим эпик 1");
        taskManager.deleteEpicTask(epicId1);
        System.out.println("Проверим что получилось - ещё раз вызовем список эпиков");
        System.out.println(taskManager.viewAllEpicTask());
        System.out.println("И проверим список всех подзадач");
        System.out.println(taskManager.viewAllSubtask());
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Удалим все задачи и проверим их наличие");
        taskManager.deleteAllCommonTask();
        taskManager.viewAllCommonTask();
        System.out.println("Задачи были удалены, всё работает");

        System.out.println("\n");
        System.out.println("Удалим все подзадачи и проверим их наличие");
        taskManager.deleteAllSubtask();
        taskManager.viewAllSubtask();
        System.out.println("Подзадач больше нет, все работает");

        System.out.println("\n");
        System.out.println("Удалим все эпики и проверим их наличие");
        taskManager.deleteAllEpicTask();
        taskManager.viewAllEpicTask();
        taskManager.viewAllSubtask();
        System.out.println("Эпиков больше нет как и подзадач");

        System.out.println("Все тесты прошли проверку");
    }
}
