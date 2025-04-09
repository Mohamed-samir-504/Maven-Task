package com.mvntest.sumerge.repositories;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class AbstractEntity implements Serializable {
    private static final long serialVersionUID = -1183608206148005026L;
}
