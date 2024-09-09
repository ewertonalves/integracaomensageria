package com.integracao.integracaomensageria.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobStarterConfig {
	
	private final JobLauncher jobLauncher;
	private final Job distritosJob;
	
	public JobStarterConfig(JobLauncher jobLauncher, Job distritosJob) {
        this.jobLauncher 	= jobLauncher;
        this.distritosJob 	= distritosJob;
    }
	
    @Bean
    public CommandLineRunner runJob() {
        return args -> {
            try {
                JobParameters jobParameters = new JobParametersBuilder()
                        .addLong("s", System.currentTimeMillis())
                        .toJobParameters();
                jobLauncher.run(distritosJob, jobParameters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

}
