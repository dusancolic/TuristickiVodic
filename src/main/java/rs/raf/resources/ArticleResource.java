package rs.raf.resources;

import rs.raf.entities.Article;
import rs.raf.services.ArticleService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/articles")
public class ArticleResource {

    @Inject
    private ArticleService articleService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response all() {
        return Response.ok(this.articleService.allArticles()).build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response find(@PathParam("id") Long id) {
        return Response.ok(this.articleService.findArticleWithId(id)).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public Response add(@Valid Article article) {
        return Response.ok(this.articleService.addArticle(article)).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response remove(@PathParam("id") Long id) {
        Article article = articleService.findArticleWithId(id);
        if(articleService.removeArticle(article) == null){
            return Response.status(422, "Unprocessable Entity").entity("Article with this id does not exist").build();
        }

        return Response.ok(articleService.removeArticle(article)).build();
    }

    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(@Valid Article article) {
        return Response.ok(this.articleService.updateArticle(article)).build();
    }

    @GET
    @Path("/destination/{name}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findByDestinationName(@PathParam("name") String name) {
        return Response.ok(this.articleService.findArticlesWithDestinationName(name)).build();
    }

    @PUT
    @Path("/visit/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response visit(@PathParam("id") Long id) {
        Article article = articleService.findArticleWithId(id);
        return Response.ok(this.articleService.incrementVisits(article)).build();
    }

}
