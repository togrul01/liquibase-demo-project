package org.company.springliquibase.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaLoggingService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "application-logs";

    public void sendLog(String level, String message, String traceId, String spanId, String parentId) {
        try {
            Map<String, Object> logData = new HashMap<>();
            logData.put("timestamp", LocalDateTime.now().toString());
            logData.put("level", level);
            logData.put("message", message);
            logData.put("traceId", traceId);
            logData.put("spanId", spanId);
            logData.put("parentId", parentId);
            logData.put("application", "spring-liquibase");

            String jsonLog = objectMapper.writeValueAsString(logData);
            kafkaTemplate.send(TOPIC, jsonLog);
        } catch (Exception e) {
            log.error("Error sending log to Kafka", e);
        }
    }
} 