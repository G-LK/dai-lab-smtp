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
import java.util.Random;

public class Sender {
	ArrayList<Email> emails;
	Config config;
	int groupsNumber;

	// TODO
	public Sender(int groupsNumber) {
		if (groupsNumber < 1)
			throw new RuntimeException();
		this.groupsNumber = groupsNumber;
		loadConfig();
	}

	public boolean prepare() {
		return generateEmails(true);
	}

	// TODO:
	public boolean connectAndSend() {
		// TODO: open a socket on port Main.SMTP_PORT
		// print error message in case of error and return false to exit

		boolean result = false;
		try (
				Socket socket = new Socket("localhost", Main.SMTP_PORT);) {
			result = connectAndSend(socket);
		} catch (Exception e) {
			System.out.println("failed to connect to localhost");

		}
		return result;
	}

	boolean connectAndSend(Socket socket) {
		boolean result = false;
		if (socket == null)
			return false;
		try {

			BufferedReader in = new BufferedReader(
					new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			BufferedWriter out = new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
			// Consume first line of server introduction before anything else
			consumeLines(in);

			Thread.sleep(400);
			writeLogAndConsumeLines(out, in, "ehlo localhost\r\n");

			result = sendEmails(in, out);

			writeLogAndConsumeLines(out, in, "quit\r\n");

			out.close();
		} catch (IOException e) {
			System.out.println("Failed to write message ehlo localhost to socket");
		} catch (InterruptedException e) {
			System.out.println("Sleep failed");
		}
		return result;
	}

	boolean generateEmails(boolean printGeneratedList) {
		emails = new ArrayList<Email>(groupsNumber);
		FakeMessage msg;
		String[] to;
		String from;
		SecureRandom random = new SecureRandom();

		for (int i = 0; i < groupsNumber; i++) {

			int randomMessageIndex = random.nextInt(config.messages.length);
			msg = config.messages[randomMessageIndex];
			int randomVictimsNumber = random.nextInt(
					Main.MAX_VICTIMS_PER_GROUP - Main.MIN_VICTIM_PER_GROUP)
					+ Main.MIN_VICTIM_PER_GROUP; // generate a number between in range [min;max]
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

	public ArrayList<Email> getEmails() {
		return emails;
	}

	private boolean sendEmails(BufferedReader in, BufferedWriter out) {
		try {
			int counter = 0;
			for (var e : emails) {
				System.out.println("\n>>>> Sending email " + (++counter));
				for (var line : e.toRawEmailHeaderLines()) {
					writeLogAndConsumeLines(out, in, line);
				}
				writeLogAndConsumeLines(out, in, e.toRawEmailTextData());
			}
		} catch (Exception e) {
			System.out.println("Some email sending has failed...");
			return false;
		}

		return true;
	}

	private void consumeLines(BufferedReader in) {
		try {
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println("S: " + line);
				if (line.charAt(3) != '-') { // 250-
					break;
				}
			}
		} catch (IOException e) {
			System.out.println("cannot read line correctly");
		}
	}

	private void writeLogAndConsumeLines(BufferedWriter out, BufferedReader in, String text)
			throws IOException, InterruptedException {
		System.out.print("C: " + text);
		out.write(text); // it already contains the end of lines chars
		out.flush();
		consumeLines(in);
		Thread.sleep(100); // a short sleep just to be able to avoid instant finish in the console
	}

	private void loadConfig() {
		config = new Config();
		LinkedList<String> errors = config.validate();
		if (errors.size() > 0) {
			System.out.println("Some validation checks on configuration files:");
			for (var err : errors) {
				System.out.println("- " + err);
			}
			throw new RuntimeException("Configuration error");
		}
	}

}
