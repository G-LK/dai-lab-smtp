package ch.heig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Sender {
	ArrayList<Email> emails = new ArrayList<>();
	Config config;
	int groupsNumber;

	// TODO
	public Sender(int groupsNumberString) {
		if (groupsNumberString < 1)
			throw new RuntimeException();
		loadConfig();
	}

	public boolean prepare() {
		return generateEmails();
	}

	// TODO:
	public boolean connectAndSend() {
		// TODO: open a socket on port Main.SMTP_PORT
		// print error message in case of error and return false to exit
		return connectAndSend(null /* socket */);
	}

	boolean connectAndSend(Socket socket) {
		// TODO: check socket is not null or return false
		// TODO: write a "ehlo localhost"
		boolean result = sendEmails(null, null); // TODO: give in and out streams
		// TODO: when all messages have been sent, send a "quit"
		return result;
	}

	boolean generateEmails() {
		return true;
	}

	public ArrayList<Email> getEmails() {
		return emails;
	}

	private boolean sendEmails(BufferedReader in, BufferedWriter out) {
		// TODO: read lines until there is no dash after first number
		// TODO: loop on all emails
		// TODO: send each email intro line and
		// TODO: read lines until there is no dash after first number (you can ignore
		// the read lines' content)
		// TODO: then write the message content in one time

		return true;
	}

	private void loadConfig() {
		config = new Config();
		String[] errors = config.validate();
		if (errors.length > 0) {
			System.out.println("Some validation checks on configuration files:");
			for (var err : errors) {
				System.out.println("- " + err);
			}
			throw new RuntimeException("Configuration error");
		}
	}

}
