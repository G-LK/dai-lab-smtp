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
import java.util.IntSummaryStatistics;

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
		// TODO: check socket is not null or return false
		// TODO: write a "ehlo localhost"
	 	// TODO: give in and out streams
		// TODO: when all messages have been sent, send a "quit"
		boolean result = false;
		if(socket != null) {
			try {

			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		
			
			 out.write("ehlo localhost" + "\r\n");
			 result = sendEmails(in, out);		
			 out.flush();
			 out.close();
			} catch (IOException e) {
				System.out.println("Failed to write message ehlo localhost to socket");
			}
	}
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
		try {
		String line;
		while((line = in.readLine()) != null) {
			if(line.charAt(3) != '-') { // 250-

			}
		}
	} catch(IOException e) {
		System.out.println("cannot read line correctly");
	}

		// TODO: loop on all emails
		for(var e : emails) {

		}
		// TODO: send each email intro line and
		// TODO: read lines until there is no dash after first number (you can ignore
		// the read lines' content)
		// TODO: then write the message content in one time
		// TODO: read line again
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
