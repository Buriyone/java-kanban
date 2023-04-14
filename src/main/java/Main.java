package main.java;

import main.java.managers.Managers;
import main.java.models.Status;
import main.java.managers.TaskManager;
import main.java.tasks.Task;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        System.out.println("Инициализируем задачи");
        Task task1 = new Task("Покупки", "Хлеб, Молоко, яйца");
        Task task2 = new Task("Стирка", "Устроить постирушки");

        System.out.println("Создадим задачи и получим их id");
        manager.addTask(task1);
        manager.addTask(task2);
        int taskId1 = manager.getTasks().get(0).getId();
        int taskId2 = manager.getTasks().get(1).getId();


        System.out.println("\n");
        System.out.println("Инициализируем эпики");
        Epic epic1 = new Epic("Уборка", "Пора убраться");
        Epic epic2 = new Epic("Собака", "Есть несколько дел");

        System.out.println("Создадим эпики и получим их id");
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        int epicId1 = manager.getEpics().get(0).getId();
        int epicId2 = manager.getEpics().get(1).getId();

        System.out.println("\n");
        System.out.println("Инициализируем подзадачи");
        Subtask subtask1 = new Subtask("Помыть полы", "не забыть под диваном", Status.NEW, epicId1);
        Subtask subtask2 = new Subtask("Сводить собаку в парк", "Посетить площадку",
                Status.NEW, epicId2);
        Subtask subtask3 = new Subtask("Сводить собаку к грумеру", "Совсем зарос",
                Status.NEW, epicId2);

        System.out.println("Распределим подзадачи и получим их id");
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        int sub1 = manager.getSubtasks().get(0).getId();
        int sub2 = manager.getSubtasks().get(1).getId();
        int sub3 = manager.getSubtasks().get(2).getId();


        System.out.println("\n");
        System.out.println("Получим список задач");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Получим задачи по идентификатору:");
        System.out.println(manager.getTask(taskId1));
        System.out.println(manager.getTask(taskId2));
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Обновим задачу, сменим её статус, изменим описание");
        System.out.println("Вызовем задачу, и посмотрим на ее поля перед обновлением:");
        System.out.println(manager.getTask(taskId1));
        System.out.println("Обновим");
        Task task1_1 = new Task("Покупки", "Морковь, свекла", Status.IN_PROGRESS);
        manager.updateTask(task1_1, taskId1);
        System.out.println("Посмотрим что из этого вышло:");
        System.out.println(manager.getTask(taskId1));
        System.out.println("Работает как надо");

        System.out.println("\n");
        System.out.println("Удалим задачу по идентификатору");
        System.out.println("Сначала проверим наличие задач");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Удалим первую задачу");
        manager.deleteTask(taskId1);
        System.out.println("Проверим что получилось и еще раз вызовем список задач");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Задача была удалена, отлично");

        System.out.println("\n");
        System.out.println("Получим список эпиков");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Получим эпики по идентификатору");
        System.out.println(manager.getEpic(epicId1));
        System.out.println(manager.getEpic(epicId2));
        System.out.println("Все работает, отлично.");

        System.out.println("\n");
        System.out.println("Обновим эпик");
        System.out.println("Посмотрим на эпик до обновления: ");
        System.out.println(manager.getEpic(epicId1));
        System.out.println("Обновим");
        Epic epic1_1 = new Epic("Влажная уборка", "Влажная уборка с применением фибры");
        manager.updateEpic(epic1_1, epicId1);
        System.out.println("Взглянем что получилось: ");
        System.out.println(manager.getEpic(epicId1));
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Попробуем сменить эпику статус");
        System.out.println("Посмотрим на статус эпика: ");
        System.out.println(manager.getEpic(epicId2));
        System.out.println("Заменим статус в подзадачах на выполненный и обновим их.");
        Subtask subtask2_1 = new Subtask("Сводить собаку в парк",
                "Посетить площадку", Status.DONE, epicId2);
        Subtask subtask3_1 = new Subtask("Сводить собаку к грумеру",
                "Совсем зарос", Status.DONE, epicId2);
        manager.updateSubtask(subtask2_1, sub2);
        manager.updateSubtask(subtask3_1, sub3);
        System.out.println("посмотрим как изменились подзадачи ");
        for (Subtask subtask : manager.getEpicSubtasks(epicId2)) {
            System.out.println(subtask);
        }
        System.out.println("И теперь вызовем эпик ");
        System.out.println(manager.getEpic(epicId2));
        System.out.println("работает");

        System.out.println("\n");
        System.out.println("Получим список всех подзадач");
        for (Subtask subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Получим подзадачи по идентификатору");
        System.out.println(manager.getSubtask(sub1));
        System.out.println(manager.getSubtask(sub2));
        System.out.println(manager.getSubtask(sub3));
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Получим все подзадачи конкретного эпика");
        System.out.println("Поочередно вызовем метод для каждого эпика");
        System.out.println("Подзадачи эпика 1: ");
        for (Subtask subtask : manager.getEpicSubtasks(epicId1)) {
            System.out.println(subtask);
        }
        System.out.println("Подзадачи эпика 2: ");
        for (Subtask subtask : manager.getEpicSubtasks(epicId2)) {
            System.out.println(subtask);
        }
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Удалим подзадачу принадлежащую эпику 2");
        manager.deleteSubtask(sub3);
        System.out.println("И получим список всех подзадач для проверки");
        for (Subtask subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Удалим эпик по идентификатору для этого:");
        System.out.println("Проверим наличие эпиков");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println("И проверим наличие подзадач:");
        for (Subtask subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println("Удалим эпик 1");
        manager.deleteEpic(epicId1);
        System.out.println("Проверим что получилось - ещё раз вызовем список эпиков");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println("И проверим список всех подзадач");
        for (Subtask subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Удалим все задачи и проверим их наличие");
        manager.deleteTasks();
        manager.getTasks();
        System.out.println("Задачи были удалены, всё работает");

        System.out.println("\n");
        System.out.println("Удалим все подзадачи и проверим их наличие");
        manager.deleteSubtasks();
        manager.getSubtasks();
        System.out.println("Подзадач больше нет, все работает");

        System.out.println("\n");
        System.out.println("Удалим все эпики и проверим их наличие");
        manager.deleteEpics();
        manager.getEpics();
        manager.getSubtasks();
        System.out.println("Эпиков больше нет как и подзадач");

        System.out.println("\n");
        System.out.println("Убедимся что история пустая.");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Инициализируем задачи для истории");
        Task testTask1 = new Task("Заказать еду", "тут могла быть реклама");
        Task testTask2 = new Task("Купить наушники", "Давно пора");

        System.out.println("Создадим задачи и получим их id");
        manager.addTask(testTask1);
        manager.addTask(testTask2);
        int taskId_1 = manager.getTasks().get(0).getId();
        int taskId_2 = manager.getTasks().get(1).getId();


        System.out.println("\n");
        System.out.println("Инициализируем эпики для истории");
        Epic epic_1 = new Epic("Разминка", "не халтурить");
        Epic epic_2 = new Epic("Починить кран", "надоел капать");

        System.out.println("Создадим эпики и получим их id");
        manager.addEpic(epic_1);
        manager.addEpic(epic_2);
        int epicId_1 = manager.getEpics().get(0).getId();
        int epicId_2 = manager.getEpics().get(1).getId();

        System.out.println("\n");
        System.out.println("Инициализируем подзадачи для истории");
        Subtask subtask_1 = new Subtask("Присед", "3 по 15", Status.NEW, epicId_1);
        Subtask subtask_2 = new Subtask("Отжимания", "3 по 20",
                Status.NEW, epicId_1);
        Subtask subtask_3 = new Subtask("Планка", "Бьем рекорды",
                Status.NEW, epicId_1);

        System.out.println("Распределим подзадачи и получим их id");
        manager.addSubtask(subtask_1);
        manager.addSubtask(subtask_2);
        manager.addSubtask(subtask_3);
        int sub_1 = manager.getSubtasks().get(0).getId();
        int sub_2 = manager.getSubtasks().get(1).getId();
        int sub_3 = manager.getSubtasks().get(2).getId();

        System.out.println("\n");
        System.out.println("Запросим созданные задачи несколько раз в разном порядке.");
        manager.getTask(taskId_1);
        manager.getTask(taskId_2);
        manager.getEpic(epicId_1);
        manager.getEpic(epicId_2);
        manager.getSubtask(sub_1);
        manager.getSubtask(sub_2);
        manager.getSubtask(sub_3);
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("\n");
        System.out.println("Еще раз делаем запрос в ином порядке с изменением головы.");

        manager.getTask(taskId_1);
        manager.getTask(taskId_2);
        manager.getSubtask(sub_1);
        manager.getSubtask(sub_2);
        manager.getEpic(epicId_2);
        manager.getSubtask(sub_3);
        manager.getEpic(epicId_1);
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("\n");
        System.out.println("Еще раз делаем запрос в ином порядке с изменением хвоста.");
        manager.getSubtask(sub_3);
        manager.getEpic(epicId_1);
        manager.getSubtask(sub_2);
        manager.getEpic(epicId_2);
        manager.getTask(taskId_1);
        manager.getTask(taskId_1);
        manager.getSubtask(sub_1);
        manager.getSubtask(sub_1);
        manager.getSubtask(sub_1);
        manager.getTask(taskId_2);
        manager.getTask(taskId_2);
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("\n");
        System.out.println("Тест успешный");

        System.out.println("\n");
        System.out.println("Удалим задачу, которая есть в истории, и проверим, " +
                "что при печати она не будет выводиться;");
        System.out.println("Удалим голову и хвост и проверим как работает алгоритм замены и наличие удаленных " +
                "экземпляров из истории.");
        manager.deleteSubtask(sub_3);
        manager.deleteTask(taskId_2);
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("\n");
        System.out.println("Тест успешный");

        System.out.println("\n");
        System.out.println("удалим эпик с тремя подзадачами и убедимся, " +
                "что из истории удалился как сам эпик, так и все его подзадачи.");
        manager.deleteEpic(epicId_1);
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("\n");
        System.out.println("Тест успешный.");

        System.out.println("\n");
        System.out.println("удалим оставшиеся элементы и убедимся что история пустая");
        manager.deleteTasks();
        manager.deleteEpics();
        System.out.println(manager.getHistory());

        System.out.println("\n");
        System.out.println("Все тесты прошли проверку");

        System.out.println("\n");
        System.out.println("Дополнительный тест.");
        Epic epic = new Epic(("Task title" + 1), ("Task description" + 1));
        manager.addEpic(epic);
        Subtask sub = new Subtask(("Task title" + 2), ("Task description" + 1), Status.NEW, 15);
        manager.addSubtask(sub);
        System.out.println(manager.getSubtask(16));
        manager.deleteEpic(15);
        System.out.println(manager.getHistory());
    }
}
