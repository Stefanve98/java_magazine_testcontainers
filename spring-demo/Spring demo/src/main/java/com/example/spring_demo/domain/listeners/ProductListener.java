package com.example.spring_demo.domain.listeners;

import com.example.spring_demo.domain.Product;
import com.example.spring_demo.kafka.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.PostPersist;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductListener {

    private final KafkaProducer kafkaProducer;

    private final ObjectMapper mapper;

    private static final String TOPIC = "product";

    @PostPersist
    public void afterInsert(Product product) throws JsonProcessingException {
        String json = mapper.writeValueAsString(product);
        kafkaProducer.send(TOPIC, json);
    }
}
