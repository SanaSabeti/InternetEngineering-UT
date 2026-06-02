package ir.ac.ut.ece.ie.service;

import ir.ac.ut.ece.ie.model.Article;
import ir.ac.ut.ece.ie.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository repository;

    public ArticleService(ArticleRepository repository) {
        this.repository = repository;
    }

    public Article createArticle(String title, String summary, String body, List<Integer> refs) {
        return repository.addArticle(title, summary, body, refs);
    }

    public Article getArticle(int id) {
        return repository.getById(id);
    }

    public List<Article> getAll() {
        return repository.getAll();
    }

    public List<Article> search(String keyword) {
        return repository.search(keyword);
    }

}
