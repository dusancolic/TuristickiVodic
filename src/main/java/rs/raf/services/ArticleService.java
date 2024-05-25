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

    public List<Article> allArticles() {
        return this.articleRepository.allArticles();
    }

    public Article findArticleWithId(Long id) {
        return this.articleRepository.findArticleWithId(id);
    }
}
