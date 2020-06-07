package Model;

public class Utilisateur {
    protected String email, nom, prenom;
    protected int droit, id;
    
    public Utilisateur(int id, String email, String nom, String prenom, int droit){
        this.id = id;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.droit = droit;
    }

    public String getEmail() {
        return email;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public int getDroit() {
        return droit;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setDroit(int droit) {
        this.droit = droit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
}
