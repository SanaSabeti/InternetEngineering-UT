package ir.ac.ut.ece.ie.dynamiccontentserver;

import java.util.*;

public class ArticleRepository {

    private static final List<Article> articles = new ArrayList<>();
    private static int nextId = 1;

    public static Article addArticle(String title, String summary, String body) {
        for (Article a : articles) {
            if (a.title.equalsIgnoreCase(title))
                return null;
        }

        Article article = new Article(nextId++, title, summary, body);
        articles.add(article);

        return article;
    }

    public static Article getById(int id) {
        for (Article a : articles) {
            if (a.id == id) return a;
        }
        return null;
    }

    public static List<Article> search(String keyword) {
        keyword = keyword.toLowerCase();

        List<Article> titleMatch = new ArrayList<>();
        List<Article> summaryMatch = new ArrayList<>();

        for (Article a : articles) {
            boolean inTitle = a.title.toLowerCase().contains(keyword);
            boolean inSummary = a.summary.toLowerCase().contains(keyword);

            if (inTitle) titleMatch.add(a);
            else if (inSummary) summaryMatch.add(a);
        }

        List<Article> result = new ArrayList<>();
        result.addAll(titleMatch);
        result.addAll(summaryMatch);

        return result;
    }

    public static List<Article> getAll() {
        return new ArrayList<>(articles);
    }
}
