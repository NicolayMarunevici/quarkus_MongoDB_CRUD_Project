package com.example.mongodb.entities;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@EqualsAndHashCode
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Car {
    private String name;
    private String color;
    private String age;
}
