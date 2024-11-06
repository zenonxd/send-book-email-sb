package com.devsuperior.sendbookemailsb.writter;

import com.devsuperior.sendbookemailsb.domain.UserBookLoan;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class sendUserRequestReturnWriterConfig {

    private static Logger LOG = LoggerFactory.getLogger(sendUserRequestReturnWriterConfig.class);

    @Autowired
    private SendGrid sendGrid;

    @Bean
    public ItemWriter<Mail> sendUserRequestReturnWriter() {
        return items -> items.forEach(this::sendEmail);
    }

    private void sendEmail(Mail email) {
        LOG.info("Sending mail");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(email.build());
            LOG.info("[WRITER STEP] Send email to: " + email.build());
            Response response = sendGrid.api(request);
            if (response.getStatusCode() >= 400 && response.getStatusCode() <= 500) {
                LOG.error("Error sending email: " + response.getBody());
                throw new Exception(response.getBody());
            }
            LOG.info("Email sent! Status = " + response.getStatusCode());
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
