package ch.heig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

public class Config {
	String[] victims;
	FakeMessage[] messages;

	public Config() {
		load(readFile(Main.VICTIMS_FILE), readFile(Main.MESSAGES_FILE));
	}

	// For easy testing
	Config(String victimsJson, String messagesJson) {
		load(victimsJson, messagesJson);
	}

	private void load(String victimsJson, String messagesJson) {
		if (victimsJson == null || victimsJson.isEmpty() || messagesJson == null || messagesJson.isEmpty())
			throw new RuntimeException();

		// Parse content with the help of Gjson library
		Gson gson = new Gson();
		System.out.println("victimsJson: " + victimsJson);
		victims = gson.fromJson(victimsJson, String[].class);
		System.out.println("messagesJson: " + messagesJson);
		messages = gson.fromJson(messagesJson, FakeMessage[].class);
	}

	// Read a given file entirely
	private String readFile(String path) {
		String content = "";
		try (BufferedReader in = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(path),
						StandardCharsets.UTF_8))) {
			String line;
			while ((line = in.readLine()) != null) {
				content += line + '\n';
			}
		} catch (Exception e) {
			System.out.println("Error in reading file '" + path + "': " + e);
			return null;
		}
		return content;
	}

	// Validate the extracted victims and messages and return an array of errors
	// in case they exists
	public String[] validate() {
		// TODO: do the checks (see more in tests)
		return new String[] {};
	}
}
