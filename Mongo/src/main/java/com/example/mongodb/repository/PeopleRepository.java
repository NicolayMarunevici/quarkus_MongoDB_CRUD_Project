package com.example.mongodb.repository;

import com.example.mongodb.entities.People;
import io.quarkus.mongodb.panache.PanacheMongoRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PeopleRepository implements PanacheMongoRepository<People> {

    public People findByName(String name){
        return find("name", name).firstResult();
    }

    public List<People> findByNameCountryAge(String name, String country, int age){
        return find("{'name' : ?1, 'country' : ?2, 'age' : ?3}", name, country, age).list();
    }
}
