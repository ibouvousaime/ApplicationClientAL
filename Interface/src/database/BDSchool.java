package database;

import entities.Etudiant;
import entities.Utilisateur;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class BDSchool {
    
    private  final String user="newsite";
    private final String password="passer";
    private final String url ="jdbc:mysql://localhost:3306/journal";
    private String driver = "com.mysql.jdbc.Driver";
    private Connection connex;
    
    public BDSchool(){
        
                try {
                        Class.forName(driver);
                        connex = DriverManager.getConnection(url, user, password);
                        System.out.println("Connexion à la base de données réussie");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Echec de connexion à la base de données");
                    }
        
            }
    
    
   public boolean seConnecter(String login, String password)
   {
       try  {
           
            String sql ="SELECT * FROM utilisateurs WHERE login= ? AND password= ?;";
            PreparedStatement stm = connex.prepareStatement(sql);//Creer un objet pour preparer la requete SQL
            stm.setString(1, login);//completer la requete en mettant la valeur du premier paramétre qui est login
            stm.setString(2, password);//completer la requete en mettant la valur du scond paramétre qui est password
            //Execution de la requete et récupération du resultat dans un Objet de type ResultSet
            ResultSet rs = stm.executeQuery(); //Execute la requete
            
            if(rs.next())//Oui, La requete renvoi un resultat
            {
                System.out.println("L'utilisateur existe"); 
                return true;
            }
            else{//Non, Aucun résultat trouvé
                System.out.println("L'utilisateur n'existe pas"); 
                return false;
            }
            
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
       
   } 

    public boolean ajouterUtilisateur(Utilisateur utilisateur){
        try {
            String sql ="INSERT INTO utilisateurs(login, password, prenom, nom, profil) VALUES (?,?,?,?,?)";
            PreparedStatement stm = connex.prepareStatement(sql);
            stm.setString(1, utilisateur.getLogin());
            stm.setString(2, utilisateur.getPassword());
            stm.setString(3, utilisateur.getPrenom());
            stm.setString(4, utilisateur.getNom());
            stm.setString(5, utilisateur.getProfil());
            stm.executeUpdate();//Inserer dans la table utilisateurs
            return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
    }
     public int obtenirNombreDadministrateurs(){
            try {
                String sql ="SELECT count(*) as count FROM utilisateurs WHERE profil=?;";
                PreparedStatement stm = connex.prepareCall(sql);
                stm.setString(1, "Administrateur");
                ResultSet rs = stm.executeQuery();
                while(rs.next())
            {
               return rs.getInt("count");
            }
                
            }
            catch (Exception e) {
                
            }
            return 0;
        }
               public int obtenirNombreDutilisateur(){
            try {
                String sql ="SELECT count(*) as count FROM utilisateurs WHERE profil!=?;";
                PreparedStatement stm = connex.prepareCall(sql);
                stm.setString(1, "Administrateur");
                ResultSet rs = stm.executeQuery();
                while(rs.next())
            {
               return rs.getInt("count");
            }
                
            }
            catch (Exception e) {
                
            }
            return 0;
        }

    
    public boolean modifierUtilisateur(Utilisateur utilisateur){
        try {
            String sql ="UPDATE utilisateurs SET login=?,password=?, prenom=?, nom=?, profil=? WHERE id=?;";
            PreparedStatement stm = connex.prepareStatement(sql);
            stm.setString(1, utilisateur.getLogin());
            stm.setString(2, utilisateur.getPassword());
            stm.setString(3, utilisateur.getPrenom());
            stm.setString(4, utilisateur.getNom());
            stm.setString(5, utilisateur.getProfil());
            stm.setInt(6, utilisateur.getId());//pour le where id
            stm.executeUpdate();//Update dans la table utilisateurs
            return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
    }

    public boolean supprimerUtilisateur(int id){
        try {
            String sql ="DELETE FROM utilisateurs WHERE id=?;";
            PreparedStatement stm = connex.prepareStatement(sql);
            stm.setInt(1, id);//pour le where id
            stm.executeUpdate();//Delete dans la table utilisateurs
            return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
    }
   
    
    public void chargerUtilisateurs(DefaultTableModel dftm, JTable jtUtilisateurs){
        
        try {
            
            String sql ="SELECT * FROM utilisateurs;";
            PreparedStatement stm = connex.prepareCall(sql);
            ResultSet rs = stm.executeQuery();
            
            while(rs.next())
            {
                Vector v = new Vector();
                v.add(rs.getInt("id"));
                v.add(rs.getString("prenom"));
                v.add(rs.getString("nom"));
                v.add(rs.getString("login"));
                v.add(rs.getString("profil"));
                dftm.addRow(v);//remplir expl une ligne du tableau
            }
            
            jtUtilisateurs.setModel(dftm);//Chargment des colonnes et ligns sur le jtable
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
        public void chargerUtilisateurByLogin(String login, DefaultTableModel dftm, JTable jtUtilisateurs){
        
        try {
            
            String sql ="SELECT * FROM utilisateurs WHERE login=?;";
            PreparedStatement stm = connex.prepareCall(sql);
            stm.setString(1, login);
            ResultSet rs = stm.executeQuery();
            
            while(rs.next())
            {
                Vector v = new Vector();
                v.add(rs.getInt("id"));
                v.add(rs.getString("prenom"));
                v.add(rs.getString("nom"));
                v.add(rs.getString("login"));
                v.add(rs.getString("profil"));
                dftm.addRow(v);//remplir expl une ligne du tableau
            }
            
            jtUtilisateurs.setModel(dftm);//Chargment des colonnes et ligns sur le jtable
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
        
    public ArrayList<String> chargerFiliere(JComboBox cbFliere)
    {
        ArrayList<String> resultat = new ArrayList<String>();
        try {
            
            cbFliere.removeAllItems();//Vider le comboBox
            
            //SELECT nom de(s) colonne(s) FROM NOM_DE_LA_TABLE 
            String sql= "SELECT nom FROM formation;";
            PreparedStatement stm = connex.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            //Parcourir les resultats si existants
            while(rs.next())
            {
               String nom = rs.getString("nom");//récupére valeur à la colonne correspondante
               resultat.add(nom);
               cbFliere.addItem(nom); //Ajout dans le combobox
               
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultat;
    }
    
    
        
        
    
    public static void main(String[] args) {
        
       BDSchool bd= new BDSchool();
       bd.seConnecter("seck", "passer");
       
    }
}
