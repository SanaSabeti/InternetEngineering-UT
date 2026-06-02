package ir.ac.ut.ece.ie.model;

import java.util.List;

public class Article {

    private int id;
    private String title;
    private String summary;
    private String body;
    private List<Integer> references;

    public Article() {
    }

    public Article(int id, String title, String summary, String body, List<Integer> references) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.body = body;
        this.references = references;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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


    public List<Integer> getReferences() {
        return references;
    }

    public void setReferences(List<Integer> references) {
        this.references = references;
    }

}
