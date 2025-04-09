package com.mvntest.sumerge.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mvntest.sumerge.resources.user.entity.UserModel;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEvent {
    @JsonProperty("operation")
    private Operations operation;
    @JsonProperty("data")
    private UserModel data;
    @JsonProperty("descriptionAr")
    private String descriptionAr;
    @JsonProperty("description")
    private String description;


    public enum Operations {
        CREATE,
        DELETE,
        UPDATE,

    }

}
