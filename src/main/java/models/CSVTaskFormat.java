package main.java.models;

import main.java.managers.HistoryManager;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormat {
    private final static String firstLine = "id,type,name,status,description,epic";

    public static String getFirstLine() {
        return firstLine;
    }
    public static String historyToString(HistoryManager manager) {
        StringBuilder str = new StringBuilder();

        for (Task task : manager.getHistory()) {
            if (str.length() == 0) {
                str.append(task.getId());
            } else {
                str.append(",");
                str.append(task.getId());
            }
        }
        return str.toString();
    }

    public static Task fromString(String value) {
        if (!value.equals(firstLine) && value.length() != 0) {
            String[] strSpl = value.split(",");
            Status status;

            if (strSpl[3].equals(Status.NEW.toString())) {
                status = Status.NEW;
            } else if (strSpl[3].equals(Status.IN_PROGRESS.toString())) {
                status = Status.IN_PROGRESS;
            } else {
                status= Status.DONE;
            }

            if (strSpl[1].equals(Type.TASK.toString())) {
                Task task = new Task(strSpl[2], strSpl[4]);
                task.setId(Integer.parseInt(strSpl[0]));
                task.setStatus(status);
                return task;
            } else if (strSpl[1].equals(Type.EPIC.toString())) {
                Epic epic = new Epic(strSpl[2], strSpl[4]);
                epic.setId(Integer.parseInt(strSpl[0]));
                epic.setStatus(Status.NEW);
                return epic;
            } else {
                int id = Integer.parseInt(strSpl[5]);
                Subtask subtask = new Subtask(strSpl[2], strSpl[4], status, id);
                subtask.setId(Integer.parseInt(strSpl[0]));
                return subtask;
            }
        } else {
            return null;
        }
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> id = new ArrayList<>();
        if (!value.equals(firstLine) && value.length() != 0) {
            String[] historyId = value.split(",");
            for (String s : historyId) {
                id.add(Integer.parseInt(s));
            }
        }
        return id;
    }
}
