package ir.ac.ut.ece.ie.dynamiccontentserver;

import java.io.*;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class DynamicContentServer {
    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(9092);
        System.out.println("Server started on localhost:9092");

        while (true) {
            Socket socket = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String firstLine = reader.readLine();

            if (firstLine == null) continue;
            System.out.println(firstLine);

            HttpRequest request = new HttpRequest(firstLine);

//            System.out.println("method:"+  request.method);
//            System.out.println("\npath:"+  request.path);
//            System.out.println("\nsubPath:"+  request.subPath);
//            System.out.println("\nqueryParams:"+  request.queryParams);


            String headerLine;
            int contentLength = 0;
            while (!(headerLine = reader.readLine()).isEmpty()) {
                if (headerLine.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(headerLine.split(":")[1].trim());
                    //System.out.println(contentLength);
                }
            }

            // if it was post:
            if (contentLength > 0) {
                char[] bodyChars = new char[contentLength];
                reader.read(bodyChars);
                request.body = new String(bodyChars);
            }

            try {
                Class<?> c = Class.forName("ir.ac.ut.ece.ie.dynamiccontentserver." + request.path+ "Handler");
                Object page = c.getDeclaredConstructor().newInstance();

                Method method = c.getMethod("pageBody", HttpRequest.class);
                byte[] data = (byte[]) method.invoke(page, request);

                String header = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + data.length + "\r\n\r\n";

                socket.getOutputStream().write(header.getBytes());
                socket.getOutputStream().write(data);

            } catch (Exception e) {
                e.printStackTrace();
                String header = "HTTP/1.1 404 Not Found\r\n\r\nPage Not Found!";
                socket.getOutputStream().write(header.getBytes());
            }

            socket.getOutputStream().flush();
            socket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        new DynamicContentServer().start();
    }
}
