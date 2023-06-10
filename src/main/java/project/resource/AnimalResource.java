package project.resource;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.dto.AnimalDTO;
import project.model.Animal;

@Path("animals")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnimalResource {

    @GET
    public Response listAll() {
        List<Animal> animals = Animal.listAll();
        return Response.ok(animals).build();
    }

    @POST
    @Transactional
    public Response insert(AnimalDTO dto) {
        Animal animal = new Animal();
        animal.setName(dto.getName());
        animal.setCategory(dto.getCategory());
        animal.persist();

        return Response.ok(animal).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void edit(@PathParam("id") Long id, AnimalDTO dto) {
        Optional<Animal> animalOp = Animal.findByIdOptional(id);
        if (animalOp.isPresent()) {
            Animal animalNew = animalOp.get();
            animalNew.setName(dto.getName());
            animalNew.setCategory(dto.getCategory());
            animalNew.persist();
        } else {
            throw new NotFoundException();
        }
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Optional<Animal> animalOp = Animal.findByIdOptional(id);
        animalOp.ifPresentOrElse(Animal::delete, () -> {
            throw new NotFoundException();
        });
    }
}
