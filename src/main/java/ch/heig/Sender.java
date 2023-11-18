package ch.heig;

public class Sender {
	final int groupsNB;
	Email[] emails;
	String messagesFilePath;
	String victimsFilePath;
	String[] victimsList;

	// TODO
	public Sender(int groupsNB, String messagesFilePath, String victimsFilePath) {
		this.groupsNB = groupsNB;
		this.messagesFilePath = messagesFilePath;
		this.victimsFilePath = victimsFilePath;
	}

	public void prepare() {
		// TODO extractions
		generateEmails();
	}

	public String[] getVictimsList() {
		return victimsList;
	}

	public String[] getMessages() {
		return victimsList;
	}

	// TODO:
	public void send() {
		sendEmails();
	}

	void generateEmails() {

	}

	public Email[] getEmails() {
		return emails;
	}

	public void sendEmails() {

	}
}
