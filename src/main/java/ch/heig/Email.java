package ch.heig;

public class Email {
	private String subject;
	private String body;
	private String from;
	private String[] to;

	public Email(String subject, String body) {
		this.subject = subject;
		this.body = body;
	}

	public String toString() {
		// TODO
		return "";
	}

	public void parse(String json) {
		// TODO
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
