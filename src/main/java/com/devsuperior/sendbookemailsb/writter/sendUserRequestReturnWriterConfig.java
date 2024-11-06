package com.devsuperior.sendbookemailsb.writter;

import com.devsuperior.sendbookemailsb.domain.UserBookLoan;
import com.sendgrid.helpers.mail.Mail;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class sendUserRequestReturnWriterConfig {

    @Bean
    public ItemWriter<Mail> sendUserRequestReturnWriter() {
        return items -> items.forEach(System.out::println);
    }
}
