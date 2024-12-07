package com.example.spring_demo.domain.listeners;

import com.example.spring_demo.domain.Product;
import com.example.spring_demo.kafka.KafkaProducer;
import com.example.spring_demo.kafka.ProductKafkaMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductListener {

    private final KafkaProducer kafkaProducer;

    private final ObjectMapper mapper;

    private static final String TOPIC = "product";

    @PostPersist
    public void afterInsert(Product product) throws JsonProcessingException {
        kafkaProducer.send(TOPIC,
                new ProductKafkaMessage(ProductKafkaMessage.ChangeStatus.SAVE, product)
        );
    }

    @PostUpdate
    public void afterUpdate(Product product) throws JsonProcessingException {
        kafkaProducer.send(TOPIC,
                new ProductKafkaMessage(ProductKafkaMessage.ChangeStatus.SAVE, product)
        );
    }

    @PostRemove
    public void afterRemove(Product product) throws JsonProcessingException {
        Product deletedProduct = new Product();
        deletedProduct.setId(product.getId());

        kafkaProducer.send(TOPIC,
                new ProductKafkaMessage(ProductKafkaMessage.ChangeStatus.DELETE, deletedProduct)
        );
    }
}
