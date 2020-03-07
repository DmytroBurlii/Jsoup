import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Scanner;

public class Parser {

    public static void main(String[] args) throws IOException {

        Scanner scan = new Scanner(System.in);
        System.out.print("Введите запрос: ");
        String query = scan.nextLine();

        String url = "https://www.google.com/search?q=" + query;

        Document document = Jsoup
                .connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36")
                .timeout(10000)
                .get();

        Elements search = document.select("div[id=search]");
        Elements divs = search.select("div[class=r]");

        Element temp;
        int i = 1;
        for (Element div : divs) {
            temp = div.select("a[href]").first();
            System.out.println("link " + i + ": " + temp.attr("href"));
            i++;
        }
    }
}