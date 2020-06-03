
package edt;


public class Etudiant extends Utilisateur{
    private String groupe;
    
    public Etudiant(int id, String email, String nom, String prenom, String groupe){
        super(id, email, nom, prenom, 4);
        this.groupe = groupe;
    }

    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }
    
    
}
