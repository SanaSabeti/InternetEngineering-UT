package ir.ac.ut.ece.ie.controller;

import ir.ac.ut.ece.ie.dto.ArticleRequest;
import ir.ac.ut.ece.ie.model.Article;
import ir.ac.ut.ece.ie.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
@CrossOrigin(origins = "http://localhost:5173")
public class ArticleController {

    private final ArticleService service;

    public ArticleController(ArticleService service) {
        this.service = service;
    }

    @GetMapping
    public List<Article> getArticles(
            @RequestParam(
                    name = "s",
                    required = false
            )
            String searchKeyword
    ) {

        if (searchKeyword == null || searchKeyword.isBlank()) {
            return service.getAll();
        }

        return service.search(searchKeyword);
    }

    @GetMapping("/{id}")
    public Article getArticle(
            @PathVariable("id") int id
    ) {
        return service.getArticle(id);
    }

    @PostMapping
    public Article createArticle(
            @Valid @RequestBody ArticleRequest request
    ) {

        return service.createArticle(
                request.getTitle(),
                request.getSummary(),
                request.getBody(),
                request.getReferences()
        );
    }
}