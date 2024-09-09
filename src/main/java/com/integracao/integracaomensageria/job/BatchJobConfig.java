package com.integracao.integracaomensageria.job;

import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.integracao.integracaomensageria.client.DistritosSpClient;
import com.integracao.integracaomensageria.model.Distritos;

@Configuration
@EnableBatchProcessing
public class BatchJobConfig {

	private final JobRepository 			 jobRepository;
	private final DistritosSpClient 		 distritosSpClient;
	private final AmqpTemplate 				 amqpTemplate;
	private final PlatformTransactionManager transactionManager;

	public BatchJobConfig(JobRepository jobRepository, DistritosSpClient distritosSpClient, AmqpTemplate amqpTemplate,
			PlatformTransactionManager transactionManager) {
		this.jobRepository 		= jobRepository;
		this.distritosSpClient 	= distritosSpClient;
		this.amqpTemplate 		= amqpTemplate;
		this.transactionManager = transactionManager;
	}
	
	@Bean
	@StepScope
	public ItemReader<Distritos> distritosReader() {
		List<Distritos> distritos = distritosSpClient.getDistritos();
		return new ListItemReader<>(distritos);
	}
	
	@Bean
	public ItemProcessor<Distritos, Distritos> distritosProcessor() {
		return distritos -> distritos;
	}
	
	@Bean
	public ItemWriter<Distritos> distritosWriter() {
		return distritos -> distritos.forEach(distrito -> amqpTemplate.convertAndSend("distritosQueue", distrito));
	}
	
	@Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(10);
        return taskExecutor;
    }
	
    @Bean
    public Step stepDistritos() {
        return new StepBuilder("stepDistritos", jobRepository)
                .<Distritos, Distritos>chunk(100, transactionManager)
                .reader(distritosReader())
                .processor(distritosProcessor())
                .writer(distritosWriter())
                .taskExecutor(taskExecutor())
                .build();
    }
	
	@Bean
	public Job distritosJob() {
	    return new JobBuilder("distritosJob", jobRepository)
	            .start(stepDistritos())
	            .build();
	}

}
