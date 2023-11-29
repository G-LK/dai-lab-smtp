package ch.heig;

public class Main {
	public static void main(String[] args) {
		Sender sender = new Sender();
		System.out.println("\nStarting fake emails campaign...");
		System.out.println(
				"Preparing the emails, senders and recipients. (Randomly generated from data/messages.json and data/victims.txt)");
		sender.prepare();
		System.out.println("\nPreparation done. Starting sending...");
		sender.send();
		System.out.println("\nCampaign done !");
	}
}
