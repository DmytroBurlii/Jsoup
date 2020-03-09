import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Parser {

    public static Connection con;
    public static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws IOException, SQLException {

        if (open()){
            System.out.print("Введите запрос: ");
            String word = scan.nextLine();

            String url = "https://www.google.com/search?q=" + word;

            Document document = Jsoup
                    .connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36")
                    .timeout(10000)
                    .get();

            Elements search = document.select("div[id=search]");
            Elements divs = search.select("div[class=r]");

            Element temp;
            String link;
            int i = 1;
            for (Element div : divs) {
                temp = div.select("a[href]").first();
                link = temp.attr("href");
                System.out.println("link " + i + ": " + link);
                String query =
                        "INSERT INTO data (link, word) " +
                                "VALUES ('" + link + "', '" + word + "')";
                Statement statement = con.createStatement();
                statement.executeUpdate(query);
                statement.close();
                i++;
            }
            select();
            close();
        }
    }





    public static boolean open(){
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Dima\\IdeaProjects\\Parser\\database.db");
            System.out.println("Connected");
            return true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void select(){
        try {
            System.out.print("Поиск в БД: ");
            String word = scan.nextLine();
            Statement statement = con.createStatement();
            String query =
                    "SELECT id, link FROM data WHERE word LIKE '%" + word + "%'";
            ResultSet rs = statement.executeQuery(query);

            System.out.println("id | link");
            while (rs.next()){
                int id = rs.getInt("id");
                String link =  rs.getString("link");
                System.out.println(id + " | " + link);
            }

            rs.close();
            statement.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void close() {
        try {
            con.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}