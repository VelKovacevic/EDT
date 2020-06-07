package Model;

public class Location {
    private String site, salle;
    
    public Location(String site, String salle){
        this.site = site;
        this.salle = salle;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSalle() {
        return salle;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }
    
    
}
