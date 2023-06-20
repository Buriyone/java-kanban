package main.java.managers;

import java.nio.file.Path;

public class Managers {
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return HttpTaskManager.loadFromServer("http://localhost:8078/");
    }
    
    public static TaskManager getReserved() {
        return FileBackedTasksManager.loadFromFile(Path.of("src/main/resources/file.csv").toFile());
    }
}
