package ch.heig;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.*;

public class Sender {
	final int groupsNB;
	Email[] emails;
	String messagesFilePath;
	String victimsFilePath;
	String[] victimsList;
	Config config;

	// TODO
	public Sender(int groupsNB, String messagesFilePath, String victimsFilePath) {
		this.groupsNB = groupsNB;
		this.messagesFilePath = messagesFilePath;
		this.victimsFilePath = victimsFilePath;
	}

	public void prepare() {
		// TODO extractions
		generateEmails();
	}

	public String[] getVictimsList() {
		return victimsList;
	}

	public String[] getMessages() {
		return victimsList;
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
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(json), StandardCharsets.UTF_8));

		String line;
		String json;

		while((line = in.readLine()) != null) {
			json += line + '\n';
		}
		
		config = gson.fromJson(json, Config.class);

	}
}
