package rs.raf.resources;

import rs.raf.entities.Subject;
import rs.raf.services.SubjectService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/subjects")
public class SubjectResource {

    @Inject
    private SubjectService subjectService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response all() {
        return Response.ok(this.subjectService.allSubjects()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Subject create(@Valid Subject subject) {
        return this.subjectService.addSubject(subject);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Subject find(@PathParam("id") Integer id) {
        return this.subjectService.findSubject(id);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Integer id) {
        this.subjectService.deleteSubject(id);
    }

}
