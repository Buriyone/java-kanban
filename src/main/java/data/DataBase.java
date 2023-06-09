package main.java.data;

import main.java.tasks.Subtask;
import main.java.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class DataBase {
	private final HashMap<LocalDateTime, Boolean> calendar = new HashMap<>();
	
	private Set<Task> prioritizedTasks;
	
	public LocalDateTime getFreeTime() {
		if (calendar.isEmpty()) {
			return LocalDateTime.now();
		} else {
			for (LocalDateTime time : calendar.keySet()) {
				if (calendar.get(time).equals(false)) {
					if (calendar.get(time.plusMinutes(15)).equals(true)) {
						return time.plusMinutes(15);
					}
				}
			}
			return null;
		}
	}
	
	public void addTime(LocalDateTime time) {
		if (calendar.isEmpty()) {
			createCalendar(time);
			calendar.put(time, false);
		} else {
			calendar.put(time, false);
		}
	}
	
	protected void createCalendar(LocalDateTime time) {
		calendar.put(time, true);
		
		for (int i = 0; i < 4320; i++) {
			time = time.plusMinutes(15);
			calendar.put(time, true);
		}
	}
	
	public void updatePrioritizedTasks(HashMap<Integer, Task> tasks, HashMap<Integer, Subtask> subtasks) {
		Comparator<Task> comparator = new Comparator<Task>() {
			@Override
			public int compare(Task o1, Task o2) {
				if (o1.getStartTime() == null) {
					return 1;
				} else if(o2.getStartTime() == null){
					return -1;
				} else if(o1.getStartTime() == null && o2.getStartTime() == null){
					return 1;
				} else {
					Duration duration = Duration.between(o1.getStartTime(), o2.getStartTime());
					long durSec = duration.toSeconds();
					
					if (durSec > 0) {
						return -1;
					} else {
						return 1;
					}
				}
			}
		};
		
		prioritizedTasks = new TreeSet<Task>(comparator);
		
		for (Integer id : tasks.keySet()) {
			prioritizedTasks.add(tasks.get(id));
		}
		
		for (Integer id : subtasks.keySet()) {
			prioritizedTasks.add(subtasks.get(id));
		}
	}
	
	public Set<Task> getPrioritizedTasks() {
		return prioritizedTasks;
	}
}
