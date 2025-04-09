package com.mvntest.sumerge.resources.user.control;

import com.mvntest.sumerge.repositories.user.entity.UserDocument;
import com.mvntest.sumerge.resources.user.entity.UserModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserResourceCtrl {
    public List<UserModel> mapUsersListFromEntities(List<UserDocument> userDocumentList) {
        List<UserModel> userModels = new ArrayList<>();
        userDocumentList.stream().map(this::mapUserModelFromEntity).
                forEach(userModels::add);
        return userModels;
    }

    public UserModel mapUserModelFromEntity(UserDocument userDocument) {
        UserModel userModel = new UserModel();
        userModel.setId(userDocument.getId().toString());
        userModel.setName(userDocument.getName());
        return userModel;
    }
}
