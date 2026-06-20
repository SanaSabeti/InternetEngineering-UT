package ir.ac.ut.ece.ie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(
        name = "articles",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_articles_title",
                        columnNames = "title"
                )
        }
)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(
            name = "title",
            nullable = false,
            unique = true,
            length = 255
    )
    private String title;

    @Column(
            name = "summary",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String summary;

    @Column(
            name = "body",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String body;

    @Column(
            name = "published_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime publishedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "article_references",

            joinColumns = @JoinColumn(
                    name = "source_article_id",
                    nullable = false,
                    foreignKey = @ForeignKey(
                            name = "fk_reference_source_article"
                    )
            ),

            inverseJoinColumns = @JoinColumn(
                    name = "target_article_id",
                    nullable = false,
                    foreignKey = @ForeignKey(
                            name = "fk_reference_target_article"
                    )
            ),

            uniqueConstraints = @UniqueConstraint(
                    name = "uk_article_reference",
                    columnNames = {
                            "source_article_id",
                            "target_article_id"
                    }
            )
    )
    @JsonIgnore
    private Set<Article> referencedArticles = new LinkedHashSet<>();

    @ManyToMany(
            mappedBy = "referencedArticles",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private Set<Article> citedByArticles = new LinkedHashSet<>();

    public Article() {
    }

    public Article(String title, String summary, String body) {
        this.title = title;
        this.summary = summary;
        this.body = body;
    }

    @PrePersist
    public void setPublicationTime() {
        if (publishedAt == null) {
            publishedAt = LocalDateTime.now();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Set<Article> getReferencedArticles() {
        return referencedArticles;
    }

    public void setReferencedArticles(Set<Article> referencedArticles) {
        this.referencedArticles = referencedArticles;
    }

    public Set<Article> getCitedByArticles() {
        return citedByArticles;
    }

    public void setCitedByArticles(Set<Article> citedByArticles) {
        this.citedByArticles = citedByArticles;
    }

    @JsonProperty("references")
    public List<Integer> getReferenceIds() {
        return referencedArticles.stream()
                .map(Article::getId)
                .toList();
    }

    @JsonProperty("citationCount")
    public int getCitationCount() {
        return citedByArticles.size();
    }

    public void addReference(Article article) {
        referencedArticles.add(article);
    }

    public void removeReference(Article article) {
        referencedArticles.remove(article);
    }
}