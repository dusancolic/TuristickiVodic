package rs.raf.resources;

import rs.raf.entities.Article;
import rs.raf.services.ArticleService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/articles")
public class ArticleResource {

    @Inject
    private ArticleService articleService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response all(@QueryParam("page") @DefaultValue("1") int page, @QueryParam("size")@DefaultValue("50") int size) {
        List<Article> articles = this.articleService.allArticles("all", page, size);
        long count = this.articleService.countArticles("all");

        Map<String, Object> response = new HashMap<>();
        response.put("articles", articles);
        response.put("totalArticles", count);
        response.put("currentPage", page);
        response.put("totalPages", (int) Math.ceil((double) count / size));
        return Response.ok(response).build();
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
    public Response findByDestinationName(@PathParam("name") String name,@QueryParam("page") @DefaultValue("1") int page, @QueryParam("size")@DefaultValue("50") int size) {
        List<Article> articles = this.articleService.findArticlesWithDestinationName(name, page, size);
        long count = this.articleService.countArticlesWithDestinationName(name);
        Map<String, Object> response = new HashMap<>();
        response.put("articles", articles);
        response.put("totalArticles", count);
        response.put("currentPage", page);
        response.put("totalPages", (int) Math.ceil((double) count / size));
        return Response.ok(response).build();
    }

    @GET
    @Path("/most-visited")
    @Produces({MediaType.APPLICATION_JSON})
    public Response mostVisited(@QueryParam("page") @DefaultValue("1") int page, @QueryParam("size")@DefaultValue("50") int size) {
        List<Article> articles = this.articleService.allArticles("mostVisited", page, size);
        long count = this.articleService.countArticles("mostVisited");
        if(count > 10)
            count = 10;
        Map<String, Object> response = new HashMap<>();
        response.put("articles", articles);
        response.put("totalArticles", count);
        response.put("currentPage", page);
        response.put("totalPages", (int) Math.ceil((double) count / size));
        return Response.ok(response).build();
    }

    @GET
    @Path("/most-recent")
    @Produces({MediaType.APPLICATION_JSON})
    public Response mostRecent(@QueryParam("page") @DefaultValue("1") int page, @QueryParam("size")@DefaultValue("50") int size) {
        List<Article> articles = this.articleService.allArticles("mostRecent", page, size);
        long count = this.articleService.countArticles("mostRecent");
        if(count > 10)
            count = 10;
        Map<String, Object> response = new HashMap<>();
        response.put("articles", articles);
        response.put("totalArticles", count);
        response.put("currentPage", page);
        response.put("totalPages", (int) Math.ceil((double) count / size));
        return Response.ok(response).build();
    }


    @PUT
    @Path("/visit/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response visit(@PathParam("id") Long id) {
        Article article = articleService.findArticleWithId(id);
        return Response.ok(this.articleService.incrementVisits(article)).build();
    }

    @GET
    @Path("/activity/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response allArticlesWithActivityId(@PathParam("id") Long id, @QueryParam("page") @DefaultValue("1") int page, @QueryParam("size")@DefaultValue("50") int size) {
        List<Article> articles = this.articleService.findArticlesWithActivityId(id, page, size);
        long count = this.articleService.countArticlesWithActivityId(id);
        Map<String, Object> response = new HashMap<>();
        response.put("articles", articles);
        response.put("totalArticles", count);
        response.put("currentPage", page);
        response.put("totalPages", (int) Math.ceil((double) count / size));
        return Response.ok(response).build();
    }
}
