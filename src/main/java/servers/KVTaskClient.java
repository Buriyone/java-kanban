package main.java.servers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
	private final String apiToken;
	private final String url;
	public KVTaskClient(String url) {
		this.url = url;
		this.apiToken = register(url);
	}
	
	private String register(String url) {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url + "register"))
				.header("Accept", "application/json")
				.GET()
				.build();
		
		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() == 200) {
				return response.body();
			} else {
				System.out.println("Что-то пошло не так, сервер вернул код: " + response.statusCode());
			}
		} catch (IOException | InterruptedException e) {
			System.out.println("Произошла ошибка при формировании запроса регистрации.");
		}
		return null;
	}
	
	public void put(String key, String json) {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken))
				.header("Accept", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(json))
				.build();
		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() != 200) {
				System.out.println("Что-то пошло не так, сервер вернул код: " + response.statusCode());
			}
		} catch (IOException | InterruptedException e) {
			System.out.println("Произошла ошибка при формировании запроса сохранения.");
		}
	}
	
	public String load(String key) {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken))
				.header("Accept", "application/json")
				.GET()
				.build();
		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() == 200) {
				return response.body();
			} else {
				System.out.println("Что-то пошло не так, сервер вернул код: " + response.statusCode());
				return String.valueOf(response.statusCode());
			}
		} catch (IOException | InterruptedException e) {
			System.out.println("Произошла ошибка при формировании запроса восстановления.");
		}
		return null;
	}
}
