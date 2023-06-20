package main.java.servers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import main.java.managers.Managers;
import main.java.managers.TaskManager;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {
	private static final int PORT = 8080;
	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
	
	private static final GsonBuilder gsonBuilder = new GsonBuilder();
	private static final Gson gson = gsonBuilder.serializeNulls().create();
	
	private static TaskManager manager;
	private static  HttpServer server;
	
	public void start() throws IOException {
		server = HttpServer.create();
		manager = Managers.getDefault();
		server.bind(new InetSocketAddress(PORT), 0);
		server.createContext("/tasks/task/", new TaskHandler());
		server.createContext("/tasks/epic/", new EpicHandler());
		server.createContext("/tasks/subtask/", new SubtaskHandler());
		server.createContext("/tasks/subtask/epic/", new EpicSubtasksHandler());
		server.createContext("/tasks/history/", new HistoryHandler());
		server.createContext("/tasks/", new PrioritizedTasksHandler());
		server.start();
		
		System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
	}
	
	public void stop() {
		server.stop(1);
	}
	
	private static class TaskHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			manager = Managers.getDefault();
			String method = exchange.getRequestMethod();
			String query = exchange.getRequestURI().getQuery();
			
			switch (method) {
				case "GET":
					if (query != null) {
						getTask(exchange);
					} else {
						writeResponse(200, gson.toJson(manager.getTasks()), exchange);
					}
					break;
				case "POST":
					addOrUpdateTask(exchange);
					break;
				case "DELETE":
					if (query != null) {
						deleteTask(exchange);
					} else {
						deleteTasks(exchange);
					}
					break;
				default:
					writeResponse(400, gson.toJson("Некорректный метод."), exchange);
			}
		}
	}
	
	private static class EpicHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			manager = Managers.getDefault();
			String method = exchange.getRequestMethod();
			String query = exchange.getRequestURI().getQuery();
			
			switch (method) {
				case "GET":
					if (query != null) {
						getEpic(exchange);
					} else {
						writeResponse(200, gson.toJson(manager.getEpics()), exchange);
					}
					break;
				case "POST":
					addOrUpdateEpic(exchange);
					break;
				case "DELETE":
					if (query != null) {
						deleteEpic(exchange);
					} else {
						deleteEpics(exchange);
					}
					break;
				default:
					writeResponse(400, gson.toJson("Некорректный метод."), exchange);
			}
		}
	}
	
	private static class SubtaskHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			manager = Managers.getDefault();
			String method = exchange.getRequestMethod();
			String query = exchange.getRequestURI().getQuery();
			
			switch (method) {
				case "GET":
					if (query != null) {
						getSubtask(exchange);
					} else {
						writeResponse(200, gson.toJson(manager.getSubtasks()), exchange);
					}
					break;
				case "POST":
					addOrUpdateSubtask(exchange);
					break;
				case "DELETE":
					if (query != null) {
						deleteSubtask(exchange);
					} else {
						deleteSubtasks(exchange);
					}
					break;
				default:
					writeResponse(400, gson.toJson("Некорректный метод."), exchange);
			}
		}
	}
	
	private static class EpicSubtasksHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			manager = Managers.getDefault();
			String method = exchange.getRequestMethod();
			String query = exchange.getRequestURI().getQuery();
			
			if (method.equals("GET")) {
				if (query != null) {
					getEpicSubtasks(exchange);
				} else {
					writeResponse(400, gson.toJson("Некорректный запрос"), exchange);
				}
			} else {
				writeResponse(400, gson.toJson("Некорректный метод."), exchange);
			}
		}
	}
	
	private static class HistoryHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			manager = Managers.getDefault();
			String method = exchange.getRequestMethod();
			
			
			if (method.equals("GET")) {
				if (manager.getHistory().isEmpty()) {
					writeResponse(200, gson.toJson("История пуста."), exchange);
				} else {
					writeResponse(200, gson.toJson(manager.getHistory()), exchange);
				}
			} else {
				writeResponse(400, gson.toJson("Некорректный метод."), exchange);
			}
		}
	}
	
	private static class PrioritizedTasksHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			manager = Managers.getDefault();
			String method = exchange.getRequestMethod();
			
			if (method.equals("GET")) {
				if (manager.getPrioritizedTasks().isEmpty()) {
					writeResponse(200, gson.toJson("Список приоритетных задач пуст."), exchange);
				} else {
					writeResponse(200, gson.toJson(manager.getPrioritizedTasks()), exchange);
				}
			} else {
				writeResponse(400, gson.toJson("Некорректный метод."), exchange);
			}
		}
	}
	
	private static void getTask(HttpExchange exchange) throws IOException {
		final String[] querySplit = exchange.getRequestURI().getQuery().split("=");
		final int id = Integer.parseInt(querySplit[1]);
		
		int code = 404;
		String response = gson.toJson("Задачи с id: " + id + " не существует.");
		
		for (Task task : manager.getTasks()) {
			if (task.getId() == id) {
				code = 200;
				response = gson.toJson(manager.getTask(id));
				break;
			}
		}
		writeResponse(code, response, exchange);
	}
	
	private static void addOrUpdateTask(HttpExchange exchange) throws IOException {
		InputStream inputStream = exchange.getRequestBody();
		String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
		
		Task task = gson.fromJson(body, Task.class);
		
		int code = 500;
		String response = gson.toJson("В настоящий момент сервер не может выполнить эту задачу.");
		
		if (task.getId() == 0) {
			manager.addTask(task);
			code = 200;
			response = gson.toJson("Задача добавлена с id: " + task.getId());
		} else {
			for (Task t : manager.getTasks()) {
				if (t.getId() == task.getId()) {
					manager.updateTask(task);
					code = 200;
					response = gson.toJson("Задача оптимизирована.");
					break;
				} else {
					code = 404;
					response = gson.toJson("Задача с id: " + task.getId() + " не найдена.");
				}
			}
		}
		writeResponse(code, response, exchange);
	}
	
	private static void deleteTask(HttpExchange exchange) throws IOException {
		final String[] querySplit = exchange.getRequestURI().getQuery().split("=");
		final int id = Integer.parseInt(querySplit[1]);
		
		int code = 404;
		String response = gson.toJson("Задачи с id: " + id + " не существует.");
		
		for (Task task : manager.getTasks()) {
			if (task.getId() == id) {
				code = 200;
				manager.deleteTask(id);
				response = gson.toJson("Задача с id: " + task.getId() + " удалена.");
				break;
			}
		}
		writeResponse(code, response, exchange);
	}
	
	private static void deleteTasks(HttpExchange exchange) throws IOException {
		manager.deleteTasks();
		writeResponse(200, gson.toJson("Все задачи удалены."), exchange);
	}
	
	private static void getEpic(HttpExchange exchange) throws IOException {
		final String[] querySplit = exchange.getRequestURI().getQuery().split("=");
		final int id = Integer.parseInt(querySplit[1]);
		
		int code = 404;
		String response = gson.toJson("Эпик с id: " + id + " не существует.");
		
		for (Task task : manager.getEpics()) {
			if (task.getId() == id) {
				code = 200;
				response = gson.toJson(manager.getEpic(id));
				break;
			}
		}
		writeResponse(code, response, exchange);
	}
	
	private static void addOrUpdateEpic(HttpExchange exchange) throws IOException {
		InputStream inputStream = exchange.getRequestBody();
		String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
		
		Epic epic = gson.fromJson(body, Epic.class);
		
		int code = 500;
		String response = gson.toJson("В настоящий момент сервер не может выполнить эту задачу.");
		
		if (epic.getId() == 0) {
			manager.addEpic(epic);
			code = 200;
			response = gson.toJson("Эпик добавлен с id: " + epic.getId());
		} else {
			for (Task t : manager.getEpics()) {
				if (t.getId() == epic.getId()) {
					manager.updateEpic(epic);
					code = 200;
					response = gson.toJson("Эпик оптимизирован.");
					break;
				} else {
					code = 404;
					response = gson.toJson("Эпик с id: " + epic.getId() + " не найден.");
				}
			}
		}
		writeResponse(code, response, exchange);
	}
	
	private static void deleteEpic(HttpExchange exchange) throws IOException {
		final String[] querySplit = exchange.getRequestURI().getQuery().split("=");
		final int id = Integer.parseInt(querySplit[1]);
		
		int code = 404;
		String response = gson.toJson("Эпик с id: " + id + " не существует.");
		
		for (Task t : manager.getEpics()) {
			if (t.getId() == id) {
				code = 200;
				manager.deleteEpic(id);
				response = gson.toJson("Эпик с id: " + t.getId() + " удален.");
				break;
			}
		}
		writeResponse(code, response, exchange);
	}
	
	private static void deleteEpics(HttpExchange exchange) throws IOException {
		manager.deleteEpics();
		writeResponse(200, gson.toJson("Все эпики удалены."), exchange);
	}
	
	private static void getSubtask(HttpExchange exchange) throws IOException {
		final String[] querySplit = exchange.getRequestURI().getQuery().split("=");
		final int id = Integer.parseInt(querySplit[1]);
		
		int code = 404;
		String response = gson.toJson("Подзадачи с id: " + id + " не существует.");
		
		for (Task t : manager.getSubtasks()) {
			if (t.getId() == id) {
				code = 200;
				response = gson.toJson(manager.getSubtask(id));
				break;
			}
		}
		writeResponse(code, response, exchange);
	}
	
	private static void addOrUpdateSubtask(HttpExchange exchange) throws IOException {
		InputStream inputStream = exchange.getRequestBody();
		String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
		
		Subtask sub = gson.fromJson(body, Subtask.class);
		
		int code = 500;
		String response = gson.toJson("В настоящий момент сервер не может выполнить эту задачу.");
		
		if (sub.getId() == 0) {
			manager.addSubtask(sub);
			code = 200;
			response = gson.toJson("Подзадача добавлена с id: " + sub.getId());
		} else {
			for (Task t : manager.getSubtasks()) {
				if (t.getId() == sub.getId()) {
					manager.updateSubtask(sub);
					code = 200;
					response = gson.toJson("Подзадача оптимизирована.");
					break;
				} else {
					code = 404;
					response = gson.toJson("Подзадача с id: " + sub.getId() + " не найдена.");
				}
			}
		}
		writeResponse(code, response, exchange);
	}
	
	private static void deleteSubtask(HttpExchange exchange) throws IOException {
		final String[] querySplit = exchange.getRequestURI().getQuery().split("=");
		final int id = Integer.parseInt(querySplit[1]);
		
		int code = 404;
		String response = gson.toJson("Подзадача с id: " + id + " не существует.");
		
		for (Task t : manager.getSubtasks()) {
			if (t.getId() == id) {
				code = 200;
				manager.deleteSubtask(id);
				response = gson.toJson("Подзадача с id: " + t.getId() + " удалена.");
				break;
			}
		}
		writeResponse(code, response, exchange);
	}
	
	private static void deleteSubtasks(HttpExchange exchange) throws IOException {
		manager.deleteSubtasks();
		writeResponse(200, gson.toJson("Все подзадачи удалены."), exchange);
	}
	
	private static void getEpicSubtasks(HttpExchange exchange) throws IOException {
		final String[] querySplit = exchange.getRequestURI().getQuery().split("=");
		final int id = Integer.parseInt(querySplit[1]);
		
		int code = 404;
		String response = gson.toJson("Эпик с id: " + id + " не существует.");
		
		for (Task t : manager.getEpics()) {
			if (t.getId() == id) {
				code = 200;
				if (manager.getEpicSubtasks(id).isEmpty()) {
					response = gson.toJson("Список подзадач эпика пуст.");
				} else {
					response = gson.toJson(manager.getEpicSubtasks(id));
				}
				break;
			}
		}
		writeResponse(code, response, exchange);
	}
	
	private static void writeResponse(int code, String response, HttpExchange exchange) throws IOException {
		exchange.sendResponseHeaders(code, 0);
		try (OutputStream os = exchange.getResponseBody()) {
			os.write(response.getBytes());
		}
	}
}
