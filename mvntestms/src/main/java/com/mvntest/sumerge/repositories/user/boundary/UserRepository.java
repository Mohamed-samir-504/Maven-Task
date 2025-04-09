package com.mvntest.sumerge.repositories.user.boundary;

import com.mvntest.sumerge.repositories.user.entity.UserDocument;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserDocument, ObjectId> {
}
