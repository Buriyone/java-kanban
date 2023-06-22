package main.java.manager;

import main.java.model.Node;
import main.java.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
	
	private final Map<Integer, Node<Task>> hashNodes = new HashMap<>();
	
	private Node<Task> head;
	
	private Node<Task> tail;
	
	
	private List<Task> getTasks() {
		List<Task> history = new ArrayList<>();
		
		Node<Task> node = head;
		
		while (node != null) {
			history.add(node.data);
			node = node.next;
		}
		return history;
	}
	
	private void removeNode(Node<Task> node) {
		if (node != null) {
			Node<Task> prevNode = node.prev;
			Node<Task> nextNode = node.next;
			
			if (prevNode == null && nextNode == null) {
				node.data = null;
				head = null;
				tail = null;
				return;
			} else if (prevNode == null) {
				head = nextNode;
				nextNode.prev = null;
			} else if (nextNode == null) {
				tail = prevNode;
				prevNode.next = null;
			} else {
				prevNode.next = nextNode;
				nextNode.prev = prevNode;
			}
			node.data = null;
		}
	}
	
	@Override
	public void add(Task task) {
		if (task != null) {
			if (!hashNodes.containsKey(task.getId())) {
				linkLast(task);
			} else {
				removeNode(hashNodes.get(task.getId()));
				linkLast(task);
			}
		}
	}
	
	private void linkLast(Task task) {
		Node<Task> prevTail = tail;
		Node<Task> currentNode = new Node<>(task, prevTail, null);
		
		tail = currentNode;
		if (prevTail == null) {
			head = currentNode;
		} else {
			prevTail.next = currentNode;
		}
		hashNodes.put(task.getId(), currentNode);
	}
	
	@Override
	public void remove(int id) {
		removeNode(hashNodes.get(id));
		hashNodes.remove(id);
	}
	
	@Override
	public List<Task> getHistory() {
		return getTasks();
	}
}
