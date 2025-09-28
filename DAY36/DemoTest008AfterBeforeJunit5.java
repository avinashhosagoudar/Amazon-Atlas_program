package DAY36;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DemoTest008AfterBeforeJunit5 {

    private int val1;
    private int val2;

    @BeforeAll
    static void setupBeforeAll() {
        System.out.println("Run before all tests");
    }

    @AfterAll
    static void cleanupAfterAll() {
        System.out.println("Run after all tests");
    }

    @BeforeEach
    void setupBeforeEach() {
        System.out.println("Run before each test");
        val1 = 10;
        val2 = 5;
    }

    @AfterEach
    void cleanupAfterEach() {
        System.out.println("Run after each test");
    }

    @Test
    void testSum() {
        System.out.println("Testing sum of two numbers");
        int result = val1 + val2;
        assertEquals(15, result, "Actual sum should be 15");
    }

    @Test
    void testProduct() {
        System.out.println("Testing product of two numbers");
        int result = val1 * val2;
        assertEquals(50, result, "Actual product should be 50");
    }

    @Test
    void testSampleMethod() {
        System.out.println("Sample test method");
    }
}