
``` java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)  
@Testcontainers  
public class IntegrationTest {  
  
    @Container  
    @ServiceConnection    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");  
  
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

```

