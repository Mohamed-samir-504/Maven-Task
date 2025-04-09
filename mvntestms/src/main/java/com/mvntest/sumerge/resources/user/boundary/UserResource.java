package com.mvntest.sumerge.resources.user.boundary;

import com.mvntest.sumerge.producer.boundary.EventProducer;
import com.mvntest.sumerge.repositories.user.boundary.UserRepository;
import com.mvntest.sumerge.resources.user.control.UserResourceCtrl;
import com.mvntest.sumerge.resources.user.entity.UserModel;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mvntest.sumerge.common.Utilities.getMessageAr;
import static com.mvntest.sumerge.common.Utilities.getMessageEn;
import static com.mvntest.sumerge.events.UserEvent.Operations.CREATE;

@RestController
@CrossOrigin
@RequestMapping(value = "/users")
public class UserResource {

    private static final String USER_CREATED = "user.create";

    private final EventProducer eventProducer;
    private final UserRepository userRepository;
    private final UserResourceCtrl userResourceCtrl;

    @Autowired
    public UserResource(EventProducer eventProducer, UserRepository userRepository, UserResourceCtrl userResourceCtrl) {
        this.eventProducer = eventProducer;
        this.userRepository = userRepository;
        this.userResourceCtrl = userResourceCtrl;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.ok().body(userResourceCtrl.mapUsersListFromEntities(userRepository.findAll()));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser() {
        UserModel userModel = new UserModel();
        userModel.setId(new ObjectId().toHexString());
        userModel.setName("Ahmed Yousry");
        eventProducer.sendUserEvent(CREATE, getMessageEn(USER_CREATED),
                getMessageAr(USER_CREATED), userModel);
        return ResponseEntity.ok().body(getMessageEn(USER_CREATED));
    }
}
