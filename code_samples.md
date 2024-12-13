
``` java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)  
@Testcontainers  
public class IntegrationTest {  
  
    @Container  
    @ServiceConnection    
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");  
  
    @Container  
    static KafkaContainer kafka = new KafkaContainer("apache/kafka:3.8.1")  
            .withEnv("KAFKA_AUTO_CREATE_TOPICS_ENABLE", "true");  
  
    @BeforeAll  
    public static void init() {  
        System.setProperty("spring.kafka.bootstrap-servers", kafka.getBootstrapServers());  
    }  
}
```


``` xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-testcontainers</artifactId>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>postgresql</artifactId>
        <scope>test</scope>
    </dependency>
```

