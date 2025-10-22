package be.ddd.infra.config;

import be.ddd.application.batch.CafeBeverageBatchService;
import be.ddd.application.batch.dto.LambdaBeverageDto;
import be.ddd.domain.entity.crawling.CafeBeverage;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Log4j2
@Profile("!prod")
public class BeverageBatchConfig {

    private final CafeBeverageBatchService cafeBeverageBatchService;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public ItemReader<LambdaBeverageDto> beverageReader() {
        List<LambdaBeverageDto> list = cafeBeverageBatchService.fetchAll();
        return new ListItemReader<>(list != null ? list : List.of());
    }

    @Bean
    public ItemProcessor<LambdaBeverageDto, CafeBeverage> beverageProcessor() {
        return cafeBeverageBatchService::toEntity;
    }

    @Bean
    public ItemWriter<CafeBeverage> beverageWriter() {
        JpaItemWriter<CafeBeverage> writer =
                new JpaItemWriterBuilder<CafeBeverage>()
                        .entityManagerFactory(entityManagerFactory)
                        .build();

        return items -> {
            log.info("[DEBUG] Writing {} items to DB.", items.size());
            for (CafeBeverage item : items) {
                log.info(
                        "[DEBUG]   - Writing beverage: '{}', with sizes: {}",
                        item.getName(),
                        item.getSizes().stream()
                                .map(s -> s.getSizeType().name())
                                .collect(Collectors.toList()));
            }
            writer.write(items);
        };
    }

    @Bean
    public Step loadBeveragesStep(
            JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("loadBeverages", jobRepository)
                .<LambdaBeverageDto, CafeBeverage>chunk(10, transactionManager)
                .reader(beverageReader())
                .processor(beverageProcessor())
                .writer(beverageWriter())
                .build();
    }

    @Bean
    public Job beverageJob(JobRepository jobRepository, Step loadBeveragesStep) {
        return new JobBuilder("beverageJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(loadBeveragesStep)
                .build();
    }
}
