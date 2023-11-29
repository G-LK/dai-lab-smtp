package ch.heig;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SenderTest {
	@Test
	void generateEmailsWorks() {

		Sender s = new Sender();
		assertEquals(new Email[] {}, s.getEmails());
		s.prepare();
		s.generateEmails();
		Email[] emails = s.getEmails();
		for (var e : emails) {
			assertTrue(e.getTo().length >= 1);
			assertTrue(e.getTo().length <= 4);
		}

		// Add more tests related to README instructions...
	}

	@Test
	void configCanBeLoaded() {
		Sender s = new Sender();
		s.loadConfig();
		assertEquals(5, s.config.groupNumber);
		assertEquals(
				new String[] { "hey@example.com",
						"hello@example.com",
						"john@example.com",
						"alice@example.com",
						"luke@example.com",
						"ben@example.com" },
				s.config.addresses);

		// Make sure the first message is loaded (others are probably too)
		var e = new FakeMessage();
		e.subject = "Hey guys";
		e.body = "you won 100000$\nRegards\nThe US president.";
		assertEquals(e, s.config.messages[0]);
		assertEquals(4, s.config.messages.length);
	}
}
