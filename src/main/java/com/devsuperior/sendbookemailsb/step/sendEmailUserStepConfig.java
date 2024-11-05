package com.devsuperior.sendbookemailsb.step;

import com.devsuperior.sendbookemailsb.domain.User;
import com.devsuperior.sendbookemailsb.domain.UserBookLoan;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class sendEmailUserStepConfig {

    private int chunkSize;

    @Autowired
    @Qualifier("transactionManagerApp")
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public Step sendEmailUserStep(ItemReader<UserBookLoan> readUsersWithLoansCloseToReturnReader,
            JobRepository jobRepository) {
        return new StepBuilder("sendEmailUserStep", jobRepository)
                .<UserBookLoan, UserBookLoan>chunk(1, platformTransactionManager)
                .reader(readUsersWithLoansCloseToReturnReader)
                .build();
    }
}
