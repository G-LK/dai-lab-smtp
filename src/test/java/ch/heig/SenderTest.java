package ch.heig;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;

public class SenderTest {
	@Test
	void generateEmailsWorks() {

		Sender s = new Sender(5);
		//
		assertEquals(0, s.getEmails().size());

		s.prepare();
		// Run 100 times the email generation to make sure we can catch errors
		// even with random in play
		for (int i = 0; i < 100; i++) {
			s.generateEmails();
			var emails = s.getEmails();
			assertTrue(s.getEmails().size() > 0);
			HashSet<String> toList = new HashSet<>();
			for (var e : emails) {
				// Make sure all to adresses are unique
				for (var to : e.getTo()) {
					toList.add(to);
				}
				assertEquals(e.getTo().length, toList.size());

				// Assert from address is not part of the to adresses:
				assertFalse(toList.contains(e.getFrom()));

				// Check number of groups
				assertTrue(e.getTo().length >= 1); // at minimum one receiver
				assertTrue(e.getTo().length <= 4); // 5 at max minus the sender
			}
		}

		// Add more tests related to README instructions...
	}

	@Test
	void configCanBeLoaded() {
		Sender s = new Sender(5);
		s.loadConfig();
		assertArrayEquals(
				new String[] { "hey@example.com",
						"hello@example.com",
						"john@example.com",
						"alice@example.com",
						"luke@example.com",
						"ben@example.com" },
				s.config.victims);

		// Make sure the first message is loaded (others are probably too)

		assertEquals("Hey investors", s.config.messages[1].subject);
		assertEquals("I have a great deal for you\nRegards\nThe US president.", s.config.messages[1].body);
		assertEquals(4, s.config.messages.length);
	}
}
