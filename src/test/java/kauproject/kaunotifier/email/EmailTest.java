package kauproject.kaunotifier.email;

import kauproject.kaunotifier.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailTest {
    @Autowired
    private EmailService emailService;
    @Test
    void serviceTest() {
        String expectation = "ses";
        Assertions.assertThat(emailService.getServiceName()).isEqualTo(expectation);
    }

    @Test
    void sendTest() {
        String sender = "no-reply@kau-notifier.site";
        String recipient = "choiyool00@gmail.com";
        String subject = "Test Email!";
        String bodyHTML = "<html>" + "<head></head>" + "<body>" + "<h1>Hello!</h1>"
                + "<p> See the list of customers.</p>" + "</body>" + "</html>";

        emailService.sendMail(sender, recipient, subject, bodyHTML);
    }

    @Test
    void sendTemplateTest() {
        String sender = "no-reply@kau-notifier.site";
        String recipient = "choiyool00@gmail.com";
        JSONObject object = new JSONObject();
        object.put("name", "최율");
        object.put("code", "123456");
        emailService.sendVerificationCodeEmail(sender, recipient, object);
    }

    @Test
    void json() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "최율");
        jsonObject.put("code", 123456);

        System.out.println(jsonObject.toString());
    }
}
