import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class Connexion {

   /** URL de connexion */
    private final String connectionURL = "jdbc:mysql://localhost:3306/planning"; 
    /** objet de connexion */
    protected Connection conn ;
    /** indicateur de connexion r�ussie */    
    public boolean baseOuverte = false ;
	
	
	  public void connexion() {
	        try { 
	            //** un pilote jdbc derby doit �tre specifi� avant toute connexion */
	        	Class.forName("com.mysql.jdbc.Driver");
	            //** URL, identifiant et mot de passe sont n�cessaires pour ouvrir une connexion */ 
	            conn = DriverManager.getConnection(connectionURL,"root",""); 
	            baseOuverte = true ;
	            System.out.println("Connection � la bdd r�ussie ! "); 
	        } catch (ClassNotFoundException ex) { 
	            System.out.println("Driver Connect failed ! " + ex.getMessage()); 
	            baseOuverte = false ;
	        }
	        catch (SQLException ex) { 
	            System.out.println("SQL Connect failed ! " + ex.getMessage()); 
	            baseOuverte = false ;
	        }
	    }
	
	
	
}