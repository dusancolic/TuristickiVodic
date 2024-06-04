package rs.raf.resources;

import rs.raf.entities.Article;
import rs.raf.entities.Comment;
import rs.raf.services.CommentService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GET
    @Path("/article/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findCommentsWithArticleId(@PathParam("id") Long id, @QueryParam("page") @DefaultValue("1") int page, @QueryParam("size")@DefaultValue("50") int size) {
        List<Comment> comments = this.commentService.findCommentsWithArticleId(id, page, size);
        long count = this.commentService.countCommentsWithArticleId(id);
        Map<String, Object> response = new HashMap<>();
        response.put("comments", comments);
        response.put("totalComments", count);
        response.put("currentPage", page);
        response.put("totalPages", (int) Math.ceil((double) count / size));
        return Response.ok(response).build();
    }
}
