package ir.ac.ut.ece.ie.repository;

import ir.ac.ut.ece.ie.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository
        extends JpaRepository<Article, Integer> {

    boolean existsByTitleIgnoreCase(String title);

    @Query("""
            SELECT article
            FROM Article article
            LEFT JOIN article.citedByArticles citingArticle
            GROUP BY article
            ORDER BY COUNT(citingArticle) DESC,
                     article.publishedAt DESC
            """)
    List<Article> findAllOrderByCitationCount();

    @Query("""
            SELECT article
            FROM Article article
            WHERE LOWER(article.title)
                      LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(article.summary)
                      LIKE LOWER(CONCAT('%', :keyword, '%'))
            ORDER BY
                CASE
                    WHEN LOWER(article.title)
                         LIKE LOWER(CONCAT('%', :keyword, '%'))
                    THEN 0
                    ELSE 1
                END,
                article.publishedAt DESC
            """)
    List<Article> search(@Param("keyword") String keyword);
}