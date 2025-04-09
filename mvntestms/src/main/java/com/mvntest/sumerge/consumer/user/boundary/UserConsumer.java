package com.mvntest.sumerge.consumer.user.boundary;

import com.mvntest.sumerge.common.exceptions.UserNotFoundException;
import com.mvntest.sumerge.consumer.user.control.UserConsumerCtrl;
import com.mvntest.sumerge.events.UserEvent;
import com.mvntest.sumerge.repositories.user.boundary.UserRepository;
import com.mvntest.sumerge.repositories.user.entity.UserDocument;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Optional;

import static com.mvntest.sumerge.common.Constants.USER_TEST_TOPIC;

@Service
public class UserConsumer {

    private final UserConsumerCtrl userConsumerCtrl;
    private final UserRepository userRepository;
    private LinkedHashMap map;



    @Autowired
    public UserConsumer(UserConsumerCtrl userConsumerCtrl, UserRepository userRepository) {
        this.userConsumerCtrl = userConsumerCtrl;
        this.userRepository = userRepository;
    }

    @KafkaListener(topics = USER_TEST_TOPIC)
    public void consume(ConsumerRecord<String, LinkedHashMap> record) {


        UserEvent eventModel = userConsumerCtrl.getModel(record.value());
        userConsumerCtrl.validate(eventModel);
        UserDocument userDocument = userConsumerCtrl.getDocument(record.value());

        Optional<UserDocument> engineeringOfficeOptional = userRepository.findById(userDocument.getId());

        switch (eventModel.getOperation()) {
            case DELETE:
                if (engineeringOfficeOptional.isEmpty())
                    throw new UserNotFoundException();
                userRepository.delete(userDocument);
                break;
            case UPDATE:
                if (engineeringOfficeOptional.isEmpty())
                    throw new UserNotFoundException();
                userRepository.save(userDocument);
            case CREATE:
            default:
                userRepository.save(userDocument);
                break;
        }
    }
}
