package DAY36;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestCase02 {

    @Test
    @Tags({ @Tag("firstPriority") })  // way to use @Tags
    void testMethod01() {
        assertEquals(2 + 3, 5);
    }

    @Test
    @Tag("firstPriority")
    void runTestcase02() {
        assertEquals("Hello".toUpperCase(), "HELLO");
    }

    @Test
    @Tag("fastTag")   // Added @Test here
    void testMethod03() {
        assertEquals(10 - 4, 6);
    }

    @Test
    @Tag("slowTag")
    void runTestcase04() {
        assertEquals(9 / 3, 3);
    }
}