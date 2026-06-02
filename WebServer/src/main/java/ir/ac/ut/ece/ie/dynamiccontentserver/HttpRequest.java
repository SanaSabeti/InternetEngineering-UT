package ir.ac.ut.ece.ie.dynamiccontentserver;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class HttpRequest {

    public String method; //POST or GET
    public String path; //article
    public String subPath; //id
    public Map<String, String> queryParams = new HashMap<>(); //search command. ex: {search,AI}
    public String body;
    public Map<String, String> form = new HashMap<>();

    public HttpRequest(String line) {
        StringTokenizer st = new StringTokenizer(line, " ");
        this.method = st.nextToken(); // GET ya POST
        String fullPath = st.nextToken(); // masalan: article?s=AI
        String pathPart = fullPath;

        //Search Command:
        if (fullPath.contains("?")) {
            String[] parts = fullPath.split("\\?");

            pathPart = parts[0]; //article
            String query = parts[1]; //s=AI

            // System.out.println("2....." + pathPart);
            // System.out.println("3....." + query);

            String[] kv = query.split("=");
            this.queryParams.put(kv[0],kv[1]);
        }

        // rahnamaee jahat ranj nakeshidan az kharkariye code in future:)
        // /article --> path=article
        // /article/5 --> path=article, subPath=5
        String[] pathSegments = pathPart.substring(1).split("/");
        this.path = pathSegments[0];
        //System.out.println(req.path);

        if (pathSegments.length > 1) {
            this.subPath = pathSegments[1];
            //System.out.println(pathSegments[1]);
        }
    }
}
