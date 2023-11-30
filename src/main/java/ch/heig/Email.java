package ch.heig;

import java.util.Base64;

public class Email {
	private String subject;
	private String body;
	private String from;
	private String[] to;
	private final static String CONTENT_TYPE = "Content-Type: text/plain; charset=utf-8";

	public Email(String subject, String body, String from, String[] to) {
		if (to == null || to.length == 0) {
			throw new RuntimeException("cannot create empty email");
		}

		if (from == null || from.isEmpty()) {
			throw new RuntimeException("cannot create email without sender");
		}

		if (subject == null || subject.isEmpty()) {
			throw new RuntimeException("cannot create email without subject");
		}

		if (body == null || body.isEmpty()) {
			throw new RuntimeException("cannot send email without a body");
		}
		this.subject = subject;
		this.body = body;
		this.from = from;
		this.to = to;
	}

	public String toRawEmailTextData() {
		// TODO

		String itTo = "";
		for (int i = 0; i < to.length; i++) {
			if (i > 0) {
				itTo += ", ";
			}
			itTo += "<" + to[i] + ">";
		}

		String result = "From: <" + from + ">" +
				"\r\nTo: " + itTo +
				"\r\nSubject:" + "=?utf-8?Q?" +
				getBase64EncodedSubject() + "?=" +
				"\r\n" + CONTENT_TYPE +
				"\r\n\r\n" + getBody() +
				"\r\n.\r\n";

		return result;
	}

	public String[] toRawEmailHeaderLines() {
		// TODO
		int count = 0;
		String[] lignes = new String[to.length + 2];
		lignes[0] = "MAIL FROM:<" + from + ">\r\n";
		++count;
		for (int i = 0; i < to.length; i++) {
			lignes[count] = "RCPT TO:<" + to[i] + ">\r\n";
			count++;
		}
		lignes[count] = "DATA\r\n";

		return lignes;
	}

	public String getSubject() {
		return subject;
	}

	public String getBase64EncodedSubject() {
		return new String(Base64.getEncoder().encode(subject.getBytes()));
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

}
