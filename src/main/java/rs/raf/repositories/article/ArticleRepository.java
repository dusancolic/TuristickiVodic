package rs.raf.repositories.article;

import rs.raf.entities.Article;

import java.util.List;

public interface ArticleRepository {

     Article addArticle(Article article);
     Article findArticleWithId(Integer id);

     List<Article> allArticles();
}
