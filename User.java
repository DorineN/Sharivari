import java.sql.Statement ;
import java.sql.ResultSet ;
import java.sql.SQLException ;

public class User{
	//attribut
	
	  /** Instruction à exécuter */
    private Statement requete ;
    /** Résultat récupéré */
    private ResultSet resultat ;
    
	private String id;
	private String mdp;
	
	//constructeur
	public User(){
	
	}
	
	public User(String id, String mdp){
		this.id=id;
		this.mdp=mdp;
	}
	
	public User(String id){
		this.id=id;
	}

	//méthode get and set
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMdp() {
		return mdp;
	}

	public void setMdp(String mdp) {
		this.mdp = mdp;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", mdp=" + mdp + "]";
	}
	
	
	public boolean verifConnexion(Connexion maConnexion, String id, String mdp){
		boolean test = true; 
		maConnexion.connexion();
		
		 if (maConnexion.baseOuverte) {
	            try { 
	            requete = maConnexion.conn.createStatement();
	            resultat = requete.executeQuery("Select id, mdp from user WHERE id='"+id+"' AND mdp='"+mdp+"'");
	            if(!resultat.next()){
	            	 test=false;
	            }
	            } catch (SQLException ex) { 
	            System.out.println("Erreur de lecture ! " + ex.getMessage()); 
	            }
	        }
	        else {
	            System.out.println("Connexion à la bdd échoué ! " ); 
	        }
	    
		return(test);
	}
	
	
}



