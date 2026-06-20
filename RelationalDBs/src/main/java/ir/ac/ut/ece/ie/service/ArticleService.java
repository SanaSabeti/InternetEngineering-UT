package ir.ac.ut.ece.ie.service;

import ir.ac.ut.ece.ie.model.Article;
import ir.ac.ut.ece.ie.repository.ArticleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository repository;

    public ArticleService(ArticleRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Article createArticle(
            String title,
            String summary,
            String body,
            List<Integer> refs
    ) {

        String normalizedTitle = requireText(title, "Title");
        String normalizedSummary = requireText(summary, "Summary");
        String normalizedBody = requireText(body, "Body");

        if (repository.existsByTitleIgnoreCase(normalizedTitle)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "An article with this title already exists."
            );
        }

        LinkedHashSet<Integer> referenceIds =
                normalizeReferenceIds(refs);

        List<Article> foundArticles =
                repository.findAllById(referenceIds);

        if (foundArticles.size() != referenceIds.size()) {

            Set<Integer> foundIds = foundArticles.stream()
                    .map(Article::getId)
                    .collect(Collectors.toSet());

            List<Integer> missingIds = referenceIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Referenced articles do not exist: " + missingIds
            );
        }
        Map<Integer, Article> articlesById =
                foundArticles.stream()
                        .collect(Collectors.toMap(
                                Article::getId,
                                Function.identity()
                        ));

        LinkedHashSet<Article> orderedReferences =
                referenceIds.stream()
                        .map(articlesById::get)
                        .collect(Collectors.toCollection(
                                LinkedHashSet::new
                        ));

        Article article = new Article(
                normalizedTitle,
                normalizedSummary,
                normalizedBody
        );

        article.setReferencedArticles(orderedReferences);

        try {
            Article savedArticle =
                    repository.saveAndFlush(article);

            initializeRelations(savedArticle);

            return savedArticle;

        } catch (DataIntegrityViolationException exception) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "An article with this title already exists.",
                    exception
            );
        }
    }

    @Transactional(readOnly = true)
    public Article getArticle(int id) {

        Article article = repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Article with id " + id + " was not found."
                        )
                );

        initializeRelations(article);

        return article;
    }

    @Transactional(readOnly = true)
    public List<Article> getAll() {

        List<Article> articles =
                repository.findAllOrderByCitationCount();

        articles.forEach(this::initializeRelations);

        return articles;
    }

    @Transactional(readOnly = true)
    public List<Article> search(String keyword) {

        /*
         * اگر پارامتر جست‌وجو خالی باشد،
         * همان فهرست کامل مقاله‌ها برگردانده می‌شود.
         */
        if (keyword == null || keyword.isBlank()) {
            return getAll();
        }

        List<Article> articles =
                repository.search(keyword.trim());

        articles.forEach(this::initializeRelations);

        return articles;
    }

    private String requireText(String value, String fieldName) {

        if (value == null || value.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    fieldName + " must not be empty."
            );
        }

        return value.trim();
    }

    private LinkedHashSet<Integer> normalizeReferenceIds(
            List<Integer> refs
    ) {

        if (refs == null || refs.isEmpty()) {
            return new LinkedHashSet<>();
        }

        if (refs.stream().anyMatch(Objects::isNull)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Reference ids must not contain null."
            );
        }

        if (refs.stream().anyMatch(id -> id <= 0)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Reference ids must be positive numbers."
            );
        }

        return new LinkedHashSet<>(refs);
    }

    private void initializeRelations(Article article) {
        article.getReferencedArticles().size();
        article.getCitedByArticles().size();
    }
}