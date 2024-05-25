package rs.raf.resources;

import rs.raf.entities.Article;
import rs.raf.entities.Comment;
import rs.raf.services.CommentService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/comments")
public class CommentResource {

    @Inject
    private CommentService commentService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response all() {
        return Response.ok(this.commentService.allComments()).build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response find(@PathParam("id") Long id) {
        return Response.ok(this.commentService.findCommentWithId(id)).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public Response add(@Valid Comment comment) {
        return Response.ok(this.commentService.addComment(comment)).build();
    }

}
