package ch.heig;

public class Email {
	private String subject;
	private String body;
	private String from;
	private String[] to;
	private final static String CONTENT_TYPE = "Content-Type: text/plain; charset=utf-8";

	public Email(String subject, String body, String from, String[] to) {
		this.subject = subject;
		this.body = body;
		this.from = from;
		this.to = to;
	}

	public String toRawEmailTextData() {
		// TODO
		
			String itTo = "";
			for(int i = 0; i < to.length; i++) {
				if(i > 0) {
					itTo += ", ";
				}
				itTo += "<" + to[i] + ">"; 
			}

			String result = 
				"From: <" + from + ">" + 
				"\r\nTo: " + itTo +
				"\r\nSubject:" + subject + 
				"\r\n" + CONTENT_TYPE +
				"\r\n\r\n" + body + 
				"\r\n.\r\n";
		
		
		return result;
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
