package ch.heig;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		victims = gson.fromJson(victimsJson, String[].class);
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
	public LinkedList<String> validate() {
		// TODO: do the checks (see more in tests)
		LinkedList<String> errors = new LinkedList<>();
		Pattern emailRegex = Pattern.compile("^[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)+$");
		for (int i = 0; i < victims.length; i++) {
			Matcher m = emailRegex.matcher(victims[i]);
			if (m.find() == false) {
				errors.add("Victim " + (i + 1) + " (" + victims[i] + ") is not a valid email");
			}
		}

		for (int i = 0; i < messages.length; i++) {
			String subject = messages[i].subject;
			String body = messages[i].body;

			if (subject == null)
				errors.add("Email " + (i + 1) + " has no subject");
			else if (subject.isEmpty())
				errors.add("Email " + (i + 1) + " has an empty subject");

			if (body == null)
				errors.add("Email " + (i + 1) + " has no body");
			else if (body.isEmpty())
				errors.add("Email " + (i + 1) + " has an empty body");

		}

		return errors;
	}
}
