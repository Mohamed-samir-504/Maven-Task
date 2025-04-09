package com.mvntest.sumerge.producer.control;

import com.mvntest.sumerge.events.UserEvent;
import com.mvntest.sumerge.resources.user.entity.UserModel;
import org.springframework.stereotype.Component;

@Component
public class EventProducerCtrl {
    public UserEvent getUserEvent(UserEvent.Operations operation, String description, String descriptionAr,
                                  UserModel userModel) {

        UserEvent topicEvent = new UserEvent(operation,userModel, description, descriptionAr);
        return topicEvent;
    }

}
