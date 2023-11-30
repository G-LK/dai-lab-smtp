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
		return "";
	}

	public String[] toRawEmailHeaderLines() {
		// TODO
		return new String[] {};
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
