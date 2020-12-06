
package entities;

import java.util.ArrayList;

public class Utilisateur {
    
    private int id;
    private String login;
    private String password;
    private String prenom;
    private String nom;
    private String profil;
    public ArrayList<String> getAttributeArray() {
        ArrayList<String> resultat = new ArrayList<String>();
        resultat.add(String.valueOf(this.getId()));
        resultat.add(this.getLogin());
        resultat.add(this.getPassword());
        resultat.add(this.getNom());
        resultat.add(this.getPrenom());
        resultat.add(this.getProfil());
        return resultat;
    }
     public ArrayList<String> getAttributeArrayNoms() {
        ArrayList<String> resultat = new ArrayList<>();
        resultat.add("id");
        resultat.add("login");
        resultat.add("password");
        resultat.add("nom");
        resultat.add("prenom");
        resultat.add("type");
        return resultat;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }
    
    
   
}
