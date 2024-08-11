package com.course.multi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
public class MultiThreadedApplication {
	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(25);
		executor.setThreadNamePrefix("TaskExecutor-");
		executor.initialize();
		return executor;
	}

	@Bean
	public String identifier(@Value("${threadName:thread_dummy}") String threadName) {
		return threadName;
	}
	public static void main(String[] args) {
		SpringApplication.run(MultiThreadedApplication.class, args);
	}

}
