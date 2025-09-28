package DAY36;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JunitTest_Task002 {
    private static final Logger logger = LoggerFactory.getLogger(JunitTest_Task002.class);


    @Test
    void testcase01() {
        logger.info("start testing");
        String res = testcase02();
        logger.info("testing is done "+res);
    }
    private String testcase02() {
        logger.info(" we are in testcase02");


        try{
            Thread.sleep(10);
        }catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
            logger.error("execution got interruped", ex);
        }
        return "success msg";
    }
}
