package main.java.managers;

import org.junit.jupiter.api.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
	@BeforeEach
	public void initialManager(){
		manager = new InMemoryTaskManager();
	}
	
	@AfterEach
	public void cleanerManager() {
		manager.deleteTasks();
		manager.deleteEpics();
		
		taskEnder = 1;
		epicEnder = 1;
		subtaskEnder = 1;
	}
}