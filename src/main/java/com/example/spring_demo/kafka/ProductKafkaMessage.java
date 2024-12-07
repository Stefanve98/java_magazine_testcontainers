package com.example.spring_demo.kafka;

import com.example.spring_demo.domain.Product;

public record ProductKafkaMessage(ChangeStatus status, Product product) {

    public enum ChangeStatus {
        SAVE,
        DELETE
    }
}
