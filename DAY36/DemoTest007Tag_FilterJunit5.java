package DAY36;/* Tag and Filter - tag test cases using @tag annotation.
Tags are labels to categorize the test cases.
Filter - used to filter and run the test cases by using the Tags.
*/

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class DemoTest007Tag_FilterJunit5 {


    @Test
    @Tag("slow")
    void slowTest() {
        assertTrue(true, "slow test method");
    }


    @Test
    @Tag("fast")
    void fastTest() {
        assertTrue(true, "fast test method");
    }


    @Test
    @Tag("fast")
    @Tag("integration")
    void fastIntegrationTest() {
        assertTrue(true, "fast integration test methid");
    }
}