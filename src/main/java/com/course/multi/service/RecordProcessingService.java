package com.course.multi.service;
import com.course.multi.processor.RecordProcessorTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class RecordProcessingService {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final int BATCH_SIZE = 1000;

    @PostConstruct
    public void init() {
        Integer startId = jdbcTemplate.queryForObject("SELECT MIN(id) FROM my_table", Integer.class);
        Integer maxId = jdbcTemplate.queryForObject("SELECT MAX(id) FROM my_table", Integer.class);

        if (startId == null || maxId == null) {
            // Handle the case where the table might be empty
            System.out.println("No records found in my_table.");
            return;
        }
        while (startId <= maxId) {
            for (int i = 0; i < 5; i++) {
                int endId = Math.min(startId + BATCH_SIZE - 1, maxId);

                String threadName = "Thread-" + (i + 1);
                taskExecutor.execute(new RecordProcessorTask(threadName, jdbcTemplate, startId, endId));

                startId = endId + 1;

                // Break the loop if we've reached the maximum ID
                if (startId > maxId) {
                    break;
                }
            }
        }
    }
}
