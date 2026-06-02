package ir.ac.ut.ece.ie.controller;

import ir.ac.ut.ece.ie.dto.ArticleRequest;
import ir.ac.ut.ece.ie.model.Article;
import ir.ac.ut.ece.ie.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService service;

    public ArticleController(ArticleService service) {
        this.service = service;
    }

    @GetMapping
    public List<Article> getArticles(@RequestParam(required = false) String s) {

        if (s != null) {
            return service.search(s);
        }

        return service.getAll();
    }

    @GetMapping("/{id}")
    public Article getArticle(@PathVariable int id) {
        return service.getArticle(id);
    }

    @PostMapping
    public Article createArticle(@RequestBody ArticleRequest request) {

        return service.createArticle(
                request.getTitle(),
                request.getSummary(),
                request.getBody(),
                request.getReferences()
        );
    }

}
