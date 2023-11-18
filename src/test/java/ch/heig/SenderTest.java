package ch.heig;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SenderTest {

	@Test
	void prepareCorrectlyExtractsVictimsAndMessages() {
		// TODO: should have a single config file ? including the N parameter ?
		Sender s = new Sender(5, "data/messages.json", "data/victims.txt");
		assertEquals(new String[] {}, s.getVictimsList());
		s.prepare();
		assertEquals(
				new String[] { "hey@example.com",
						"hello@example.com",
						"john@example.com",
						"alice@example.com",
						"luke@example.com",
						"ben@example.com" },
				s.getVictimsList());

		// TODO: test messages have been extracted
	}

	@Test
	void generateEmailsWorks() {
		Sender s = new Sender(5, "data/messages.json", "data/victims.txt");
		assertEquals(new Email[] {}, s.getEmails());
		s.prepare();
		s.generateEmails();
		Email[] emails = s.getEmails();
		for (var e : emails) {
			assertTrue(e.getTo().length >= 1);
			assertTrue(e.getTo().length <= 4);
		}

		//Add more tests related to README instructions...
	}
}
