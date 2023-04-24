package main.java.managers;

import java.io.File;
import java.nio.file.Path;

public class Managers {
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return FileBackedTasksManager.loadFromFile(Path.of("src/main/resources/file.csv").toFile());
    }
}
