package com.example.spring_demo.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final ObjectMapper mapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, ProductKafkaMessage product) throws JsonProcessingException {
        String payload = mapper.writeValueAsString(product);
        kafkaTemplate.send(topic, payload);
    }
}
