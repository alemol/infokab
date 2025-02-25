package mx.geoint.Email;

import mx.geoint.Apis.Email.Emailer;
import org.junit.jupiter.api.Test;

public class EmailTest {
    @Test
    void sendEmail () {
        Emailer emailer = new Emailer();
        emailer.sendEmail("test.zip", "ucp.jose@gmail.com");
    }
}
