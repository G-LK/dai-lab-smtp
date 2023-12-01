package ch.heig;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class EmailTest {
	final static String from = "alice@localhost.com";
	final static String[] to = { "john@localhost.com", "bob@localhost.com" };
	final static String basicJsonMail = "{\"subject\":\"Ho\", \"body\":\"hello world\"}";
	final static String subject = "Ho";
	final static String body = "hello world";

	@Test
	void emailObjectSetup() {
		Email e = new Email(subject, body, from, to);
		assertEquals("Ho", e.getSubject());
		assertEquals("hello world", e.getBody());
		assertEquals(from, e.getFrom());
		assertEquals(to, e.getTo());
	}

	@Test
	void emailObjectSetupWithInvalidArgumentsFails() {
		// Make sure empty "to" array is refused
		assertThrows(RuntimeException.class, () -> {
			new Email(subject, body, from, new String[] {});
		});
		// Make sure empty "from" is refused
		assertThrows(RuntimeException.class, () -> {
			new Email(subject, body, "", to);
		});
		// Make sure empty "subject" is refused
		assertThrows(RuntimeException.class, () -> {
			new Email("", body, from, to);
		});
		// Make sure empty "body" is refused
		assertThrows(RuntimeException.class, () -> {
			new Email(subject, "", from, to);
		});
	}

	@Test
	void bodyUseCorrectLineEndings() {
		// Make sure that \n are converted to \r\n
		Email e = new Email("Ho", "hello\n ... \n world", from, to);
		assertEquals("Ho", e.getSubject());
		assertEquals("hello\r\n ... \r\n world", e.getBody());
	}

	@Test
	void toRawEmailTextDataWorks() {
		Email e = new Email("Ho", "hello\n ... \n world\nThis is a fantastic day !!", from, to);

		String expectedRawDataSection = "From: <" + from + ">" +
				"\r\nTo: <" + to[0] + ">, <" + to[1] + ">" +
				"\r\nSubject: " + "=?utf-8?B?" + "SG8=" + "?=" +
				"\r\nContent-Type: text/plain; charset=utf-8" + // line always present
				"\r\n" +
				"\r\nhello\r\n ... \r\n world\r\nThis is a fantastic day !!" +
				"\r\n.\r\n";
		assertEquals(expectedRawDataSection, e.toRawEmailTextData());
	}

	@Test
	void toRawEmailTextDataWithUTF8SubjectAndBody() {
		Email e = new Email("hello lion 🦁🦁 ó¿¡á", "🦁🦁🦁🦁🦁🍿🍿🍿🍿🍿🍿🍿", from, to);

		String expectedRawDataSection = "From: <" + from + ">" +
				"\r\nTo: <" + to[0] + ">, <" + to[1] + ">" +
				"\r\nSubject: " + "=?utf-8?B?" + "aGVsbG8gbGlvbiDwn6aB8J+mgSDDs8K/wqHDoQ==" + "?=" +
				"\r\nContent-Type: text/plain; charset=utf-8" + // line always present
				"\r\n" +
				"\r\n🦁🦁🦁🦁🦁🍿🍿🍿🍿🍿🍿🍿" +
				"\r\n.\r\n";
		assertEquals(expectedRawDataSection, e.toRawEmailTextData());
	}

	@Test
	void toRawEmailHeaderLinesWorks() {
		Email e = new Email(subject, body, from, to);
		String[] expectedLines = {
				"MAIL FROM:<" + from + ">\r\n",
				"RCPT TO:<" + to[0] + ">\r\n",
				"RCPT TO:<" + to[1] + ">\r\n",
				"DATA\r\n" };
		assertArrayEquals(expectedLines, e.toRawEmailHeaderLines());
	}
}
