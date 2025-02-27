package mx.geoint.Email;

import com.google.api.services.gmail.Gmail;
import mx.geoint.Apis.Email.Emailer;
import mx.geoint.Apis.Email.GmailSender;
import org.junit.jupiter.api.Test;

import javax.mail.internet.MimeMessage;

public class EmailTest {
    @Test
    void sendEmail () {

        try {
            GmailSender gmailSender = new GmailSender();

            MimeMessage email = gmailSender.createEmail("ucp.jose@gmail.com", "test.zip");

            // Enviar el mensaje
            gmailSender.sendMessage("me", email);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
