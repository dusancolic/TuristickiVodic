package rs.raf.services;

import rs.raf.entities.Article;
import rs.raf.repositories.article.ArticleRepository;

import javax.inject.Inject;
import java.util.List;

public class ArticleService {

    @Inject
    private ArticleRepository articleRepository;

    public ArticleService() {
        System.out.println(this);
    }

    public Article addArticle(Article article) {
        return this.articleRepository.addArticle(article);
    }

    public List<Article> findArticlesWithActivityId(Long id) {
        return this.articleRepository.findArticlesWithActivityId(id);
    }

    public List<Article> allArticles(String filter) {
        return this.articleRepository.allArticles(filter);
    }

    public Article findArticleWithId(Long id) {
        return this.articleRepository.findArticleWithId(id);
    }

    public List<Article> findArticlesWithDestinationName(String name) {
        return this.articleRepository.findArticlesWithDestinationName(name);
    }

    public String removeArticle(Article article) {
        return this.articleRepository.removeArticle(article);
    }

    public Article updateArticle(Article article) {
        return this.articleRepository.updateArticle(article);
    }

    public int incrementVisits(Article article) {
        return this.articleRepository.incrementVisits(article);
    }
}
