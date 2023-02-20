# java-kanban
## Это заготовка backand проекта для трекера задач.

Трекер позволяет создавать ***Обычные задачи***, давать им описание и присваивать статус выполнения этой задачи.

Реализована возможность создавать ***Большие задачи*** и их ***Подзадачи***.
У всех задач имеется свой уникальный ***id*** по которому при необходимости их можно найти.

Расскажу подробнее об основных **методах** примененных для решения этой задачи.

Основной класс работы трекера: ***TaskManager***

Он включает в себя ***20 методов*** разной степени доступа.

Большинство методов публичные и не несут опасности для логики исполнения кода.

Есть методы закрытые от внешнего доступа, такие как: 

```java
 private void updateEpicTask(Epic epic) {
        epic.setTaskStatus(epicStatusDealer(epic));
        epicTaskStorage.put(epic.getTaskId(), epic);
    }
```

Данный метод вызывается исключительно при внесении ***изменений*** в ***подзадачи***, 

логика его исполнения заключается в вызове метода ***epicStatusDealer()***,

и передачи ему обьекта класса ***Epic***. Сам ***Epic*** приходит из методов изменящищих ***подзадачи***.

Метод получает перерассчитанный ***статус*** объекта класса ***Epic*** исходя из условий данных в ***ТЗ***, 

затем обновляет его в хранилище ***epicTaskStorage***.

К приватным методам относится и метод ***epicStatusDealer***.

```java
private String epicStatusDealer(Epic epic) {
        String status;

        if (!epic.getSubtasksStorage().isEmpty()) {
            ArrayList<String> itemStatus = new ArrayList<>();

            for (Subtask subtask : epic.getSubtasksStorage()) {
                itemStatus.add(subtask.getTaskStatus());
            }

            if(!itemStatus.contains("IN_PROGRESS") && !itemStatus.contains("DONE")) {
                status = "NEW";
            } else if (!itemStatus.contains("IN_PROGRESS") && !itemStatus.contains("NEW")) {
                status = "DONE";
            } else {
                status = "IN_PROGRESS";
            }
        } else {
            status = "NEW";
        }
        return status;
    }
```

В его задачи входит ***перерасчет статуса*** объекта класса ***Epic*** - на основе его содержания и условий данных в ***ТЗ***.

Далее на очереди метод ***удаления Большой задачи***.
```java
public void deleteEpicTask(int theEpic) {
        if (epicTaskStorage.containsKey(theEpic)) {
            if (!epicTaskStorage.get(theEpic).getSubtasksStorage().isEmpty()) {

                for (Subtask subtaskByEpic : epicTaskStorage.get(theEpic).getSubtasksStorage()) {
                    ArrayList<Integer> subIdForDeleted = new ArrayList<>();

                    for (Integer subtaskID : subtaskStorage.keySet()) {
                        if (subtaskStorage.get(subtaskID).equals(subtaskByEpic)
                                && subtaskStorage.get(subtaskID).hashCode()
                                == subtaskByEpic.hashCode()) {
                            subIdForDeleted .add(subtaskID);
                        }
                    }

                    for (Integer subId: subIdForDeleted) {
                        subtaskStorage.remove(subId);
                    }
                }
                epicTaskStorage.remove(theEpic);
            } else {
                epicTaskStorage.remove(theEpic);
            }
        }
    }
```
Я посчитал, что удалить сам Epic из хранилища ***недостаточно***, 

необходимо также ***удалить*** и все ***подзадачи*** имеющие к нему отношение.

По этой причине метод сравнивает все ***подзадачи***, хранящиеся внутри ***Большой задачи***, 

с ***подзадачей*** из своего хранилища ***subtaskStorage***,

затем производит их поочередную ***очистку***, после чего удаляет и сам ***Epic***.

Метод ***updateSubtask()*** позволяет ***сравнить*** переданную ***подзадачу*** с уже существующей, 

и при полном ***равенстве*** производит его перезапись как в хранилище, так и в листе хранения, 

имеющего отношение к этой подзадаче - задаче класса Epic.
```java
public void updateSubtask(Subtask subtask) {
        for (Integer theEpic : epicTaskStorage.keySet()) {
            for(int i = 0; i < epicTaskStorage.get(theEpic).getSubtasksStorage().size(); i++) {
                if (epicTaskStorage.get(theEpic).getSubtasksStorage().get(i).equals(subtask)
                        && epicTaskStorage.get(theEpic).getSubtasksStorage().get(i).hashCode()
                        == subtask.hashCode()) {
                    epicTaskStorage.get(theEpic).getSubtasksStorage().set(i, subtask);

                    subtaskStorage.put(subtask.getTaskId(), subtask);

                    updateEpicTask(epicTaskStorage.get(theEpic));
                }
            }
        }
    }
```
Метод ***deleteSubtask()*** позволяет ***удалить*** необходимую ***подзадачу***. 

Для этого ему требуется ***Id*** в качестве ***аргумента***, 

после чего он ***самостоятельно*** находит все места хранения передаваемой ***подзадачи***, 

производит ***сверку*** и ***перезаписывает***, дополнительно вызывая метод ***updateEpicTask()*** 

для внесения изменений в имеющий отноешние к данной подзадаче основной задаче класса Epic.
```java
public void deleteSubtask(int subtaskId) {
        for (Integer theEpic : epicTaskStorage.keySet()) {
            for(int i = 0; i < epicTaskStorage.get(theEpic).getSubtasksStorage().size(); i++) {
                if (epicTaskStorage.get(theEpic).getSubtasksStorage().get(i).equals(subtaskStorage.get(subtaskId))
                && epicTaskStorage.get(theEpic).getSubtasksStorage().get(i).hashCode()
                        == subtaskStorage.get(subtaskId).hashCode()) {
                    epicTaskStorage.get(theEpic).getSubtasksStorage()
                            .remove(epicTaskStorage.get(theEpic).getSubtasksStorage().get(i));

                    subtaskStorage.remove(subtaskId);

                    updateEpicTask(epicTaskStorage.get(theEpic));
                }
            }
        }
    }
```

При применении метода ***equals()*** я использовал только имя задачи и его ***Id***.

Я посчитал что этого более чем достаточно для точного определения.
```java
@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return Objects.equals(taskName, task.taskName) && (taskId == task.taskId);
    }
```
Метод ***hashCode()*** также слегка накручен дополнительным значением - это позволит получить более точный Хэшкод объекта.
```java
@Override
    public int hashCode() {
        int hash = 17;
        if (taskName != null) {
            hash = hash + taskName.hashCode() + taskId * 3;
        }
        hash = hash * 31;
        return hash;
    }
```

## Немного о тестировании.

Все ***методы*** добавления задач:
1. createCommonTask();
2. createEpicTask();
3. createSubtask();

- возвращают свой id при создании. Это сделано для удобства тестирования.

Несомненно, система тестирования отличается от той, что я оставил в классе Main.

Но если потребуется мой тест, то я оставлю тут часть кода, проводившую тесты:

```java
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
```
