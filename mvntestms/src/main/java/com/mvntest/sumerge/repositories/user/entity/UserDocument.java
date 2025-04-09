package com.mvntest.sumerge.repositories.user.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = false)
@Document("users")
public class UserDocument {

    @Id
    private ObjectId id;
    private String name;
}
