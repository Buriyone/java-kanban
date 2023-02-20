import TaskManager.TaskManager;
import TaskManager.Task;
import TaskManager.Epic;
import TaskManager.Subtask;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Покупки", "Хлеб, Молоко, яйца");
        Task task2 = new Task("Стирка", "Устроить постирушки");
        Epic epic1 = new Epic("Уборка", "Пора убраться");
        Epic epic2 = new Epic("Собака", "Есть несколько дел");
        Subtask subtask1 = new Subtask("Пыль", "Пора бы уже");
        Subtask subtask2 = new Subtask("Сводить собаку в парк", "Посетить площадку");
        Subtask subtask3 = new Subtask("Сводить собаку к грумеру", "Совсем зарос");


        System.out.println("Создадим задачи");
        int taskId1 = taskManager.createCommonTask(task1);
        int taskId2 = taskManager.createCommonTask(task2);

        System.out.println("Создадим эпики");
        int epicId1 = taskManager.createEpicTask(epic1);
        int epicId2 = taskManager.createEpicTask(epic2);

        System.out.println("Создадим и распределим подзадачи по эпикам");
        int sub1 = taskManager.createSubtask(subtask1, epicId1);
        int sub2 = taskManager.createSubtask(subtask2, epicId2);
        int sub3 = taskManager.createSubtask(subtask3, epicId2);

        System.out.println("\n");
        System.out.println("Получим список задач");
        System.out.println(taskManager.viewAllCommonTask());
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Получим задачи по идентификатору");
        System.out.println(taskManager.getCommonTask(taskId1));
        System.out.println(taskManager.getCommonTask(taskId2));
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Обновим задачу и сменим её статус");
        System.out.println("Действующий статус: " + task1.getTaskStatus());
        task1.setTaskStatus("IN_PROGRESS");
        taskManager.updateCommonTask(task1);
        System.out.println("Новый статус: " + task1.getTaskStatus());
        System.out.println("Посмотрим, не изменилось ли чего");
        System.out.println(taskManager.getCommonTask(taskId1));
        System.out.println("Всё в порядке");

        System.out.println("\n");
        System.out.println("Удалим задачу по идентификатору");
        taskManager.deleteCommonTask(taskId1);
        System.out.println("Проверим что получилось и еще раз вызовем список задач");
        System.out.println(taskManager.viewAllCommonTask());
        System.out.println("Задача была удалена, отлично");

        System.out.println("\n");
        System.out.println("Получим список эпиков");
        System.out.println(taskManager.viewAllEpicTask());
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Получим эпики по идентификатору");
        System.out.println(taskManager.getEpicTask(epicId1));
        System.out.println(taskManager.getEpicTask(epicId2));
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Попробуем обновить эпик и сменить ему статус");
        System.out.println("Действующий статус: " + epic2.getTaskStatus());
        System.out.println("Заменим статус в подзадачах на выполненный и обновим их");
        subtask2.setTaskStatus("DONE");
        subtask3.setTaskStatus("DONE");
        taskManager.updateSubtask(subtask2);
        taskManager.updateSubtask(subtask3);
        System.out.println("Новый статус: " + epic2.getTaskStatus());
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
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Обновим все подзадачи на выполненный");
        subtask1.setTaskStatus("DONE");
        subtask2.setTaskStatus("DONE");
        subtask3.setTaskStatus("DONE");
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        taskManager.updateSubtask(subtask3);
        System.out.println("И проверим  как изменился статус эпиков");
        System.out.println(epic1.getTaskStatus());
        System.out.println(epic2.getTaskStatus());
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Получим все подзадачи конкретного эпика");
        System.out.println("Поочередно вызовем метод для каждого эпика");
        System.out.println(taskManager.viewAllEpicSubtask(epicId1));
        System.out.println(taskManager.viewAllEpicSubtask(epicId2));
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Получим список всех подзадач");
        System.out.println(taskManager.viewAllSubtask());
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Удалим одну подзадачу");
        taskManager.deleteSubtask(sub3);
        System.out.println("И получим список всех подзадач для проверки");
        System.out.println(taskManager.viewAllSubtask());
        System.out.println("Работает");

        System.out.println("\n");
        System.out.println("Удалим эпик по идентификатору");
        taskManager.deleteEpicTask(epicId2);
        System.out.println("Проверим что получилось - ещё раз вызовем список эпиков");
        System.out.println("И проверим список всех подзадач");
        System.out.println(taskManager.viewAllEpicTask());
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
