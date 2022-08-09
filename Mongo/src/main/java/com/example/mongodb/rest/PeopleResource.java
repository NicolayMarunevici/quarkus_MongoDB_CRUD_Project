package com.example.mongodb.rest;

import com.example.mongodb.entities.Car;
import com.example.mongodb.entities.People;
import com.example.mongodb.repository.PeopleRepository;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/people")
public class PeopleResource {

    @Inject
    PeopleRepository peopleRepository;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<People>> findAll() {
        return Uni.createFrom().item(peopleRepository.findAll().list());
    }

    @GET
    @Path("/name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> findByName(@PathParam("name") String name) {
        Optional<People> people = Optional.ofNullable(peopleRepository.findByName(name));
        if(people.isPresent()){
            return Uni.createFrom().item(Response.ok(people).build());
        } else return Uni.createFrom().item(Response.ok("Human with such name does not exist").status(200).build());
    }

    @GET
    @Path("/id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> findById(@PathParam("id") String id) {
        Optional<People> people = peopleRepository.findByIdOptional(new ObjectId(id));
        if(people.isPresent()){
            return Uni.createFrom().item(Response.ok(people).build());
        } else return Uni.createFrom().item(Response.ok("Element with such id does not exist").build());
    }


    @GET
    @Path("/nameCountryAge/{name}/{country}/{age}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> findByNameCountryAge(@PathParam("name") String name, @PathParam("country") String country, @PathParam("age") int age) {
        Optional<List<People>> humans = Optional.of(peopleRepository.findByNameCountryAge(name, country, age));
        if(humans.isPresent()) {
            return Uni.createFrom().item(Response.ok(humans).build());
        }
        return Uni.createFrom().item(Response.ok("Human with such name does not exist").status(200).build());
    }


    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> delete(@PathParam("id") String id) {
        Optional<People> optionalPeople = peopleRepository.findByIdOptional(new ObjectId(id));
        if(optionalPeople.isPresent()) {
            peopleRepository.delete(optionalPeople.get());
            return Uni.createFrom().item(Response.ok("Element was deleted").status(200).build());
        } else {
            return Uni.createFrom().item(Response.ok("Element with such id does not exist").status(200).build());
        }
    }


    @DELETE
    @Path("/{id}/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> deleteByCarName(@PathParam("id") String id, @PathParam("name") String name) {
        People optionalPeople = peopleRepository.findById(new ObjectId(id));
        if(optionalPeople.getCars() != null) {
            Optional<Car> car = optionalPeople.getCars().stream().filter(e -> e.getName().equals(name)).findFirst();
            if(car.isEmpty())
                return Uni.createFrom().item(Response.ok("This human does not have car with such name").status(200).build());
            optionalPeople.getCars().remove(car.get());
            peopleRepository.update(optionalPeople);
            return Uni.createFrom().item(Response.ok("Car " + name + " was deleted").status(200).build());
        } else {
            return Uni.createFrom().item(Response.ok("This human does not have car with such name").status(200).build());
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> save(@RequestBody(required = true) People people) throws Exception {
        people.setId(ObjectId.get());
        peopleRepository.persist(people);
        return Uni.createFrom().item(Response.ok(people).status(Response.Status.CREATED).build());
    }



    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> update(@PathParam("id") String id, @RequestBody(required = true) People people) {
        Optional<People> optionalPeople = Optional.of(peopleRepository.findById(new ObjectId(id)));
        if (!optionalPeople.isPresent()) {
            return Uni.createFrom().item(Response.status(Response.Status.NOT_FOUND).build());
        }
        people.setId(optionalPeople.get().getId());
        peopleRepository.update(people);
        return Uni.createFrom().item(Response.status(Response.Status.CREATED).build());
    }
}
