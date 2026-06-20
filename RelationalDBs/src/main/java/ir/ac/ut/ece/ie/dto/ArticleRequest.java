package ir.ac.ut.ece.ie.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class ArticleRequest {

    @NotBlank(message = "Title must not be empty.")
    @Size(
            max = 255,
            message = "Title must not be longer than 255 characters."
    )
    private String title;

    @NotBlank(message = "Summary must not be empty.")
    private String summary;

    @NotBlank(message = "Body must not be empty.")
    private String body;

    private List<
            @NotNull(message = "Reference id must not be null.")
            @Positive(message = "Reference id must be a positive number.")
                    Integer
            > references = new ArrayList<>();

    public ArticleRequest() {
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

    public List<Integer> getReferences() {
        return references;
    }

    public void setReferences(List<Integer> references) {
        this.references =
                references == null
                        ? new ArrayList<>()
                        : references;
    }
}