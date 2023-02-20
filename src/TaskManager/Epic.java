package TaskManager;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasksStorage = new ArrayList<>();

    public Epic(String taskName, String descriptionEpic) {
        super(taskName, descriptionEpic);
    }

    protected ArrayList<Subtask> getSubtasksStorage() {
        return subtasksStorage;
    }

    protected void setSubtasksStorage(ArrayList<Subtask> subtasksStorage) {
        this.subtasksStorage = subtasksStorage;
    }
}
