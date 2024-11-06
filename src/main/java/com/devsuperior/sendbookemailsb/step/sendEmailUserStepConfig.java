package com.devsuperior.sendbookemailsb.step;

import com.devsuperior.sendbookemailsb.domain.User;
import com.devsuperior.sendbookemailsb.domain.UserBookLoan;
import com.sendgrid.helpers.mail.Mail;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class sendEmailUserStepConfig {


    @Autowired
    @Qualifier("transactionManagerApp")
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public Step sendEmailUserStep(ItemReader<UserBookLoan> readUsersWithLoansCloseToReturnReader,
            ItemProcessor<UserBookLoan, Mail> processLoanNotificationEmailProcessor,
            ItemWriter<Mail> sendUserRequestReturnWriter,
            JobRepository jobRepository) {
        return new StepBuilder("sendEmailUserStep", jobRepository)
                .<UserBookLoan, Mail>chunk(1, platformTransactionManager)
                .reader(readUsersWithLoansCloseToReturnReader)
                .processor(processLoanNotificationEmailProcessor)
                .writer(sendUserRequestReturnWriter)
                .build();
    }
}
