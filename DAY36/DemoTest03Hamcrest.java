package DAY36;


import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;

public class DemoTest03Hamcrest {

    @Test
    void checkHasProperties() {
        Customer customer = new Customer("John", "Abraham");

        // check if properties exist
        assertThat(customer, hasProperty("fname"));
        assertThat(customer, hasProperty("lname"));

        // check if property has expected value
        assertThat(customer, hasProperty("fname", equalTo("John")));
        assertThat(customer, hasProperty("lname", equalTo("Abraham")));
    }
}
