package main.java;

import main.java.managers.HistoryManager;
import main.java.managers.Managers;
import main.java.managers.Status;
import main.java.managers.TaskManager;
import main.java.tasks.Task;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;

public class Main {

    public static void main(String[] args) {
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault(historyManager);


        System.out.println("Инициализируем задачи");
        Task task1 = new Task("Покупки", "Хлеб, Молоко, яйца", Status.NEW);
        Task task2 = new Task("Стирка", "Устроить постирушки", Status.NEW);

        System.out.println("Создадим задачи и получим их id");
        taskManager.addCommonTask(task1);
        taskManager.addCommonTask(task2);
        int taskId1 = taskManager.getAllCommonTask().get(0).getTaskId();
        int taskId2 = taskManager.getAllCommonTask().get(1).getTaskId();


        System.out.println("\n");
        System.out.println("Инициализируем эпики");
        Epic epic1 = new Epic("Уборка", "Пора убраться", Status.NEW);
        Epic epic2 = new Epic("Собака", "Есть несколько дел", Status.NEW);

        System.out.println("Создадим эпики и получим их id");
        taskManager.addEpicTask(epic1);
        taskManager.addEpicTask(epic2);
        int epicId1 = taskManager.getAllEpicTask().get(0).getTaskId();
        int epicId2 = taskManager.getAllEpicTask().get(1).getTaskId();

        System.out.println("\n");
        System.out.println("Инициализируем подзадачи");
        Subtask subtask1 = new Subtask("Пыль", "Пора бы уже", Status.NEW, epicId1);
        Subtask subtask2 = new Subtask("Сводить собаку в парк", "Посетить площадку",
                Status.NEW, epicId2);
        Subtask subtask3 = new Subtask("Сводить собаку к грумеру", "Совсем зарос",
                Status.NEW, epicId2);

        System.out.println("Распределим подзадачи и получим их id");
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        int sub1 = taskManager.getAllSubtask().get(0).getTaskId();
        int sub2 = taskManager.getAllSubtask().get(1).getTaskId();
        int sub3 = taskManager.getAllSubtask().get(2).getTaskId();


        System.out.println("\n");
        System.out.println("Получим список задач");
        for (Task task : taskManager.getAllCommonTask()) {
            System.out.println(task);
        }
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Получим задачи по идентификатору:");
        System.out.println(taskManager.getCommonTask(taskId1));
        System.out.println(taskManager.getCommonTask(taskId2));
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Обновим задачу, сменим её статус, изменим описание");
        System.out.println("Вызовем задачу, и посмотрим на ее поля перед обновлением:");
        System.out.println(taskManager.getCommonTask(taskId1));
        System.out.println("Обновим");
        Task task1_1 = new Task("Покупки", "Морковь, свекла", Status.IN_PROGRESS);
        taskManager.updateCommonTask(task1_1, taskId1);
        System.out.println("Посмотрим что из этого вышло:");
        System.out.println(taskManager.getCommonTask(taskId1));
        System.out.println("Работает как надо");

        System.out.println("\n");
        System.out.println("Удалим задачу по идентификатору");
        System.out.println("Сначала проверим наличие задач");
        for (Task task : taskManager.getAllCommonTask()) {
            System.out.println(task);
        }
        System.out.println("Удалим первую задачу");
        taskManager.deleteCommonTask(taskId1);
        System.out.println("Проверим что получилось и еще раз вызовем список задач");
        for (Task task : taskManager.getAllCommonTask()) {
            System.out.println(task);
        }
        System.out.println("Задача была удалена, отлично");

        System.out.println("\n");
        System.out.println("Получим список эпиков");
        for (Epic epic : taskManager.getAllEpicTask()) {
            System.out.println(epic);
        }
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Получим эпики по идентификатору");
        System.out.println(taskManager.getEpicTask(epicId1));
        System.out.println(taskManager.getEpicTask(epicId2));
        System.out.println("Все работает, отлично.");

        System.out.println("\n");
        System.out.println("Обновим эпик");
        System.out.println("Посмотрим на эпик до обновления: ");
        System.out.println(taskManager.getEpicTask(epicId1));
        System.out.println("Обновим");
        Epic epic1_1 = new Epic("Влажная уборка", "Влажная уборка с применением фибры",
                Status.NEW);
        taskManager.updateEpicTask(epic1_1, epicId1);
        System.out.println("Взглянем что получилось: ");
        System.out.println(taskManager.getEpicTask(epicId1));
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Попробуем сменить эпику статус");
        System.out.println("Посмотрим на статус эпика: ");
        System.out.println(taskManager.getEpicTask(epicId2));
        System.out.println("Заменим статус в подзадачах на выполненный и обновим их.");
        Subtask subtask2_1 = new Subtask("Сводить собаку в парк",
                "Посетить площадку", Status.DONE, epicId2);
        Subtask subtask3_1 = new Subtask("Сводить собаку к грумеру",
                "Совсем зарос", Status.DONE, epicId2);
        taskManager.updateSubtask(subtask2_1, sub2);
        taskManager.updateSubtask(subtask3_1, sub3);
        System.out.println("посмотрим как изменились подзадачи ");
        //System.out.println(taskManager.getAllEpicSubtask(epicId2));
        for (Subtask subtask : taskManager.getAllEpicSubtask(epicId2)) {
            System.out.println(subtask);
        }
        System.out.println("И теперь вызовем эпик ");
        System.out.println(taskManager.getEpicTask(epicId2));
        System.out.println("работает");

        System.out.println("\n");
        System.out.println("Получим список всех подзадач");
        //System.out.println(taskManager.getAllSubtask());
        for (Subtask subtask : taskManager.getAllSubtask()) {
            System.out.println(subtask);
        }
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Получим подзадачи по идентификатору");
        System.out.println(taskManager.getSubtask(sub1));
        System.out.println(taskManager.getSubtask(sub2));
        System.out.println(taskManager.getSubtask(sub3));
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Получим все подзадачи конкретного эпика");
        System.out.println("Поочередно вызовем метод для каждого эпика");
        System.out.println("Подзадачи эпика 1: ");
        //System.out.println(taskManager.getAllEpicSubtask(epicId1));
        for (Subtask subtask : taskManager.getAllEpicSubtask(epicId1)) {
            System.out.println(subtask);
        }
        System.out.println("Подзадачи эпика 2: ");
       // System.out.println(taskManager.getAllEpicSubtask(epicId2));
        for (Subtask subtask : taskManager.getAllEpicSubtask(epicId2)) {
            System.out.println(subtask);
        }
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Удалим подзадачу принадлежащую эпику 2");
        taskManager.deleteSubtask(sub3);
        System.out.println("И получим список всех подзадач для проверки");
        //System.out.println(taskManager.getAllSubtask());
        for (Subtask subtask : taskManager.getAllSubtask()) {
            System.out.println(subtask);
        }
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Удалим эпик по идентификатору для этого:");
        System.out.println("Проверим наличие эпиков");
        //System.out.println(taskManager.getAllEpicTask());
        for (Epic epic : taskManager.getAllEpicTask()) {
            System.out.println(epic);
        }
        System.out.println("И проверим наличие подзадач:");
        //System.out.println(taskManager.getAllSubtask());
        for (Subtask subtask : taskManager.getAllSubtask()) {
            System.out.println(subtask);
        }
        System.out.println("Удалим эпик 1");
        taskManager.deleteEpicTask(epicId1);
        System.out.println("Проверим что получилось - ещё раз вызовем список эпиков");
        //System.out.println(taskManager.getAllEpicTask());
        for (Epic epic : taskManager.getAllEpicTask()) {
            System.out.println(epic);
        }
        System.out.println("И проверим список всех подзадач");
        //System.out.println(taskManager.getAllSubtask());
        for (Subtask subtask : taskManager.getAllSubtask()) {
            System.out.println(subtask);
        }
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Удалим все задачи и проверим их наличие");
        taskManager.deleteAllCommonTask();
        taskManager.getAllCommonTask();
        System.out.println("Задачи были удалены, всё работает");

        System.out.println("\n");
        System.out.println("Удалим все подзадачи и проверим их наличие");
        taskManager.deleteAllSubtask();
        taskManager.getAllSubtask();
        System.out.println("Подзадач больше нет, все работает");

        System.out.println("\n");
        System.out.println("Удалим все эпики и проверим их наличие");
        taskManager.deleteAllEpicTask();
        taskManager.getAllEpicTask();
        taskManager.getAllSubtask();
        System.out.println("Эпиков больше нет как и подзадач");

        System.out.println("\n");
        System.out.println("Тестирование решения истории задач.");
        System.out.println("Воспроизведем пользовательский сценарий:");
        System.out.println("Создадим несколько задач разного типа.");

        Task testCommonTask = new Task("Задача", "Обычная задача", Status.NEW);
        taskManager.addCommonTask(testCommonTask);
        int idTestCommonTask = taskManager.getAllCommonTask().get(0).getTaskId();

        Epic testEpicTask = new Epic("Эпик", "Тестовый эпик", Status.NEW);
        taskManager.addEpicTask(testEpicTask);
        int idTestEpicTask = taskManager.getAllEpicTask().get(0).getTaskId();

        Subtask testSubtask = new Subtask("Подзадача", "Тестовая подзадача",
                Status.NEW, idTestEpicTask);
        taskManager.addSubtask(testSubtask);
        int idTestSubtask = taskManager.getAllSubtask().get(0).getTaskId();

        System.out.println("Поочередно вызовем каждый метод и распечатаем историю.\n"
                + "Если последние 3 объекта в списке будут тестовые, расположенные в порядке: "
                + "Задача; Эпик; Подзадача - значит код работает исправно.\n");
        taskManager.getCommonTask(idTestCommonTask);
        taskManager.getEpicTask(idTestEpicTask);
        taskManager.getSubtask(idTestSubtask);
        for (Task testTask : taskManager.getHistory()) {
            System.out.println(testTask);
        }
        System.out.println("\n");
        System.out.println("Теперь проверим как работает ограничение размера истории."
                + " Для этого 10 раз вызовем Задачу.\n"
                + "Если история будет состоять только из задач и их количество не превысит значение 10 "
                + "- значит все работает как надо.\n");
        for (int i = 0; i < 10; i++) {
            taskManager.getCommonTask(idTestCommonTask);
        }
        for (Task testTask : taskManager.getHistory()) {
            System.out.println(testTask);
        }

        System.out.println("\n");
        System.out.println("Все работает как надо, количество задач в истории ровно "
                + taskManager.getHistory().size() + ".");

        System.out.println("\n");
        System.out.println("Все тесты прошли проверку");
    }
}
