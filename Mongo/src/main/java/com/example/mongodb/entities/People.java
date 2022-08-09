 package com.example.mongodb.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.*;
import org.bson.types.ObjectId;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@EqualsAndHashCode
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@MongoEntity(collection = "people")
public class People {

    private ObjectId id;
    private String name;
    private String country;
    private int age;
    private List<Car> cars;

}
