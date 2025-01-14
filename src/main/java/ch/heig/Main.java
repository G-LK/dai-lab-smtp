package ch.heig;

public class Main {
	// Constants of the program
	final static String SMTP_HOST = "localhost";
	final static int SMTP_PORT = 1025;
	final static int MIN_VICTIM_PER_GROUP = 2;
	final static int MAX_VICTIMS_PER_GROUP = 5;
	final static String VICTIMS_FILE = "victims.json";
	final static String MESSAGES_FILE = "messages.json";

	public static void main(String[] args) {
		// Making sure the number of group is given and valid
		if (args.length < 1) {
			System.out.println("Erreur: le premier paramètre doit être le nombre de groupes.");
			System.exit(1);
		}
		int groupsNumber = 0;
		try {
			groupsNumber = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.out.println("Erreur: Le nombre de groupes fourni n'est pas un nombre entier.");
			System.exit(1);
		}
		if (groupsNumber <= 0) {
			System.out.println("Erreur: le nombre de groupe fourni doit être strictement positif.");
			System.exit(1);
		}

		// Starting sender process
		try {
			Sender sender = new Sender(groupsNumber);
			System.out.println("\n>> Starting fake emails campaign...");
			System.out.println(
					"\n>> Preparing the " + groupsNumber
							+ " emails, senders and recipients. (With messages.json and victims.json)");
			if (!sender.prepare())
				exitWithFailures();
			System.out.println(
					"\n>> Preparation done. Establishing connection on " + Main.SMTP_HOST + ":" + SMTP_PORT
							+ " and starting campaign...");
			if (!sender.connectAndSend())
				exitWithFailures();
		} catch (Exception e) {
			System.out.println("Exception: " + e);
			exitWithFailures();
		}

		System.out.println("\n>> Campaign done !");
	}

	public static void exitWithFailures() {
		System.out.println("\n>> Fin du programme pour cause d'erreurs...");
		System.exit(1);
	}
}
