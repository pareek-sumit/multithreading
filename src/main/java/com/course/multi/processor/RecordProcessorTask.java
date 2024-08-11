package com.course.multi.processor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public class RecordProcessorTask implements Runnable {

    private final String threadName;
    private final JdbcTemplate jdbcTemplate;
    private final int startId;
    private final int endId;

    public RecordProcessorTask(String threadName, JdbcTemplate jdbcTemplate, int startId, int endId) {
        this.threadName = threadName;
        this.jdbcTemplate = jdbcTemplate;
        this.startId = startId;
        this.endId = endId;
    }

    @Override
    public void run() {
        try {
            // Insert into the tracking table about to process
            jdbcTemplate.update("INSERT INTO processing_tracking (thread_name, id_from, id_to, status, timestamp) VALUES (?, ?, ?, ?, ?)",
                    threadName, startId, endId, "about to process", new java.sql.Timestamp(System.currentTimeMillis()));

            // Fetch the records for processing
            String sql = "SELECT * FROM my_table WHERE id BETWEEN ? AND ?";
            List<Map<String, Object>> records = jdbcTemplate.queryForList(sql, startId, endId);

            // Insert into the tracking table before starting processing
            jdbcTemplate.update("UPDATE processing_tracking SET status = ?, timestamp = ? WHERE thread_name = ? AND id_from = ? AND id_to = ?",
                    "processing", new java.sql.Timestamp(System.currentTimeMillis()), threadName, startId, endId);


            // Simulate processing of the records
            for (Map<String, Object> record : records) {
                // Process each record here
                // Example: System.out.println("Processing record: " + record.get("id"));
            }

            // Update status to 'success' after successful processing
            jdbcTemplate.update("UPDATE processing_tracking SET status = ?, timestamp = ? WHERE thread_name = ? AND id_from = ? AND id_to = ?",
                    "success", new java.sql.Timestamp(System.currentTimeMillis()), threadName, startId, endId);

        } catch (Exception e) {
            // Handle any exceptions and mark the status as 'failure'
            jdbcTemplate.update("UPDATE processing_tracking SET status = ?, timestamp = ? WHERE thread_name = ? AND id_from = ? AND id_to = ?",
                    "failure", new java.sql.Timestamp(System.currentTimeMillis()), threadName, startId, endId);
            e.printStackTrace();
        }
    }
}
