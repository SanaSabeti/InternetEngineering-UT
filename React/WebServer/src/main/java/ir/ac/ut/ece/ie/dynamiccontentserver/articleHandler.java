package ir.ac.ut.ece.ie.dynamiccontentserver;

import java.util.List;

public class articleHandler {

    private String wrapHtml(String content) {
        return "<html><head><style>" +
                "body { background-color: #fff0f5; font-family: sans-serif; padding: 50px; color: #555; }" +
                ".container { background: white; padding: 20px; border-radius: 15px; box-shadow: 0 0 10px rgba(216, 27, 96, 0.2); }" +
                "h1, h2 { color: #d81b60; }" +
                "a { color: #f06292; text-decoration: none; font-weight: bold; }" +
                "a:hover { text-decoration: underline; }" +
                "input, textarea { width: 100%; margin: 10px 0; padding: 8px; border: 1px solid #f8bbd0; border-radius: 5px; }" +
                "button { background-color: #d81b60; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; }" +
                "button:hover { background-color: #c2185b; }" +
                "</style></head><body><div class='container'>" + content + "</div></body></html>";
    }

    public byte[] pageBody(HttpRequest request) {

        if (request.method.equals("GET") && request.subPath != null) {
            int id = Integer.parseInt(request.subPath);
            Article a = ArticleRepository.getById(id);
            if (a == null) return wrapHtml("<h1>Article Not Found</h1><a href='/article'>Back</a>").getBytes();

            String html = "<h1>" + a.title + "</h1>" +
                    "<h3>" + a.summary + "</h3>" +
                    "<p>" + a.body + "</p>" +
                    "<a href='/article'>Back</a>";
            return wrapHtml(html).getBytes();
        }

        if (request.method.equals("GET") && request.queryParams.containsKey("s")) {
            String key = request.queryParams.get("s");
            List<Article> list = ArticleRepository.search(key);
            StringBuilder sb = new StringBuilder("<h1>Search: " + key + "</h1>");
            for (Article a : list) {
                sb.append("<p><a href='/article/").append(a.id).append("'>").append(a.title).append("</a></p>");
            }
            sb.append("<br><a href='/article'>Back</a>");
            return wrapHtml(sb.toString()).getBytes();
        }

        if (request.method.equals("POST")) {
            String[] fields = request.body.split("&");
            String title="", summary="", body="";
            for (String f : fields) {
                String[] kv = f.split("=");
                String k = kv[0];
                String v = kv.length > 1 ? decode(kv[1]) : "";
                if (k.equals("title")) title = v;
                if (k.equals("summary")) summary = v;
                if (k.equals("body")) body = v;
            }
            Article added = ArticleRepository.addArticle(title, summary, body);
            if (added == null) return wrapHtml("<h1>Error: Title already exists!</h1><a href='/article'>Back</a>").getBytes();

            String html = "<h1>Article Added Successfully!</h1><p><a href='/article/" + added.id + "'>View Article</a></p>";
            return wrapHtml(html).getBytes();
        }

        List<Article> list = ArticleRepository.getAll();
        StringBuilder sb = new StringBuilder("<h1>Articles</h1>");
        for (Article a : list) {
            sb.append("<p><a href='/article/").append(a.id).append("'>").append(a.title).append("</a></p>");
        }

        sb.append("<br><h2>Add New Article</h2>")
                .append("<form method='POST' action='/article'>")
                .append("Title: <input name='title'><br>")
                .append("Summary: <input name='summary'><br>")
                .append("Body: <textarea name='body'></textarea><br>")
                .append("<button type='submit'>Add</button>")
                .append("</form>");

        return wrapHtml(sb.toString()).getBytes();
    }

    private String decode(String s) {
        return s.replace("+", " ");
    }
}
