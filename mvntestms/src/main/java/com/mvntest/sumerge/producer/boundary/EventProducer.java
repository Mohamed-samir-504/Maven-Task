package com.mvntest.sumerge.producer.boundary;

import com.mvntest.sumerge.events.UserEvent;
import com.mvntest.sumerge.producer.control.EventProducerCtrl;
import com.mvntest.sumerge.resources.user.entity.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.mvntest.sumerge.common.Constants.USER_TEST_TOPIC;

@Slf4j
@Service
public class EventProducer {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
    private final EventProducerCtrl producerCtrl;

    @Autowired
    public EventProducer(KafkaTemplate<String, UserEvent> kafkaTemplate, EventProducerCtrl producerCtrl) {
        this.kafkaTemplate = kafkaTemplate;
        this.producerCtrl = producerCtrl;
    }

    public void sendUserEvent(UserEvent.Operations operation, String description, String descriptionAr, UserModel userModel) {
        UserEvent eventModel = producerCtrl.getUserEvent(operation, description, descriptionAr, userModel);
        sendMessage(eventModel, USER_TEST_TOPIC, userModel.getId());
    }

    public void sendMessage(UserEvent eventModel, String topic, String id) {
        log.info(String.format("$$ -> Producing message --> %s, to topic: %s, and id is %s", eventModel, topic , id));
        this.kafkaTemplate.send(topic, id, eventModel);
    }
}
