package ch.heig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class Sender {
	ArrayList<Email> emails;
	Config config;
	int groupsNumber;

	// Create a sender dedicated to send a certain amount of emails (1 email/group)
	public Sender(int groupsNumber) {
		if (groupsNumber < 1)
			throw new RuntimeException();
		this.groupsNumber = groupsNumber;
		loadConfig();
	}

	// Prepare the campaign
	public boolean prepare() {
		return generateEmails(true);
	}

	// Generate groupsNumber emails with random
	boolean generateEmails(boolean printGeneratedList) {
		emails = new ArrayList<Email>(groupsNumber);
		FakeMessage msg;
		String[] to;
		String from;
		SecureRandom random = new SecureRandom();

		for (int i = 0; i < groupsNumber; i++) {

			int randomMessageIndex = random.nextInt(config.messages.length);
			msg = config.messages[randomMessageIndex];

			// Generate a number between in range [min;max]
			int randomVictimsNumber = random.nextInt(
					Main.MAX_VICTIMS_PER_GROUP - Main.MIN_VICTIM_PER_GROUP + 1)
					+ Main.MIN_VICTIM_PER_GROUP;
			to = new String[randomVictimsNumber - 1];

			// Shuffle victims list before taking the first ones so we have a random
			Collections.shuffle(Arrays.asList(config.victims));
			from = config.victims[0];
			for (int j = 1; j < randomVictimsNumber; j++) {
				to[j - 1] = config.victims[j];
			}

			emails.add(new Email(msg.subject, msg.body, from, to));
		}
		if (!printGeneratedList)
			return true;

		System.out.println("Generated emails:");
		for (var e : emails) {
			System.out.println("- " + e);
		}
		return true;
	}

	// Connect to the SMTP server and send emails
	public boolean connectAndSend() {
		boolean result = false;
		try (
				Socket socket = new Socket(Main.SMTP_HOST, Main.SMTP_PORT);) {

			try {
				BufferedReader in = new BufferedReader(
						new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
				BufferedWriter out = new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

				// Consume first line of server introduction before anything else
				consumeLines(in);

				// Sleep to avoid "You talk to soon" server error
				Thread.sleep(400);

				// Init emails exchange
				writeThenLogAndConsumeLines(out, in, "ehlo localhost\r\n");

				result = sendEmails(in, out);

				// End of the emails exchange
				writeThenLogAndConsumeLines(out, in, "quit\r\n");

				out.close();
			} catch (IOException e) {
				System.out.println("Failed to write messages on socket");
				result = false;
			} catch (InterruptedException e) {
				System.out.println("Sleep failed");
				result = false;
			}
		} catch (Exception e) {
			System.out.println("failed to connect to " + Main.SMTP_HOST);
		}
		return result;
	}

	// Send generated emails one after the other
	private boolean sendEmails(BufferedReader in, BufferedWriter out) {
		try {
			int counter = 0;
			for (var e : emails) {
				System.out.println("\n>>>> Sending email " + (++counter));
				for (var line : e.toRawEmailHeaderLines()) {
					writeThenLogAndConsumeLines(out, in, line);
				}
				writeThenLogAndConsumeLines(out, in, e.toRawEmailTextData());
			}
		} catch (Exception e) {
			System.out.println("Some email sending has failed...");
			return false;
		}

		return true;
	}

	// Write something on output stream, log the message, and consume the lines back
	private void writeThenLogAndConsumeLines(BufferedWriter out, BufferedReader in, String text)
			throws IOException, InterruptedException {
		out.write(text); // it already contains the end of lines chars
		System.out.print("C: " + text);
		out.flush();
		consumeLines(in);
		Thread.sleep(100); // a short sleep just to be able to avoid instant finish in the console
	}

	// Read lines sent by the server until we find a line without any dash at char 3
	// (after the 3 digits there a dash or a whitespace)
	// We log the lines but we actually ignore the content of them because this
	// is out of scope for this labo
	private void consumeLines(BufferedReader in) throws IOException {
		String line;
		while ((line = in.readLine()) != null) {
			System.out.println("S: " + line);
			if (line.charAt(3) != '-') { // 250-
				break;
			}
		}
	}

	// Load the config, validate it and show errors if necessary
	private void loadConfig() {
		config = new Config();
		LinkedList<String> errors = config.validate();
		if (errors.size() > 0) {
			System.out.println("Some validation on configuration files failed:");
			for (var err : errors) {
				System.out.println("- " + err);
			}
			throw new RuntimeException("Configuration error");
		}
	}

	// A getter on emails used mostly for testing
	public ArrayList<Email> getEmails() {
		return emails;
	}
}
