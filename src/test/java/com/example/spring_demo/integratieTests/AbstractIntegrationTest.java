package com.example.spring_demo.integratieTests;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.MountableFile;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Testcontainers
public class AbstractIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @ServiceConnection
    protected static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    protected static final KafkaContainer kafka = new KafkaContainer("apache/kafka:3.8.1")
            .withEnv("KAFKA_AUTO_CREATE_TOPICS_ENABLE", "true");

    private static final WireMockContainer wireMock = new WireMockContainer("wiremock/wiremock:2.35.0")
            .withCopyToContainer(
                    MountableFile.forClasspathResource("/mappings"),
                    "/home/wiremock/mappings/"
            );

    @BeforeAll
    public static void init() {
        Stream.of(postgres, kafka, wireMock)
            .parallel()
            .forEach(GenericContainer::start);

        System.setProperty("spring.kafka.bootstrap-servers", kafka.getBootstrapServers());
        System.setProperty("user-application.base-url", wireMock.getBaseUrl());
    }
}
