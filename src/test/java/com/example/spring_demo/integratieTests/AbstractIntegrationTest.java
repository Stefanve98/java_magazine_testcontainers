package com.example.spring_demo.integratieTests;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Testcontainers
public class AbstractIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @ServiceConnection
    protected static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    protected static KafkaContainer kafka = new KafkaContainer("apache/kafka:3.8.1")
            .withEnv("KAFKA_AUTO_CREATE_TOPICS_ENABLE", "true");

    @BeforeAll
    public static void init() {
        postgres.start();
        kafka.start();
        System.setProperty("spring.kafka.bootstrap-servers", kafka.getBootstrapServers());
    }
}
