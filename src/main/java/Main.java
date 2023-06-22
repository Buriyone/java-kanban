package main.java;

import main.java.server.HttpTaskServer;
import main.java.server.KVServer;

import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		new KVServer().start();
		new HttpTaskServer().start();
	}
}