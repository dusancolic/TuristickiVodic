package rs.raf.resources;

import rs.raf.entities.Destination;
import rs.raf.entities.User;
import rs.raf.services.DestinationService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/destinations")
public class DestinationResource {

    @Inject
    private DestinationService destinationService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response all() {
        return Response.ok(this.destinationService.allDestinations()).build();
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("name") String name) {
        return Response.ok(this.destinationService.findDestination(name)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(@Valid Destination destination) {
        if(destinationService.findDestination(destination.getName()) != null){
            return Response.status(422, "Unprocessable Entity").entity("Destination with this name already exists").build();
        }

        return Response.ok(this.destinationService.addDestination(destination)).build();
    }

    @DELETE
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove(@PathParam("name") String name) {
        Destination destination = destinationService.findDestination(name);
        if(destination == null){
            return Response.status(422, "Unprocessable Entity").entity("Destination with this name does not exist").build();
        }
        return Response.ok(destinationService.removeDestination(destination)).build();
    }
    @GET
    @Path("/id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Long id) {
        return Response.ok(this.destinationService.findDestinationById(id)).build();
    }
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@Valid Destination destination) {
        Destination destination1 = destinationService.findDestination(destination.getName());
        if(destination1 != null && !destination1.getId().equals(destination.getId())){
            return Response.status(422, "Unprocessable Entity").entity("Destination with this name already exists").build();
        }
        return Response.ok(this.destinationService.updateDestination(destination)).build();
    }
}
