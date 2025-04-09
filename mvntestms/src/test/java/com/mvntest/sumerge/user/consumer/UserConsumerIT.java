package com.mvntest.sumerge.user.consumer;

import com.mvntest.sumerge.AbstractIT;
import com.mvntest.sumerge.events.UserEvent;
import com.mvntest.sumerge.producer.boundary.EventProducer;
import com.mvntest.sumerge.repositories.user.boundary.UserRepository;
import com.mvntest.sumerge.repositories.user.entity.UserDocument;
import com.mvntest.sumerge.resources.user.entity.UserModel;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializer;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.mvntest.sumerge.common.Constants.USER_TEST_TOPIC;
import static com.mvntest.sumerge.common.Utilities.getMessageAr;
import static com.mvntest.sumerge.common.Utilities.getMessageEn;
import static com.mvntest.sumerge.events.UserEvent.Operations.CREATE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@DirtiesContext
@Slf4j
@ExtendWith(SpringExtension.class)
@EmbeddedKafka(partitions = 1, topics = {USER_TEST_TOPIC})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserConsumerIT extends AbstractIT {
    private static final String USER_CREATED = "user.create";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private KafkaMessageListenerContainer<String, UserEvent> container;
    private BlockingQueue<ConsumerRecord<String, UserEvent>> records;

    @BeforeAll
    public void setUp() {
        Map<String, Object> consumerProperties =
                KafkaTestUtils.consumerProps("sender", "false", embeddedKafkaBroker);

        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
          consumerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        //consumerProperties.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaJsonSchemaDeserializer.class);
        consumerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSchemaSerializer.class);
      //  consumerProperties.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://10.0.10.139:8081/");
        consumerProperties.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://10.0.10.199:8081/");

        DefaultKafkaConsumerFactory<String, UserEvent> consumerFactory =
                new DefaultKafkaConsumerFactory<>(consumerProperties);

        ContainerProperties containerProperties = new ContainerProperties(USER_TEST_TOPIC);

        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);

        records = new LinkedBlockingQueue<>();

        container.setupMessageListener((MessageListener<String, UserEvent>) record -> {
            records.add(record);
        });

        container.start();

        ContainerTestUtils.waitForAssignment(container,
                embeddedKafkaBroker.getPartitionsPerTopic());
    }

    @Test
    public void shouldConsumeAndSaveEvent(){
        userRepository.deleteAll();
        List<UserDocument> userList = userRepository.findAll();
        Assertions.assertEquals(0 , userList.size());

        UserModel userModel = new UserModel();
        userModel.setId(new ObjectId().toHexString());
        userModel.setName("Ahmed Yousry");

        eventProducer.sendUserEvent(CREATE, getMessageEn(USER_CREATED),
                getMessageAr(USER_CREATED), userModel);

        await().atMost(2, SECONDS).until(() ->
                userRepository.findAll().size() > 0);

        List<UserDocument> user = userRepository.findAll();

        Assertions.assertNotNull(user);
        Assertions.assertEquals("Ahmed Yousry", user.get(0).getName());
    }
}
