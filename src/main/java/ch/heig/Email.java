package ch.heig;

public class Email {
	private String subject;
	private String body;
	private String from;
	private String[] to;

	public Email(String subject, String body, String from, String[] to) {
		this.subject = subject;
		this.body = body;
		this.from = from;
		this.to = to;
	}

	public String toRawEmailTextData() {
		// TODO
		return "From: <" + from + ">" + 
				"\r\nTo: <" + to + ">" +
				"\r\nSubject:" + subject + 
				"\r\n\r\n" + body + 
				"\r\n.\r\n";
	}

	public String[] toRawEmailHeaderLines() {
		// TODO
		String[] result = {
				"MAIL FROM:<" + from + ">\r\n",
				"RCPT TO:<" + to[0] + ">\r\n",
				"RCPT TO:<" + to[1] + ">\r\n",
				"DATA\r\n" };

		return result;
	}

	public String getSubject() {
		return subject;
	}

	public String getBody() {
		return body;
	}

	public String getFrom() {
		return from;
	}

	public String[] getTo() {
		return to;
	}

}
