package main.java.server;

import com.google.gson.*;
import main.java.manager.Managers;
import main.java.manager.TaskManager;
import main.java.task.Epic;
import main.java.task.Subtask;
import main.java.task.Task;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
	private static final Gson gson = new GsonBuilder().serializeNulls().create();
	private static KVServer server;
	private static HttpTaskServer taskServer;
	
	private static HttpClient client;
	private static TaskManager manager;
	
	private static Task task;
	private static Epic epic;
	private static Subtask subtask;
	private static int taskId;
	private static int epicId;
	private static int subtaskId;
	
	@BeforeAll
	public static void start() throws IOException {
		server = new KVServer();
		server.start();
		
		taskServer = new HttpTaskServer();
		taskServer.start();
	}
	@BeforeEach
	public void initialManager() throws IOException, InterruptedException {
		client = HttpClient.newHttpClient();
		
		Task testTask = new Task("testName", "testDescription");
		URI urlTask = URI.create("http://localhost:8080/tasks/task/");
		String jsonTask = gson.toJson(testTask);
		final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonTask);
		HttpRequest request = HttpRequest.newBuilder().uri(urlTask).POST(body).build();
		client.send(request, HttpResponse.BodyHandlers.ofString());
		
		HttpRequest request2 = HttpRequest.newBuilder().uri(urlTask).GET().build();
		HttpResponse<String> response = client.send(request2, HttpResponse.BodyHandlers.ofString());
		
		JsonElement jsonElement = JsonParser.parseString(response.body());
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
		task = gson.fromJson(jsonObject, Task.class);
		taskId = task.getId();
		
		Epic epicTest = new Epic("testName", "testDescription");
		URI urlEpic = URI.create("http://localhost:8080/tasks/epic/");
		String jsonEpic = gson.toJson(epicTest);
		final HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
		HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
		client.send(requestEpic, HttpResponse.BodyHandlers.ofString());
		
		HttpRequest requestEpic2 = HttpRequest.newBuilder().uri(urlEpic).GET().build();
		HttpResponse<String> responseEpic = client.send(requestEpic2, HttpResponse.BodyHandlers.ofString());
		
		JsonElement jsonElementEpic = JsonParser.parseString(responseEpic.body());
		JsonArray jsonArrayEpic = jsonElementEpic.getAsJsonArray();
		JsonObject jsonObjectEpic = jsonArrayEpic.get(0).getAsJsonObject();
		epic = gson.fromJson(jsonObjectEpic, Epic.class);
		epicId = epic.getId();
		
		Subtask subtaskTest = new Subtask("testName", "testDescription", epicId);
		URI urlSubtask = URI.create("http://localhost:8080/tasks/subtask/");
		String jsonSubtask = gson.toJson(subtaskTest);
		final HttpRequest.BodyPublisher bodySubtask = HttpRequest.BodyPublishers.ofString(jsonSubtask);
		HttpRequest requestSubtask = HttpRequest.newBuilder().uri(urlSubtask).POST(bodySubtask).build();
		client.send(requestSubtask, HttpResponse.BodyHandlers.ofString());
		
		HttpRequest requestSubtask2 = HttpRequest.newBuilder().uri(urlSubtask).GET().build();
		HttpResponse<String> responseSubtask = client.send(requestSubtask2, HttpResponse.BodyHandlers.ofString());
		
		JsonElement jsonElementSubtask = JsonParser.parseString(responseSubtask.body());
		JsonArray jsonArraySubtask = jsonElementSubtask.getAsJsonArray();
		JsonObject jsonObjectSubtask = jsonArraySubtask.get(0).getAsJsonObject();
		subtask = gson.fromJson(jsonObjectSubtask, Subtask.class);
		subtaskId = subtask.getId();
		epic.getSubtasks().add(subtask);
		
		manager = Managers.getDefault();
	}
	
	@AfterEach
	public void stop() throws IOException, InterruptedException {
		URI url = URI.create("http://localhost:8080/tasks/task/");
		HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
		client.send(request, HttpResponse.BodyHandlers.ofString());
		
		URI url2 = URI.create("http://localhost:8080/tasks/epic/");
		HttpRequest request2 = HttpRequest.newBuilder().uri(url2).DELETE().build();
		client.send(request2, HttpResponse.BodyHandlers.ofString());
		
	}
	
	@AfterAll
	public static void stopServer() {
		taskServer.stop();
		server.stop();
	}
	
	@Test
	public void getTasksTest() throws IOException, InterruptedException {
		URI url = URI.create("http://localhost:8080/tasks/task/");
		HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		List<Task> expectedList = new ArrayList<>();
		expectedList.add(task);
		String expectedJson = gson.toJson(expectedList);
		
		assertNotNull(response.body(), "Тело запроса не было возвращено.");
		assertEquals(expectedJson, response.body(), "Тело запроса отличается.");
		
		assertEquals(response.body(), gson.toJson(manager.getTasks()), "Задачи в менеджере отличаются.");
	}
	
	@Test
	public void deleteTasksTest() throws IOException, InterruptedException {
		assertFalse(manager.getTasks().isEmpty(), "Менеджер пуст.");
		
		URI url = URI.create("http://localhost:8080/tasks/task/");
		HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		String expectedJson = gson.toJson("Все задачи удалены.");
		
		assertNotNull(response.body(), "Тело запроса не было возвращено.");
		assertEquals(expectedJson, response.body(), "Тело запроса отличается.");
		
		List<Task> expectedList = new ArrayList<>();
		String expectedJson2 = gson.toJson(expectedList);
		
		HttpRequest request2 = HttpRequest.newBuilder().uri(url).GET().build();
		HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
		
		assertNotNull(response2.body(), "Тело запроса не было возвращено.");
		assertEquals(expectedJson2, response2.body(), "Тело запроса отличается.");
		
		manager = Managers.getDefault();
		
		assertTrue(manager.getTasks().isEmpty(), "Задачи не были удалены.");
	}
	
	@Test
	public void getTaskTest() throws IOException, InterruptedException {
		URI url = URI.create("http://localhost:8080/tasks/task/?id=" + taskId);
		HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		HttpRequest request2 = HttpRequest.newBuilder().uri(url).GET().build();
		HttpResponse<String> responseTask = client.send(request2, HttpResponse.BodyHandlers.ofString());
		
		JsonElement jsonElement = JsonParser.parseString(responseTask.body());
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		Task actualTask = gson.fromJson(jsonObject, Task.class);
		
		assertNotNull(response.body(), "Тело запроса не было возвращено.");
		assertEquals(task, actualTask, "Тело запроса отличается.");
		
		assertEquals(actualTask, manager.getTask(actualTask.getId()), "Задачи отличаются.");
	}
	
	@Test
	public void addTaskTest() throws IOException, InterruptedException {
		URI url = URI.create("http://localhost:8080/tasks/task/");
		task.setId(0);
		String json = gson.toJson(task);
		final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
		HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
		client.send(request, HttpResponse.BodyHandlers.ofString());
		
		HttpRequest request2 = HttpRequest.newBuilder().uri(url).GET().build();
		HttpResponse<String> response = client.send(request2, HttpResponse.BodyHandlers.ofString());
		
		JsonElement jsonElement = JsonParser.parseString(response.body());
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		JsonObject jsonObject = jsonArray.get(1).getAsJsonObject();
		Task task1 = gson.fromJson(jsonObject, Task.class);
		
		task.setId(task1.getId());
		
		assertEquals(task, task1, "Задачи не идентичны.");
		
		manager = Managers.getDefault();
		
		assertEquals(task1, manager.getTask(task1.getId()), "Задача не была добавлена.");
	}
	
	@Test
	public void updateTaskTest() throws IOException, InterruptedException {
		task.setName("newTestName");
		task.setDescription("newTestDescription");
		
		URI url = URI.create("http://localhost:8080/tasks/task/");
		String json = gson.toJson(task);
		final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
		HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		String expectedMessage = gson.toJson("Задача оптимизирована.");
		assertEquals(expectedMessage, response.body(), "Тело запроса отличается.");
		
		URI url2 = URI.create("http://localhost:8080/tasks/task/?id=" + taskId);
		HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
		client.send(request2, HttpResponse.BodyHandlers.ofString());
		
		HttpRequest request3 = HttpRequest.newBuilder().uri(url2).GET().build();
		HttpResponse<String> responseTask = client.send(request3, HttpResponse.BodyHandlers.ofString());
		
		JsonElement jsonElement = JsonParser.parseString(responseTask.body());
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		Task actualTask = gson.fromJson(jsonObject, Task.class);
		
		assertEquals(task, actualTask, "Задачи отличаются.");
		
		manager = Managers.getDefault();
		
		assertEquals(actualTask, manager.getTask(actualTask.getId()), "Задачи не идентичны.");
	}
	
	@Test
	public void deleteTaskTest() throws IOException, InterruptedException {
		URI url = URI.create("http://localhost:8080/tasks/task/?id=" + taskId);
		HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		String expectedMessage = gson.toJson("Задача с id: " + taskId + " удалена.");
		String actualMessage = response.body();
		
		assertEquals(expectedMessage, actualMessage, "Ответы не идентичны.");
		
		URI url2 = URI.create("http://localhost:8080/tasks/task/");
		HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
		HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
		
		List<Task> expectedList = new ArrayList<>();
		String expectedJson = gson.toJson(expectedList);
		
		assertEquals(expectedJson, response2.body(), "Списки не идентичны.");
		
		manager = Managers.getDefault();
		
		assertEquals(response2.body(), gson.toJson(manager.getTasks()), "Списки задач отличаются.");
	}
	
	@Test
	public void getEpicsTest() throws IOException, InterruptedException {
		URI url = URI.create("http://localhost:8080/tasks/epic/");
		HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		List<Task> expectedList = new ArrayList<>();
		expectedList.add(epic);
		String expectedJson = gson.toJson(expectedList);
		
		assertNotNull(response.body(), "Тело запроса не было возвращено.");
		assertEquals(expectedJson, response.body(), "Тело запроса отличается.");
		
		assertEquals(response.body(), gson.toJson(manager.getEpics()), "Эпики в менеджере отличаются.");
	}
	
	@Test
	public void deleteEpicsTest() throws IOException, InterruptedException {
		assertFalse(manager.getEpics().isEmpty(), "Список эпиков пуст.");
		
		URI url = URI.create("http://localhost:8080/tasks/epic/");
		HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		String expectedJson = gson.toJson("Все эпики удалены.");
		
		assertNotNull(response.body(), "Тело запроса не было возвращено.");
		assertEquals(expectedJson, response.body(), "Тело запроса отличается.");
		
		List<Task> expectedList = new ArrayList<>();
		String expectedJson2 = gson.toJson(expectedList);
		
		HttpRequest request2 = HttpRequest.newBuilder().uri(url).GET().build();
		HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
		
		assertNotNull(response2.body(), "Тело запроса не было возвращено.");
		assertEquals(expectedJson2, response2.body(), "Тело запроса отличается.");
		
		manager = Managers.getDefault();
		
		assertTrue(manager.getEpics().isEmpty(), "Эпики не были удалены.");
	}
	
	@Test
	public void getEpicTest() throws IOException, InterruptedException {
		URI url = URI.create("http://localhost:8080/tasks/epic/?id=" + epicId);
		HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		HttpRequest request2 = HttpRequest.newBuilder().uri(url).GET().build();
		HttpResponse<String> responseTask = client.send(request2, HttpResponse.BodyHandlers.ofString());
		
		JsonElement jsonElement = JsonParser.parseString(responseTask.body());
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		Epic actualEpic = gson.fromJson(jsonObject, Epic.class);
		System.out.println(epic.getSubtasks());
		System.out.println(actualEpic.getSubtasks());
		
		assertNotNull(response.body(), "Тело запроса не было возвращено.");
		assertEquals(epic, actualEpic, "Тело запроса отличается.");
		
		assertEquals(actualEpic, manager.getEpic(actualEpic.getId()), "Эпики отличаются.");
	}
	
	@Test
	public void addEpicTest() throws IOException, InterruptedException {
		URI url = URI.create("http://localhost:8080/tasks/epic/");
		epic.setId(0);
		epic.getSubtasks().clear();
		epic.setName("newTestEpic");
		String json = gson.toJson(epic);
		final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
		HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
		client.send(request, HttpResponse.BodyHandlers.ofString());
		
		HttpRequest request2 = HttpRequest.newBuilder().uri(url).GET().build();
		HttpResponse<String> response = client.send(request2, HttpResponse.BodyHandlers.ofString());
		
		JsonElement jsonElement = JsonParser.parseString(response.body());
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		JsonObject jsonObject = jsonArray.get(1).getAsJsonObject();
		Epic epic1 = gson.fromJson(jsonObject, Epic.class);
		
		epic.setId(epic1.getId());
		
		assertEquals(epic, epic1, "Эпики не идентичны.");
		
		manager = Managers.getDefault();
		
		assertEquals(epic1, manager.getEpic(epic1.getId()), "Эпик не был добавлен.");
	}
	
	@Test
	public void updateEpicTest() throws IOException, InterruptedException {
		epic.setName("newTestName");
		epic.setDescription("newTestDescription");
		
		URI url = URI.create("http://localhost:8080/tasks/epic/");
		String json = gson.toJson(epic);
		final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
		HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		String expectedMessage = gson.toJson("Эпик оптимизирован.");
		assertEquals(expectedMessage, response.body(), "Тело запроса отличается.");
		
		URI url2 = URI.create("http://localhost:8080/tasks/epic/?id=" + epicId);
		HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
		client.send(request2, HttpResponse.BodyHandlers.ofString());
		
		HttpRequest request3 = HttpRequest.newBuilder().uri(url2).GET().build();
		HttpResponse<String> responseEpic = client.send(request3, HttpResponse.BodyHandlers.ofString());
		
		JsonElement jsonElement = JsonParser.parseString(responseEpic.body());
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		Epic actualEpic = gson.fromJson(jsonObject, Epic.class);
		
		assertEquals(epic, actualEpic, "Эпики отличаются.");
		
		manager = Managers.getDefault();
		
		assertEquals(actualEpic, manager.getEpic(actualEpic.getId()), "Эпики не идентичны.");
	}
	
	@Test
	public void deleteEpicTest() throws IOException, InterruptedException {
		URI url = URI.create("http://localhost:8080/tasks/epic/?id=" + epicId);
		HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		String expectedMessage = gson.toJson("Эпик с id: " + epicId + " удален.");
		String actualMessage = response.body();
		
		assertEquals(expectedMessage, actualMessage, "Ответы не идентичны.");
		
		URI url2 = URI.create("http://localhost:8080/tasks/epic/");
		HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
		HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
		
		List<Task> expectedList = new ArrayList<>();
		String expectedJson = gson.toJson(expectedList);
		
		assertEquals(expectedJson, response2.body(), "Списки не идентичны.");
		
		manager = Managers.getDefault();
		
		assertEquals(response2.body(), gson.toJson(manager.getEpics()), "Списки эпиков отличаются.");
	}
	
	@Test
	public void getEpicSubtasksTest() throws IOException, InterruptedException {
		URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=" + epicId);
		HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		List<Task> expectedList = new ArrayList<>();
		expectedList.add(subtask);
		String expectedJson = gson.toJson(expectedList);
		
		assertNotNull(response.body(), "Тело запроса не было возвращено.");
		assertEquals(expectedJson, response.body(), "Тело запроса отличается.");
		
		assertEquals(response.body(), gson.toJson(manager.getEpicSubtasks(epicId)),
				"Списки подзадач отличаются");
	}
	
	@Test
	public void getSubtasksTest() throws IOException, InterruptedException {
		URI url = URI.create("http://localhost:8080/tasks/subtask/");
		HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		List<Task> expectedList = new ArrayList<>();
		expectedList.add(subtask);
		String expectedJson = gson.toJson(expectedList);
		
		assertNotNull(response.body(), "Тело запроса не было возвращено.");
		assertEquals(expectedJson, response.body(), "Тело запроса отличается.");
		
		assertEquals(response.body(), gson.toJson(manager.getSubtasks()), "Подзадачи в менеджере отличаются.");
	}
	
	@Test
	public void deleteSubtasksTest() throws IOException, InterruptedException {
		URI url = URI.create("http://localhost:8080/tasks/subtask/");
		HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		String expectedJson = gson.toJson("Все подзадачи удалены.");
		
		assertNotNull(response.body(), "Тело запроса не было возвращено.");
		assertEquals(expectedJson, response.body(), "Тело запроса отличается.");
		
		List<Task> expectedList = new ArrayList<>();
		String expectedJson2 = gson.toJson(expectedList);
		
		HttpRequest request2 = HttpRequest.newBuilder().uri(url).GET().build();
		HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
		
		assertNotNull(response2.body(), "Тело запроса не было возвращено.");
		assertEquals(expectedJson2, response2.body(), "Тело запроса отличается.");
		
		manager = Managers.getDefault();
		
		assertTrue(manager.getSubtasks().isEmpty(), "Подзадачи не были удалены.");
	}
	
	@Test
	public void getSubtaskTest() throws IOException, InterruptedException {
		URI url = URI.create("http://localhost:8080/tasks/subtask/?id=" + subtaskId);
		HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		HttpRequest request2 = HttpRequest.newBuilder().uri(url).GET().build();
		HttpResponse<String> responseTask = client.send(request2, HttpResponse.BodyHandlers.ofString());
		
		JsonElement jsonElement = JsonParser.parseString(responseTask.body());
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		Subtask actualSubtask = gson.fromJson(jsonObject, Subtask.class);
		
		assertNotNull(response.body(), "Тело запроса не было возвращено.");
		assertEquals(subtask, actualSubtask, "Тело запроса отличается.");
		
		assertEquals(actualSubtask, manager.getSubtask(actualSubtask.getId()), "Подзадачи отличаются.");
	}
	
	@Test
	public void addSubtaskTest() throws IOException, InterruptedException {
		URI url = URI.create("http://localhost:8080/tasks/subtask/");
		subtask.setId(0);
		String json = gson.toJson(subtask);
		final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
		HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
		client.send(request, HttpResponse.BodyHandlers.ofString());
		
		HttpRequest request2 = HttpRequest.newBuilder().uri(url).GET().build();
		HttpResponse<String> response = client.send(request2, HttpResponse.BodyHandlers.ofString());
		
		JsonElement jsonElement = JsonParser.parseString(response.body());
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		JsonObject jsonObject = jsonArray.get(1).getAsJsonObject();
		Subtask subtask1 = gson.fromJson(jsonObject, Subtask.class);
		
		subtask.setId(subtask1.getId());
		
		assertEquals(subtask, subtask1, "Подзадачи не идентичны.");
		
		manager = Managers.getDefault();
		
		assertEquals(subtask1, manager.getSubtask(subtask1.getId()), "Подзадача не была добавлена.");
	}
	
	@Test
	public void updateSubtaskTest() throws IOException, InterruptedException {
		subtask.setName("newTestName");
		subtask.setDescription("newTestDescription");
		
		URI url = URI.create("http://localhost:8080/tasks/subtask/");
		String json = gson.toJson(subtask);
		final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
		HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		String expectedMessage = gson.toJson("Подзадача оптимизирована.");
		assertEquals(expectedMessage, response.body(), "Тело запроса отличается.");
		
		URI url2 = URI.create("http://localhost:8080/tasks/subtask/?id=" + subtaskId);
		HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
		client.send(request2, HttpResponse.BodyHandlers.ofString());
		
		HttpRequest request3 = HttpRequest.newBuilder().uri(url2).GET().build();
		HttpResponse<String> responseEpic = client.send(request3, HttpResponse.BodyHandlers.ofString());
		
		JsonElement jsonElement = JsonParser.parseString(responseEpic.body());
		System.out.println(jsonElement);
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		Subtask actualSubtask = gson.fromJson(jsonObject, Subtask.class);
		
		assertEquals(subtask, actualSubtask, "Подзадачи отличаются.");
		
		manager = Managers.getDefault();
		
		assertEquals(actualSubtask, manager.getSubtask(actualSubtask.getId()), "Подзадачи не идентичны.");
	}
	
	@Test
	public void deleteSubtaskTest() throws IOException, InterruptedException {
		URI url = URI.create("http://localhost:8080/tasks/subtask/?id=" + subtaskId);
		HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		String expectedMessage = gson.toJson("Подзадача с id: " + subtaskId + " удалена.");
		String actualMessage = response.body();
		
		assertEquals(expectedMessage, actualMessage, "Ответы не идентичны.");
		
		URI url2 = URI.create("http://localhost:8080/tasks/subtask/");
		HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
		HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
		
		List<Task> expectedList = new ArrayList<>();
		String expectedJson = gson.toJson(expectedList);
		
		assertEquals(expectedJson, response2.body(), "Списки не идентичны.");
		
		manager = Managers.getDefault();
		
		assertEquals(response2.body(), gson.toJson(manager.getSubtasks()), "Списки подззадач отличаются.");
	}
	
	@Test
	public void getHistoryTest() throws IOException, InterruptedException {
		URI url = URI.create("http://localhost:8080/tasks/subtask/?id=" + subtaskId);
		HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
		client.send(request, HttpResponse.BodyHandlers.ofString());
		
		URI url2 = URI.create("http://localhost:8080/tasks/task/?id=" + taskId);
		HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
		client.send(request2, HttpResponse.BodyHandlers.ofString());
		
		URI url3 = URI.create("http://localhost:8080/tasks/epic/?id=" + epicId);
		HttpRequest request3 = HttpRequest.newBuilder().uri(url3).GET().build();
		client.send(request3, HttpResponse.BodyHandlers.ofString());
		
		List<Task> expectedList = new ArrayList<>();
		expectedList.add(subtask);
		expectedList.add(task);
		expectedList.add(epic);
		String expectedJson = gson.toJson(expectedList);
		
		URI urlHistory = URI.create("http://localhost:8080/tasks/history/");
		HttpRequest requestHistory = HttpRequest.newBuilder().uri(urlHistory).GET().build();
		HttpResponse<String> responseHistory = client.send(requestHistory, HttpResponse.BodyHandlers.ofString());
		
		assertNotNull(responseHistory.body(), "Тело запроса не было возвращено.");
		assertEquals(expectedJson, responseHistory.body(), "Тело запроса отличается.");
		
		manager = Managers.getDefault();
		
		assertEquals(responseHistory.body(), gson.toJson(manager.getHistory()), "История отличается");
	}
	
	@Test
	public void getPrioritizedTasksTest() throws IOException, InterruptedException {
		subtask.setStartTime(LocalDateTime.now());
		
		URI url = URI.create("http://localhost:8080/tasks/subtask/");
		String json = gson.toJson(subtask);
		final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
		HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
		client.send(request, HttpResponse.BodyHandlers.ofString());
		
		HttpRequest request2 = HttpRequest.newBuilder().uri(url).GET().build();
		HttpResponse<String> responseTask = client.send(request2, HttpResponse.BodyHandlers.ofString());
		
		JsonElement jsonElement = JsonParser.parseString(responseTask.body());
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
		subtask = gson.fromJson(jsonObject, Subtask.class);
		
		List<Task> expectedList = new ArrayList<>();
		expectedList.add(subtask);
		expectedList.add(task);
		String expectedJson = gson.toJson(expectedList);
		
		URI urlPrioritized = URI.create("http://localhost:8080/tasks/");
		HttpRequest requestPrioritized = HttpRequest.newBuilder().uri(urlPrioritized).GET().build();
		HttpResponse<String> responsePrioritized = client.send(requestPrioritized,
				HttpResponse.BodyHandlers.ofString());
		
		assertNotNull(responsePrioritized.body(), "Тело запроса не было возвращено.");
		assertEquals(expectedJson, responsePrioritized.body(), "Тело запроса отличается.");
		
		manager = Managers.getDefault();
		
		assertEquals(responsePrioritized.body(), gson.toJson(manager.getPrioritizedTasks()),
				"Список приоритетных задач отличается.");
	}
}