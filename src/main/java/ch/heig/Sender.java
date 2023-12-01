package ch.heig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.Random;

public class Sender {
	ArrayList<Email> emails;
	Config config;
	int groupsNumber;
	private static Random random = new Random(42);

	// TODO
	public Sender(int groupsNumber) {
		if (groupsNumber < 1)
			throw new RuntimeException();
		this.groupsNumber = groupsNumber;
		loadConfig();
	}

	public boolean prepare() {
		return generateEmails();
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
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			out.write("ehlo localhost\r\n");
			consumeLines(in);

			out.flush();
			result = sendEmails(in, out);

			out.write("quit\r\n");
			consumeLines(in);

			out.close();
		} catch (IOException e) {
			System.out.println("Failed to write message ehlo localhost to socket");
		}
		return result;
	}

	boolean generateEmails() {
		emails = new ArrayList<Email>(groupsNumber);
		for (int i = 0; i < groupsNumber; i++) {
			int randomMessageIndex = random.nextInt(config.messages.length);
			FakeMessage msg = config.messages[randomMessageIndex];
			int randomVictimsNumber = random.nextInt(Main.MAX_VICTIMS_PER_GROUP - Main.MIN_VICTIM_PER_GROUP)
					+ Main.MIN_VICTIM_PER_GROUP;
			String[] to = new String[randomVictimsNumber];
			String from;
			
			Collections.shuffle(Arrays.asList(config.victims));
			from = config.victims[0];
			for (int j = 1; j < randomVictimsNumber; j++) {
				to[j] = config.victims[j];
			}
			
			emails.add(new Email(msg.subject, msg.body, from, to));
		}
		return true;
	}

	public ArrayList<Email> getEmails() {
		return emails;
	}

	private boolean sendEmails(BufferedReader in, BufferedWriter out) {
		try {
			for (var e : emails) {
				for (var line : e.toRawEmailHeaderLines()) {
					out.write(line);
					consumeLines(in);
				}
				out.write(e.toRawEmailTextData());
				consumeLines(in);
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
				if (line.charAt(3) != '-') { // 250-
					break;
				}
			}
		} catch (IOException e) {
			System.out.println("cannot read line correctly");
		}
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
