package DAY36;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;
// we  are given a bean ... string correctness
public class DemoTest02Hamcrest {
    @Test
    public void method1() {
        Customer customer = new Customer("John", "Abraham");
        String str = customer.toString();
        assertThat(customer, hasToString(str));
    }
}