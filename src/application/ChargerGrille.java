package application;

import application.MotsCroisesTP6;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class ChargerGrille {

    public static final int CHOIX_GRILLE = 10;
    private static final String TABLE_GRILLE = "TP5_GRILLE";
    private static final String TABLE_MOT = "TP5_MOT";

    private Connection connection;

    public ChargerGrille() {
    	  try {
              connection = connecterBD();
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
    

    private Connection connecterBD() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/base_wanwar";  
        //Class.forName("com.mysql.jdbc.Driver");    
        return DriverManager.getConnection(url, "root", "");
    }

    public static MotsCroisesTP6 extraireBD(Connection maconnection, int grille) throws Exception {
        MotsCroisesTP6 m = null;

        String sql = "SELECT num_grille, largeur, hauteur FROM " + ChargerGrille.TABLE_GRILLE + " WHERE num_grille = ?";
        PreparedStatement s = maconnection.prepareStatement(sql);
        s.setInt(1, grille);
        ResultSet set = s.executeQuery();
        if (set.first()) {
            m = new MotsCroisesTP6(set.getInt("hauteur"), set.getInt("largeur"));

            sql = "SELECT * FROM " + TABLE_MOT + " WHERE num_grille = ?";
            s = maconnection.prepareStatement(sql);
            s.setInt(1, grille);
            ResultSet setMots = s.executeQuery();
            while (setMots.next()) {
                int col = setMots.getInt("colonne");
                int lig = setMots.getInt("ligne");

                String solution = setMots.getString("solution");
                boolean horiz = setMots.getBoolean("horizontal");
                m.setDefinition(lig, col, horiz, setMots.getString("definition"));
                if (horiz) {
                    for (int i = 0; i < solution.length(); i++) {
                        m.setSolution(lig, col + i, solution.charAt(i));
                    }
                }else {
                    for (int i = 0; i < solution.length(); i++) {
                        m.setSolution(lig + i, col, solution.charAt(i));
                    }
                }
            }
        }

        return m;
    }

    public MotsCroisesTP6 extraireGrille(int nomGrille) throws Exception {
        return extraireBD(connection, nomGrille);
    }

    public Map<Integer, String> grilleDisponibles() { 
        Map<Integer, String> table = new HashMap<>();

        String sql = "SELECT * FROM " + ChargerGrille.TABLE_GRILLE;
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet data = stmt.executeQuery(sql);
            while (data.next()) {
                table.put(data.getInt("num_grille"), data.getString("nom_grille"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return table;
    }
}
