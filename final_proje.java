import javax.swing.*;
import java.awt.*;
import java.sql.*;



public class final_proje {

    static final String url = "jdbc:mysql://localhost:3306/projem";
    static final String user = "root";
    static final String password = "root123";
    
    public static void main(String[] args){

        JFrame pencere = new JFrame("Movie Finder");
        pencere.setSize(400,600);
        pencere.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel anapencere = new JPanel();
        anapencere.setLayout(new BorderLayout());

        String[] seçenekler = {"Director","Actor","Year"};
        //JPanel combopanel = new JPanel();
        JComboBox<String> comboBox = new JComboBox<>(seçenekler);
        //combopanel.add(comboBox);
 
        JPanel arama = new JPanel();
        JLabel search = new JLabel("Search:");
        JTextField aramaKutusu = new JTextField(20);
        arama.setLayout(new BorderLayout());
        arama.add(search, BorderLayout.WEST);
        arama.add(aramaKutusu, BorderLayout.CENTER);

        JPanel ustPanel = new JPanel(); 
        ustPanel.setLayout(new GridLayout(2,1));
        ustPanel.add(comboBox);
        ustPanel.add(arama);
        

        JPanel sonuc = new JPanel();
        JTextArea sonucAlani = new JTextArea(10,30);
        sonucAlani.setEditable(false);
        JScrollPane sonuckaydirma = new JScrollPane(sonucAlani);
        sonuckaydirma.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sonuckaydirma.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);   
        sonuc.setLayout(new BorderLayout()); 
        sonuc.add(sonuckaydirma,BorderLayout.CENTER);
        //sonuc.add(sonucAlani);
        //sonucAlani.add(sonuckaydirma);

        aramaKutusu.addActionListener(e -> {

            String kriter = (String) comboBox.getSelectedItem();
            String aramaTerimi = aramaKutusu.getText().trim();
            StringBuilder sonuçMetni = new StringBuilder();

            String aranacak = switch (kriter) {
                case "Director" -> "SELECT movie_title FROM movies WHERE director_name LIKE ?";
                case "Actor" -> "SELECT movie_title FROM movies WHERE actor_1_name LIKE ?";
                case "Year" -> "SELECT movie_title FROM movies WHERE title_year LIKE ?";
                default -> "";  
            };

        
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement sorgu = conn.prepareStatement(aranacak)) 
            {

                sorgu.setString(1, "%" + aramaTerimi + "%"); // aranacak = "SELECT movie_title FROM movies WHERE director_name LIKE ?" (%aramaTerimi%)
                ResultSet rs = sorgu.executeQuery();

                if (!rs.isBeforeFirst()) 
                {
                    sonuçMetni.append("Sonuç bulunamadı!");
                } 
                else 
                {
                    while (rs.next()) {
                        String movieTitle = rs.getString("movie_title");
                        sonuçMetni.append(movieTitle).append("\n");
                    }
                }
            } 
            catch (SQLException ex) 
            {
                sonuçMetni.append("Hata: ").append(ex.getMessage());
                ex.printStackTrace();
            }

            sonucAlani.setText(sonuçMetni.toString());
        });


        anapencere.add(ustPanel, BorderLayout.NORTH);   
        anapencere.add(sonuc, BorderLayout.CENTER);
         

        pencere.add(anapencere);
        pencere.setVisible(true);   
    }
}