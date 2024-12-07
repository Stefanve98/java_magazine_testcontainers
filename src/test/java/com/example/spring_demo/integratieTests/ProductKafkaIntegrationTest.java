package com.example.spring_demo.integratieTests;

import com.example.spring_demo.domain.Product;
import com.example.spring_demo.kafka.ProductKafkaMessage;
import com.example.spring_demo.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class ProductKafkaIntegrationTest extends IntegrationTest {

    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;
    private Consumer<String, String> consumer;
    private static final String TOPIC = "product";

    @Autowired
    public ProductKafkaIntegrationTest( ProductRepository productRepository, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void setUp() {
        Map<String, Object> consumerProps = new HashMap<>(KafkaTestUtils.consumerProps(kafka.getBootstrapServers(), UUID.randomUUID().toString(), "true"));

        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps);
        consumer = consumerFactory.createConsumer();
        consumer.subscribe(List.of(TOPIC));
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    void validate_that_when_saving_product_is_placed_on_product_topic() throws JsonProcessingException {
        Product product = Product.builder().name("test_1").price(2.0).description("description_1").build();
        productRepository.save(product);

        ConsumerRecord<String, String> singleRecord = KafkaTestUtils.getSingleRecord(consumer, TOPIC);

        String value = singleRecord.value();
        ProductKafkaMessage message = objectMapper.readValue(value, ProductKafkaMessage.class);

        Assertions.assertEquals(ProductKafkaMessage.ChangeStatus.SAVE, message.status());

        Product kafkaProduct = message.product();
        Assertions.assertNotNull(kafkaProduct.getId());
        Assertions.assertEquals("test_1", kafkaProduct.getName());
        Assertions.assertEquals(2.0, kafkaProduct.getPrice());
        Assertions.assertEquals("description_1", kafkaProduct.getDescription());
    }

    @Test
    void validate_that_when_updating_product_item_is_placed_on_product_topic() throws JsonProcessingException {
        Product product = productRepository.save(Product.builder().name("test_1").price(2.0).description("description_1").build());
        product.setName("test_update");
        product.setPrice(5.0);
        product.setDescription("description_update");
        productRepository.save(product);

        ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(consumer);
        String lastValue = getLastRecord(records);

        ProductKafkaMessage message = objectMapper.readValue(lastValue, ProductKafkaMessage.class);

        Assertions.assertEquals(ProductKafkaMessage.ChangeStatus.SAVE, message.status());

        Product kafkaProduct = message.product();
        Assertions.assertEquals(product.getId(), kafkaProduct.getId());
        Assertions.assertEquals("test_update", kafkaProduct.getName());
        Assertions.assertEquals(5.0, kafkaProduct.getPrice());
        Assertions.assertEquals("description_update", kafkaProduct.getDescription());
    }

    @Test
    void validate_that_when_deleting_product_delete_message_is_placed_on_product_topic() throws JsonProcessingException {
        Product product = Product.builder().name("test_1").price(2.0).description("description_1").build();
        Product savedProduct = productRepository.save(product);

        productRepository.deleteById(savedProduct.getId());

        ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(consumer);
        String lastValue = getLastRecord(records);

        ProductKafkaMessage message = objectMapper.readValue(lastValue, ProductKafkaMessage.class);

        Assertions.assertEquals(ProductKafkaMessage.ChangeStatus.DELETE, message.status());

        Product kafkaProduct = message.product();
        Assertions.assertNotNull(kafkaProduct.getId());
        Assertions.assertNull(kafkaProduct.getName());
        Assertions.assertNull(kafkaProduct.getDescription());
    }

    private String getLastRecord(ConsumerRecords<String, String> records) {
        String lastValue = null;
        for (ConsumerRecord<String, String> record : records.records(TOPIC)) {
            lastValue = record.value();
        }

        return lastValue;
    }
}
