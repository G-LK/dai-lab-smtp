package ch.heig;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConfigTest {

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
}
