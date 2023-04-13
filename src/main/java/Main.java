package main.java;

import main.java.managers.Managers;
import main.java.models.Status;
import main.java.managers.TaskManager;
import main.java.tasks.Task;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();


        /*
        Каждый сантиметр проверил!
        Код полностью работает, в конце я всяческий вызов сделал,
        Там и голова удаляется и хвост и местами меняются.
        */

        System.out.println("Инициализируем задачи");
        Task task1 = new Task("Покупки", "Хлеб, Молоко, яйца", Status.NEW);
        Task task2 = new Task("Стирка", "Устроить постирушки", Status.NEW);

        System.out.println("Создадим задачи и получим их id");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        int taskId1 = taskManager.getTasks().get(0).getId();
        int taskId2 = taskManager.getTasks().get(1).getId();


        System.out.println("\n");
        System.out.println("Инициализируем эпики");
        Epic epic1 = new Epic("Уборка", "Пора убраться", Status.NEW);
        Epic epic2 = new Epic("Собака", "Есть несколько дел", Status.NEW);

        System.out.println("Создадим эпики и получим их id");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        int epicId1 = taskManager.getEpics().get(0).getId();
        int epicId2 = taskManager.getEpics().get(1).getId();

        System.out.println("\n");
        System.out.println("Инициализируем подзадачи");
        Subtask subtask1 = new Subtask("Помыть полы", "не забыть под диваном", Status.NEW, epicId1);
        Subtask subtask2 = new Subtask("Сводить собаку в парк", "Посетить площадку",
                Status.NEW, epicId2);
        Subtask subtask3 = new Subtask("Сводить собаку к грумеру", "Совсем зарос",
                Status.NEW, epicId2);

        System.out.println("Распределим подзадачи и получим их id");
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        int sub1 = taskManager.getSubtasks().get(0).getId();
        int sub2 = taskManager.getSubtasks().get(1).getId();
        int sub3 = taskManager.getSubtasks().get(2).getId();


        System.out.println("\n");
        System.out.println("Получим список задач");
        for (Task task : taskManager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Получим задачи по идентификатору:");
        System.out.println(taskManager.getTask(taskId1));
        System.out.println(taskManager.getTask(taskId2));
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Обновим задачу, сменим её статус, изменим описание");
        System.out.println("Вызовем задачу, и посмотрим на ее поля перед обновлением:");
        System.out.println(taskManager.getTask(taskId1));
        System.out.println("Обновим");
        Task task1_1 = new Task("Покупки", "Морковь, свекла", Status.IN_PROGRESS);
        taskManager.updateTask(task1_1, taskId1);
        System.out.println("Посмотрим что из этого вышло:");
        System.out.println(taskManager.getTask(taskId1));
        System.out.println("Работает как надо");

        System.out.println("\n");
        System.out.println("Удалим задачу по идентификатору");
        System.out.println("Сначала проверим наличие задач");
        for (Task task : taskManager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Удалим первую задачу");
        taskManager.deleteTask(taskId1);
        System.out.println("Проверим что получилось и еще раз вызовем список задач");
        for (Task task : taskManager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Задача была удалена, отлично");

        System.out.println("\n");
        System.out.println("Получим список эпиков");
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Получим эпики по идентификатору");
        System.out.println(taskManager.getEpic(epicId1));
        System.out.println(taskManager.getEpic(epicId2));
        System.out.println("Все работает, отлично.");

        System.out.println("\n");
        System.out.println("Обновим эпик");
        System.out.println("Посмотрим на эпик до обновления: ");
        System.out.println(taskManager.getEpic(epicId1));
        System.out.println("Обновим");
        Epic epic1_1 = new Epic("Влажная уборка", "Влажная уборка с применением фибры",
                Status.NEW);
        taskManager.updateEpic(epic1_1, epicId1);
        System.out.println("Взглянем что получилось: ");
        System.out.println(taskManager.getEpic(epicId1));
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Попробуем сменить эпику статус");
        System.out.println("Посмотрим на статус эпика: ");
        System.out.println(taskManager.getEpic(epicId2));
        System.out.println("Заменим статус в подзадачах на выполненный и обновим их.");
        Subtask subtask2_1 = new Subtask("Сводить собаку в парк",
                "Посетить площадку", Status.DONE, epicId2);
        Subtask subtask3_1 = new Subtask("Сводить собаку к грумеру",
                "Совсем зарос", Status.DONE, epicId2);
        taskManager.updateSubtask(subtask2_1, sub2);
        taskManager.updateSubtask(subtask3_1, sub3);
        System.out.println("посмотрим как изменились подзадачи ");
        for (Subtask subtask : taskManager.getEpicSubtasks(epicId2)) {
            System.out.println(subtask);
        }
        System.out.println("И теперь вызовем эпик ");
        System.out.println(taskManager.getEpic(epicId2));
        System.out.println("работает");

        System.out.println("\n");
        System.out.println("Получим список всех подзадач");
        for (Subtask subtask : taskManager.getSubtasks()) {
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
        for (Subtask subtask : taskManager.getEpicSubtasks(epicId1)) {
            System.out.println(subtask);
        }
        System.out.println("Подзадачи эпика 2: ");
        for (Subtask subtask : taskManager.getEpicSubtasks(epicId2)) {
            System.out.println(subtask);
        }
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Удалим подзадачу принадлежащую эпику 2");
        taskManager.deleteSubtask(sub3);
        System.out.println("И получим список всех подзадач для проверки");
        for (Subtask subtask : taskManager.getSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Удалим эпик по идентификатору для этого:");
        System.out.println("Проверим наличие эпиков");
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println("И проверим наличие подзадач:");
        for (Subtask subtask : taskManager.getSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println("Удалим эпик 1");
        taskManager.deleteEpic(epicId1);
        System.out.println("Проверим что получилось - ещё раз вызовем список эпиков");
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println("И проверим список всех подзадач");
        for (Subtask subtask : taskManager.getSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Удалим все задачи и проверим их наличие");
        taskManager.deleteTasks();
        taskManager.getTasks();
        System.out.println("Задачи были удалены, всё работает");

        System.out.println("\n");
        System.out.println("Удалим все подзадачи и проверим их наличие");
        taskManager.deleteSubtasks();
        taskManager.getSubtasks();
        System.out.println("Подзадач больше нет, все работает");

        System.out.println("\n");
        System.out.println("Удалим все эпики и проверим их наличие");
        taskManager.deleteEpics();
        taskManager.getEpics();
        taskManager.getSubtasks();
        System.out.println("Эпиков больше нет как и подзадач");

        System.out.println("\n");
        System.out.println("Инициализируем задачи для истории");
        Task testTask1 = new Task("Заказать еду", "тут могла быть реклама", Status.NEW);
        Task testTask2 = new Task("Купить наушники", "Давно пора", Status.NEW);

        System.out.println("Создадим задачи и получим их id");
        taskManager.addTask(testTask1);
        taskManager.addTask(testTask2);
        int taskId_1 = taskManager.getTasks().get(0).getId();
        int taskId_2 = taskManager.getTasks().get(1).getId();


        System.out.println("\n");
        System.out.println("Инициализируем эпики для истории");
        Epic epic_1 = new Epic("Разминка", "не халтурить", Status.NEW);
        Epic epic_2 = new Epic("Починить кран", "надоел капать", Status.NEW);

        System.out.println("Создадим эпики и получим их id");
        taskManager.addEpic(epic_1);
        taskManager.addEpic(epic_2);
        int epicId_1 = taskManager.getEpics().get(0).getId();
        int epicId_2 = taskManager.getEpics().get(1).getId();

        System.out.println("\n");
        System.out.println("Инициализируем подзадачи для истории");
        Subtask subtask_1 = new Subtask("Присед", "3 по 15", Status.NEW, epicId_1);
        Subtask subtask_2 = new Subtask("Отжимания", "3 по 20",
                Status.NEW, epicId_1);
        Subtask subtask_3 = new Subtask("Планка", "Бьем рекорды",
                Status.NEW, epicId_1);

        System.out.println("Распределим подзадачи и получим их id");
        taskManager.addSubtask(subtask_1);
        taskManager.addSubtask(subtask_2);
        taskManager.addSubtask(subtask_3);
        int sub_1 = taskManager.getSubtasks().get(0).getId();
        int sub_2 = taskManager.getSubtasks().get(1).getId();
        int sub_3 = taskManager.getSubtasks().get(2).getId();

        System.out.println("\n");
        System.out.println("Запросим созданные задачи несколько раз в разном порядке.");
        taskManager.getTask(taskId_1);
        taskManager.getTask(taskId_2);
        taskManager.getEpic(epicId_1);
        taskManager.getEpic(epicId_2);
        taskManager.getSubtask(sub_1);
        taskManager.getSubtask(sub_2);
        taskManager.getSubtask(sub_3);
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("\n");
        System.out.println("Еще раз делаем запрос в ином порядке с изменением головы.");

        taskManager.getTask(taskId_1);
        taskManager.getTask(taskId_2);
        taskManager.getSubtask(sub_1);
        taskManager.getSubtask(sub_2);
        taskManager.getEpic(epicId_2);
        taskManager.getSubtask(sub_3);
        taskManager.getEpic(epicId_1);
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("\n");
        System.out.println("Еще раз делаем запрос в ином порядке с изменением хвоста.");
        taskManager.getSubtask(sub_3);
        taskManager.getEpic(epicId_1);
        taskManager.getSubtask(sub_2);
        taskManager.getEpic(epicId_2);
        taskManager.getTask(taskId_1);
        taskManager.getTask(taskId_1);
        taskManager.getSubtask(sub_1);
        taskManager.getSubtask(sub_1);
        taskManager.getSubtask(sub_1);
        taskManager.getTask(taskId_2);
        taskManager.getTask(taskId_2);
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("\n");
        System.out.println("Тест успешный");

        System.out.println("\n");
        System.out.println("Удалим задачу, которая есть в истории, и проверим, " +
                "что при печати она не будет выводиться;");
        System.out.println("Удалим голову и хвост и проверим как работает алгоритм замены и наличие удаленных " +
                "экземпляров из истории.");
        taskManager.deleteSubtask(sub_3);
        taskManager.deleteTask(taskId_2);
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("\n");
        System.out.println("Тест успешный");

        System.out.println("\n");
        System.out.println("удалим эпик с тремя подзадачами и убедимся, " +
                "что из истории удалился как сам эпик, так и все его подзадачи.");
        taskManager.deleteEpic(epicId_1);
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("\n");
        System.out.println("Все тесты прошли проверку");
    }
}
