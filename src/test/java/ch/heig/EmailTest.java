package ch.heig;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmailTest {
	final static String from = "alice@localhost.com";
	final static String[] to = { "john@localhost.com", "bob@localhost.com" };
	final static String basicJsonMail = "{\"subject\":\"Ho\", \"body\":\"hello world\"}";

	@Test
	void parseWorks() {
		Email e = new Email(basicJsonMail, from, to);
		assertEquals("Ho", e.getSubject());
		assertEquals("hello world", e.getBody());
		assertEquals(from, e.getFrom());
		assertEquals(to, e.getTo());
	}

	@Test
	void parseWithInvalidArgumentsFails() {
		// Make sure empty "to" array is refused
		assertThrows(RuntimeException.class, () -> {
			new Email(basicJsonMail, from, new String[] {});
		});
		// Make sure empty "from" is refused
		assertThrows(RuntimeException.class, () -> {
			new Email(basicJsonMail, "", to);
		});
	}

	@Test
	void parseUseCorrectLineEndings() {
		// Make sure that \n are converted to \r\n
		Email e = new Email("{\"subject\":\"Ho\", \"body\":\"hello\\n ... \\n world\\n\\n\"}", from, to);
		assertEquals("Ho", e.getSubject());
		assertEquals("hello\r\n ... \r\n world\r\n\r\n", e.getBody());
	}

	@Test
	void parseWithInvalidJsonOrInvalidEmailsFails() {
		// TODO: should we change the exception class ??
		// Invalid JSON
		assertThrows(RuntimeException.class, () -> {
			new Email("{blab invalid \"", from, to);
		});
		// Missing content
		assertThrows(RuntimeException.class, () -> {
			new Email("{\"subject\": \"\", \"body\":\"hello world\"}", from, to);
		});
		// Missing body field
		assertThrows(RuntimeException.class, () -> {
			new Email("{\"subject\": \"hello there\"}", from, to);
		});

		// Missing subject field
		assertThrows(RuntimeException.class, () -> {
			new Email("{\"body\": \"hello there\"}", from, to);
		});
	}

	@Test
	void toRawEmailTextDataWorks() {
		Email e = new Email("{\"subject\":\"Ho\", \"body\":\"hello world\\nThis is a fantastic day !!\"}", from, to);
		String expectedRawDataSection = "From: " + from +
				"\r\nTo: " + to +
				// "\r\n" + "Date: January 1st"; //TODO: decide on whether we use a date or not
				// since it is maybe not required
				"\r\nSubject:" + "Ho" +
				"\r\n" +
				"\r\nhello world\r\nThis is a fantastic day !!" +
				"\r\n\r\n.\r\n"; // TODO: one or 2 \n ??
		assertEquals(expectedRawDataSection, e.toRawEmailTextData());
	}

	@Test
	void toRawEmailHeaderLinesWorks() {
		
	}
}
