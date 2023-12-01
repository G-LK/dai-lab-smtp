package ch.heig;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Email {
	private String subject;
	private String body;
	private String from;
	private String[] to;
	private final static String CONTENT_TYPE = "Content-Type: text/plain; charset=utf-8";

	public Email(String subject, String body, String from, String[] to) {
		if (to == null || to.length == 0)
			throw new RuntimeException("cannot create empty email");

		if (from == null || from.isEmpty())
			throw new RuntimeException("cannot create email without sender");

		if (subject == null || subject.isEmpty())
			throw new RuntimeException("cannot create email without subject");

		if (body == null || body.isEmpty())
			throw new RuntimeException("cannot send email without a body");

		this.subject = subject;
		this.body = body;
		this.from = from;
		this.to = to;
	}

	// Returns the raw email data (with headers and body)
	// to give after the DATA SMTP command
	public String toRawEmailTextData() {
		String itTo = "";
		for (int i = 0; i < to.length; i++) {
			if (i > 0)
				itTo += ", ";
			itTo += "<" + to[i] + ">";
		}

		String result = "From: <" + from + ">" +
				"\r\nTo: " + itTo +
				"\r\nSubject:" + "=?utf-8?B?" +
				getBase64EncodedSubject(subject) + "?=" +
				"\r\n" + CONTENT_TYPE +
				"\r\n\r\n" // empty line to mark the end of headers
				+ getBody() +
				"\r\n.\r\n"; // final line to indicate the end of the body

		return result;
	}

	// Returns the "header" lines of the SMTP commands before email content
	public String[] toRawEmailHeaderLines() {
		int count = 0;
		String[] lines = new String[to.length + 2];
		lines[count++] = "MAIL FROM:<" + from + ">\r\n";
		for (int i = 0; i < to.length; i++) {
			lines[count++] = "RCPT TO:<" + to[i] + ">\r\n";
		}
		lines[count] = "DATA\r\n";

		return lines;
	}

	public String getSubject() {
		return subject;
	}

	public String getBase64EncodedSubject(String input) {
		byte[] encodedBytes = Base64.getEncoder().encode(subject.getBytes(StandardCharsets.UTF_8));
		return new String(encodedBytes, StandardCharsets.UTF_8);
	}

	public String getBody() {
		return body.replace("\n", "\r\n");
	}

	public String getFrom() {
		return from;
	}

	public String[] getTo() {
		return to;
	}

	public String toString() {
		String result = "Email from " + from + " to ";
		for (var t : to) {
			result += t + ", ";
		}
		result += "\nContent:" + subject + "\n" + body;
		return result;
	}
}
