package ch.heig;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.*;
import com.google.gson.Gson;

public class Sender {
	Email[] emails;
	Config config;
	static final String CONFIG_FILE = "config.json";

	// TODO
	public Sender() {
		loadConfig();
	}

	public void prepare() {
		// TODO extractions
		generateEmails();
	}

	// TODO:
	public void send() {
		sendEmails();
	}

	void generateEmails() {

	}

	public Email[] getEmails() {
		return emails;
	}

	public void sendEmails() {

	}

	public void loadConfig() {
		try (BufferedReader in = new BufferedReader(
				new InputStreamReader(new FileInputStream(CONFIG_FILE), StandardCharsets.UTF_8))) {
			String line;
			String json = "";

			while ((line = in.readLine()) != null) {
				json += line + '\n';
			}

			Gson gson = new Gson();
			config = gson.fromJson(json, Config.class);

		} catch (Exception e) {
			System.out.println("Error in JSON file reading: " + e);
		}

	}
}
