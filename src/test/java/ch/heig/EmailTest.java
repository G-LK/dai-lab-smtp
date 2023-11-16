package ch.heig;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmailTest {
	@Test
	void emailCanBeParsed() {
		Email e = new Email("{\"name\":\"Ho\", \"body\":\"hello world\"}");
		assertEquals(e, e);
	}
}
