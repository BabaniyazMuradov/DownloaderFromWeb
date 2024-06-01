package org.example;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws IOException {
        String htmlFile = getHtmlFileAtPath("src/main/java/org/example/data/lenta.html");
        List<String> links = getPathsOfImages(htmlFile);
        downloadFilesFromLinks(links);
    }

    private static void downloadFilesFromLinks(List<String> links) {
        AtomicInteger count = new AtomicInteger(1);
        links.forEach(link->{
            try(BufferedInputStream inputStream = new BufferedInputStream(new URL(link).openStream())){
                FileOutputStream outputStream = new FileOutputStream("src/main/java/org/example/Images"+count+".jpg");
                byte[] buffer = new byte[1024];
                int bytesRead;
                while((bytesRead = inputStream.read(buffer,0,1024))!=-1){
                    outputStream.write(buffer,0,bytesRead);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            count.getAndIncrement();
        });
    }

    private static List<String> getPathsOfImages(String htmlFile) {
       Document document = Jsoup.parse(htmlFile);
       Elements elements = document.select("img");
       List<String> links = new ArrayList<>();
       elements.forEach(element->links.add(elements.attr("abs:src")));
       return links;
       }

    private static String getHtmlFileAtPath(String path) throws IOException {
        StringBuilder builder = new StringBuilder();
        List<String> lines = Files.readAllLines(Paths.get(path));
        lines.forEach(line->{
            builder.append(line + "\n");
        });
        return builder.toString();
    }


}