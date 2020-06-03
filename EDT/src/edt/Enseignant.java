package edt;

import java.util.*;


public class Enseignant extends Utilisateur{
    private ArrayList<String> cours;
    
    public Enseignant(int id, String email, String nom, String prenom, ArrayList<String> cours){
        super(id, email, nom, prenom, 3);
        this.cours = cours;
    }

    public ArrayList<String> getCours() {
        return cours;
    }

    public void setCours(ArrayList<String> cours) {
        this.cours = cours;
    }
}
