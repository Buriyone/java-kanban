package main.java.tasks;

import main.java.models.Type;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Epic extends Task {
	private ArrayList<Subtask> subtasks = new ArrayList<>();
	
	public Epic(String taskName, String descriptionEpic) {
		super(taskName, descriptionEpic);
	}
	
	public LocalDateTime getEndTime() {
		if (!subtasks.isEmpty()) {
			List<Subtask> sudSort = subtasks.stream()
					.filter(subtask -> subtask.getEndTime() != null)
					.collect(Collectors.toList());
			
			if (!sudSort.isEmpty()) {
				return sudSort.get(sudSort.size() - 1).getEndTime();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	public LocalDateTime getStartTime() {
		if (!subtasks.isEmpty()) {
			List<Subtask> sudSort = subtasks.stream()
					.filter(subtask -> subtask.getStartTime() != null)
					.collect(Collectors.toList());
			
			if (!sudSort.isEmpty()) {
				return sudSort.get(0).getStartTime();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	public int getDuration() {
		if (!subtasks.isEmpty()) {
			List<Subtask> sudSort = subtasks.stream()
					.filter(subtask -> subtask.getStartTime() != null)
					.collect(Collectors.toList());
			
			if (!sudSort.isEmpty()) {
				int duration = 0;
				
				for (int i = 0; i < sudSort.size(); i++) {
					duration += sudSort.get(i).getDuration();
				}
				
				return duration;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}
	
	public ArrayList<Subtask> getSubtasks() {
		return subtasks;
	}
	
	public void setSubtasks(ArrayList<Subtask> subtasks) {
		this.subtasks = subtasks;
	}
	
	public Type getType() {
		return Type.EPIC;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (this.getClass() != obj.getClass()) return false;
		Epic epic = (Epic) obj;
		return getId() == epic.getId() && Objects.equals(getName(), epic.getName())
				&& Objects.equals(getDescription(), epic.getDescription())
				&& Objects.equals(getStatus(), epic.getStatus())
				&& Objects.equals(getSubtasks(), epic.getSubtasks())
				&& Objects.equals(getDuration(), epic.getDuration())
				&& Objects.equals(getStartTime(), epic.getStartTime());
	}
	
	@Override
	public int hashCode() {
		int hash = 17;
		if (getName() != null) {
			hash = hash + getName().hashCode() + getId() * 3 + getDescription().hashCode()
					+ getStatus().hashCode() + getSubtasks().hashCode();
		} else if (getStartTime() != null) {
			hash = hash + getStartTime().hashCode() + getDuration() * 3;
		}
		hash = hash * 31;
		return hash;
	}
	
	@Override
	public String toString() {
		return getId() + "," + getType() + "," + getName() + "," + getStatus() + "," + getDescription()
				+ "," + "," + getStartTime();
	}
}
