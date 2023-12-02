package ch.heig;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonSyntaxException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConfigTest {
	final static String from = "alice@localhost.com";
	final static String[] to = { "john@localhost.com", "bob@localhost.com" };

	@Test
	void loadConfigOnGivenJsonWorks() {
		var c = new Config("[\"hey@hey.com\",\"asdf@wwwww.com\",\"fff@ddd.com\"]",
				"[{\"subject\": \"hello there\", \"body\": \"hello body\"}," +
						"{\"subject\": \"hi\", \"body\": \"nice\\nto\\nmeet you\"}]");

		assertEquals(3, c.victims.length);
		assertEquals("hey@hey.com", c.victims[0]);

		assertEquals("hello there", c.messages[0].subject);
		assertEquals("hello body", c.messages[0].body);
		assertEquals(2, c.messages.length);
	}

	@Test
	void loadConfigManageErrors() {
		// Empty files
		assertThrows(RuntimeException.class, () -> {
			new Config("", null);
		});
		// Invalid JSON
		assertThrows(JsonSyntaxException.class, () -> {
			new Config("{blab invalid \"", "aajajajaja");
		});
	}

	@Test
	void configValidationRequiresAMinimumOfUniqueVictims() {
		// Just a victim
		var c = new Config("[\"john@example.com\"]",
				"[{\"subject\": \"hello there\",\"body\": \"hello there\"}]");

		assertArrayEquals(new String[] {
				"Only 1 unique victims given, a minimum of 5 unique emails is required !"
		}, c.validate().toArray());

		// 5 non unique victims are not valid
		var c2 = new Config(
				"[\"john@example.com\",\"john@example.com\",\"john@example.com\",\"john@example.com\",\"john@example.com\"]",
				"[{\"subject\": \"hello there\",\"body\": \"hello there\"}]");

		assertArrayEquals(new String[] {
				"All victims addresses must be unique..."
		}, c2.validate().toArray());
	}

	@Test
	void validationWorks() {
		var c = new Config(
				"[\"example.com\",\"a@com\",\"john@example.com\",\"dasdf@example.com\",\"gggww@example.com\"]",
				"[" +
						"{\"subject\": \"hello there\"}, " +
						"{\"body\": \"hello there\"}, " +
						"{\"unkown\": true}, " +
						"{\"subject\": \"hello there\",\"body\": \"\"}," +
						"{\"subject\": \"\",\"body\": \"hello there\"}" +
						"]");
		assertArrayEquals(new String[] {
				"Victim 1 (example.com) is not a valid email",
				"Victim 2 (a@com) is not a valid email",
				"Email 1 has no body",
				"Email 2 has no subject",
				"Email 3 has no subject",
				"Email 3 has no body",
				"Email 4 has an empty body",
				"Email 5 has an empty subject",
		}, c.validate().toArray());
	}
}
