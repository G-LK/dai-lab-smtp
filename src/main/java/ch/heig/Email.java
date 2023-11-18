package ch.heig;

public class Email {
	private String subject;
	private String body;
	private String from;
	private String[] to;

	public Email(String json, String from, String[] to) {
		parse(json);
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

	private void parse(String json) {
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
