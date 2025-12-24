import java.sql.*;
import java.io.FileReader;
import java.util.*;

public class MovieImpoerter {
    // CSV satırını doğru şekilde parse etmek için basit fonksiyon
    public static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') 
            {
                insideQuotes = !insideQuotes;
            } 
            else if (c == ',' && !insideQuotes) 
            {
                result.add(current.toString().trim());
                current = new StringBuilder();
            } 
            else 
            {
                current.append(c);
            }
        }
        result.add(current.toString().trim());
        
        return result.toArray(new String[0]);
    }

     public static void main(String[] args) 
    {
        String url = "jdbc:mysql://localhost:3306/projem";
        String user = "root";
        String password = "root123";
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
        try(Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();)
        {
            String querstr = "CREATE TABLE IF NOT EXISTS movies " + "(idx INTEGER not NULL AUTO_INCREMENT,"+
                                                                    "color VARCHAR(20),"+
                                                                    "director_name VARCHAR(50),"+
                                                                    "num_critic_for_reviews INTEGER,"+
                                                                    "duration INTEGER,"+
                                                                    "director_facebook_likes INTEGER,"+
                                                                    "actor_3_facebook_likes INTEGER,"+
                                                                    "actor_2_name VARCHAR(50),"+
                                                                    "actor_1_facebook_likes INTEGER,"+
                                                                    "gross BIGINT,"+
                                                                    "genres VARCHAR(200),"+
                                                                    "actor_1_name VARCHAR(50),"+
                                                                    "movie_title VARCHAR(200),"+
                                                                    "num_voted_users INTEGER,"+
                                                                    "cast_total_facebook_likes INTEGER,"+
                                                                    "actor_3_name VARCHAR(50),"+
                                                                    "facenumber_in_poster INTEGER,"+
                                                                    "plot_keywords VARCHAR(500),"+
                                                                    "movie_imdb_link VARCHAR(200),"+
                                                                    "num_user_for_reviews INTEGER,"+
                                                                    "language VARCHAR(50),"+
                                                                    "country VARCHAR(50),"+
                                                                    "content_rating VARCHAR(20),"+
                                                                    "budget BIGINT,"+
                                                                    "title_year INTEGER,"+
                                                                    "actor_2_facebook_likes INTEGER,"+
                                                                    "imdb_score FLOAT,"+
                                                                    "aspect_ratio FLOAT,"+
                                                                    "movie_facebook_likes INTEGER,"+
                                                                    "PRIMARY KEY ( idx ))";
            stmt.execute(querstr);
            PreparedStatement hazirgirdi = conn.prepareStatement("INSERT INTO movies (color, director_name, num_critic_for_reviews, duration, director_facebook_likes, actor_3_facebook_likes, actor_2_name, actor_1_facebook_likes, gross, genres, actor_1_name, movie_title, num_voted_users, cast_total_facebook_likes, actor_3_name, facenumber_in_poster, plot_keywords, movie_imdb_link, num_user_for_reviews, language, country, content_rating, budget, title_year, actor_2_facebook_likes, imdb_score, aspect_ratio, movie_facebook_likes) VALUES" + 
                                                        "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            try(FileReader okuyucu = new FileReader("movies.csv");
                java.util.Scanner tarama = new java.util.Scanner(okuyucu)) 
            {
                tarama.nextLine(); // skip header line
                while (tarama.hasNext()) 
                {  
                    String satir = tarama.nextLine();
                    String[] degerler = parseCSVLine(satir);
                    for (int i = 0; i < degerler.length && i < 28; i++) 
                    {
                        String val = degerler[i].replace("\"", "").trim();
                        if (val.isEmpty())
                        {
                            hazirgirdi.setNull(i + 1, Types.NULL);
                        }
                        else
                        {
                            hazirgirdi.setString(i + 1, val);
                        }
                    }
                    hazirgirdi.executeUpdate();
                }
            }
            catch (Exception e) 
            {
                e.printStackTrace();
            }

        }
        catch(SQLException e)
        {
         e.printStackTrace();
        }                                                                 
    }
}
