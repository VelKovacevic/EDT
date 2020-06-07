package Model;

public class Groupe {
    private String promotion, nom;
    
    public Groupe(String nom, String promotion){
        this.promotion = promotion;
        this.nom = nom;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
    
}
