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

    public List<Article> findArticlesWithActivityId(Long id,int page, int size) {
        return this.articleRepository.findArticlesWithActivityId(id,page,size);
    }

    public long countArticles(String filter) {
        return this.articleRepository.countArticles(filter);
    }

    public long countArticlesWithActivityId(Long id) {
        return this.articleRepository.countArticlesWithActivityId(id);
    }

    public List<Article> allArticles(String filter, int page, int size) {
        return this.articleRepository.allArticles(filter, page, size);
    }

    public Article findArticleWithId(Long id) {
        return this.articleRepository.findArticleWithId(id);
    }

    public List<Article> findArticlesWithDestinationName(String name, int page, int size) {
        return this.articleRepository.findArticlesWithDestinationName(name,page,size);
    }

    public long countArticlesWithDestinationName(String name) {
        return this.articleRepository.countArticlesWithDestinationName(name);
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
