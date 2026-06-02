package ir.ac.ut.ece.ie.repository;

import ir.ac.ut.ece.ie.model.Article;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {

    private final List<Article> articles = new ArrayList<>();
    private int nextId = 1;

    public synchronized Article addArticle(String title, String summary, String body, List<Integer> refs) {

        for (Article a : articles) {
            if (a.getTitle().equalsIgnoreCase(title)) {
                return null;
            }
        }

        Article article = new Article(nextId++, title, summary, body, refs);
        articles.add(article);

        return article;
    }

    public Article getById(int id) {

        for (Article a : articles) {
            if (a.getId() == id) {
                return a;
            }
        }

        return null;
    }

    public List<Article> getAll() {

        List<Article> copy = new ArrayList<>(articles);

        copy.sort((a, b) -> {
            int r1 = a.getReferences() == null ? 0 : a.getReferences().size();
            int r2 = b.getReferences() == null ? 0 : b.getReferences().size();
            return Integer.compare(r2, r1);
        });

        return copy;
    }

    public List<Article> search(String keyword) {

        List<Article> result = new ArrayList<>();

        for (Article a : articles) {

            if (a.getTitle().toLowerCase().contains(keyword.toLowerCase())
                    || a.getSummary().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(a);
            }

        }

        return result;
    }

}
