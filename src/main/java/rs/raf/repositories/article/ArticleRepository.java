package rs.raf.repositories.article;

import rs.raf.entities.Activity;
import rs.raf.entities.Article;

import java.util.List;

public interface ArticleRepository {

     Article addArticle(Article article);
     Article findArticleWithId(Long id);
     List<Article> findArticlesWithActivityId(Long id);


     List<Article> findArticlesWithDestinationName(String name);

     String removeArticle(Article article);

     int incrementVisits(Article article);
     Article updateArticle(Article article);
     List<Article> allArticles(String filter);
}
