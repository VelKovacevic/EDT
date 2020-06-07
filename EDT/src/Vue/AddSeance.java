package Vue;

import Model.Seance;
import Model.Funtions;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import javax.swing.*;

public class AddSeance extends JFrame implements ActionListener{
    private final int SIZE_X = 300, SIZE_Y = 550;
    private JComboBox typeCours, nomCours, groupe, enseignant, salle;
    private JTextField textDebut, textDate, textFin;
    private JButton cancel, validate;
    private LocalDate debut; 
    private String nom;
    private int id, type;
    private Seance seance;
    
    public AddSeance(Seance seance, LocalDate debut, int id, String nom, int type){
        this.seance = seance;
        this.debut = debut;
        this.id = id;
        this.nom = nom;
        this.type = type;
        
        this.setTitle("Ajouter une seance");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(SIZE_X, SIZE_Y);
    
        JPanel panel = new JPanel();
        panel.setLayout(null);
        
        textDate = new JTextField();
        textDate.setBounds(80, 60, 120, 25);
        panel.add(textDate);
        
        JLabel lab1 = new JLabel("Date (YYYY-MM-DD)");
        lab1.setBounds(80, 37, 120,20);
        panel.add(lab1);

        JLabel lab2 = new JLabel("Heure début (HH:MM)");
        lab2.setBounds(80, 87, 120,20);
        panel.add(lab2);

        JLabel lab3 = new JLabel("Heure fin (HH:MM)");
        lab3.setBounds(80, 137, 120,20);
        panel.add(lab3);
        
        textDebut = new JTextField();
        textDebut.setBounds(80, 110, 120, 25);
        panel.add(textDebut);
        
        textFin = new JTextField();
        textFin.setBounds(80, 160, 120, 25);
        panel.add(textFin);
    
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(Funtions.DB_URL, Funtions.USER, Funtions.PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");

            stmt = conn.createStatement();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception es){
            //Handle errors for Class.forName
            es.printStackTrace();
        }
        try{
            String sql = "SELECT NOM FROM cours";
            rs = stmt.executeQuery(sql);
            ArrayList<String> typ = new ArrayList<>();
            while(rs.next()){
                typ.add(rs.getString("NOM"));
            }
            JLabel lab4 = new JLabel("Nom du cours");
            lab4.setBounds(80, 187, 120,20);
            panel.add(lab4);
            nomCours = new JComboBox(typ.toArray());
            nomCours.setBounds(80, 210, 120, 25);
            panel.add(nomCours);
            
            sql = "SELECT NOM FROM type_cours";
            rs = stmt.executeQuery(sql);
            ArrayList<String> cour = new ArrayList<>();
            while(rs.next()){
                cour.add(rs.getString("NOM"));
            }
            JLabel lab5 = new JLabel("Type de cours");
            lab5.setBounds(80, 237, 120,20);
            panel.add(lab5);
            typeCours = new JComboBox(cour.toArray());
            typeCours.setBounds(80, 260, 120, 25);
            panel.add(typeCours);
            
            sql = "SELECT\n" +
                    "	utilisateur.NOM,\n" +
                    "    utilisateur.PRENOM\n" +
                    "FROM \n" +
                    "	utilisateur\n" +
                    "JOIN\n" +
                    "	enseignant ON enseignant.ID_UTILISATEUR = utilisateur.ID";
            rs = stmt.executeQuery(sql);
            ArrayList<String> ens = new ArrayList<>(); 
            while(rs.next()){
                ens.add(rs.getString("NOM") + " - " + rs.getString("PRENOM"));
            }
            
            JLabel lab6 = new JLabel("Enseignant");
            lab6.setBounds(80, 287, 120,20);
            panel.add(lab6);
            enseignant = new JComboBox(ens.toArray());
            enseignant.setBounds(80, 310, 120, 25);
            panel.add(enseignant);
            
            sql = "SELECT\n"
                    + "groupe.NOM, promotion.NOM AS SITENOM\n"
                    + "FROM\n"
                    + "groupe\n"
                    + "JOIN\n"
                    + "promotion ON promotion.ID = groupe.ID_PROMOTION\n";
            
            rs = stmt.executeQuery(sql);
            ArrayList<String> grp = new ArrayList<>(); 
            while(rs.next()){
                grp.add(rs.getString("SITENOM") + " - " + rs.getString("NOM"));
            }
            JLabel lab7 = new JLabel("Groupe");
            lab7.setBounds(80, 337, 120,20);
            panel.add(lab7);
            groupe = new JComboBox(grp.toArray());
            groupe.setBounds(80, 360, 120, 25);
            panel.add(groupe);
            
            sql = "SELECT\n"
                    + "salle.NOM, site.NOM AS SITENOM\n"
                    + "FROM\n"
                    + "salle\n"
                    + "JOIN\n"
                    + "site ON site.ID = salle.ID_SITE\n";
            
            rs = stmt.executeQuery(sql);
            ArrayList<String> sal = new ArrayList<>(); 
            while(rs.next()){
                sal.add(rs.getString("SITENOM") + " - " + rs.getString("NOM"));
            }
            JLabel lab8 = new JLabel("Salle");
            lab8.setBounds(80, 387, 120,20);
            panel.add(lab8);
            salle = new JComboBox(sal.toArray());
            salle.setBounds(80, 410, 120, 25);
            panel.add(salle);
            
            
            cancel = new JButton("Annuler");
            cancel.setBounds(60, 450, cancel.getPreferredSize().width, 23);
            panel.add(cancel);

            validate = new JButton("Ajouter");
            validate.setBounds(70 + cancel.getPreferredSize().width, 450, validate.getPreferredSize().width, 23);
            panel.add(validate);
            
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception es){
            //Handle errors for Class.forName
            es.printStackTrace();
        }

        try{
            System.out.print("Closing connection...");
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception es){
            //Handle errors for Class.forName
            es.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try   
        
        
        this.add(panel);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == cancel){
            switch (type) {
                case 1:
                    {
                        Seance[] seances = Funtions.seancesUtilisateur(id, 1, debut, debut.plusDays(6));
                        Seance[] seancesAnnule = Funtions.seancesUtilisateur(id, 2, debut, debut.plusDays(6));
                        new ListEdtAdmin(seancesAnnule, seances, debut, id, nom, type);
                        this.setVisible(false);
                        this.dispose();
                        break;
                    }
                case 2:
                    {
                        Seance[] seances = Funtions.seancesEnseignant(id, 1, debut, debut.plusDays(6));
                        Seance[] seancesAnnule = Funtions.seancesEnseignant(id, 2, debut, debut.plusDays(6));
                        new ListEdtAdmin(seancesAnnule, seances, debut, id, nom, type);
                        this.setVisible(false);
                        this.dispose();
                        break;
                    }
                case 3:
                    { 
                        Seance[] seances = Funtions.seancesSalle(id, 1, debut, debut.plusDays(6));
                        Seance[] seancesAnnule = Funtions.seancesSalle(id, 2, debut, debut.plusDays(6));
                        new ListEdtAdmin(seancesAnnule, seances, debut, id, nom, type);
                        this.setVisible(false);
                        this.dispose();
                        break;
                    }
                default:
                    break;
            }
        }else if(e.getSource() == validate){
            String dateT, debutT, finT, splitDate[], splitDebut[], splitFin[];
            dateT = textDate.getText();
            debutT = textDebut.getText();
            finT = textFin.getText();
            
            splitDate = dateT.split("-");
            splitDebut = debutT.split(":");
            splitFin = finT.split(":");
            
            if(splitDate.length != 3){
                JOptionPane.showMessageDialog(null, "FORMAT DATE INCORECT");
            }else{
                if(splitDebut.length != 2){
                    JOptionPane.showMessageDialog(null, "FORMAT HEURE DEBUT INCORRECT");
                }else{
                    if(splitFin.length != 2){
                        JOptionPane.showMessageDialog(null, "FORMAT HEURE FIN INCORRECT");
                    }else{
                        boolean groupeIndispo = false;
                        Connection conn = null;
                        Statement stmt = null;
                        ResultSet rs = null;
                        try{
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            System.out.println("Connecting to database...");
                            conn = DriverManager.getConnection(Funtions.DB_URL, Funtions.USER, Funtions.PASS);

                            //STEP 4: Execute a query
                            System.out.println("Creating statement...");

                            stmt = conn.createStatement();
                        }catch(SQLException se){
                            //Handle errors for JDBC
                            se.printStackTrace();
                        }catch(Exception es){
                            //Handle errors for Class.forName
                            es.printStackTrace();
                        }
                        try{
                            String gro = groupe.getSelectedItem().toString();
                            String[] splitGroupe = gro.split(" - ");
                            
                            String sql = "SELECT\n" +
                                    "	groupe.ID\n" +
                                    "FROM\n" +
                                    "	groupe\n" +
                                    "JOIN \n" +
                                    "	promotion ON groupe.ID_PROMOTION = promotion.ID\n" +
                                    "WHERE\n" +
                                    "	promotion.NOM = \"" + splitGroupe[0] + "\" AND groupe.NOM = \"" + splitGroupe[1] + "\"";
                            rs = stmt.executeQuery(sql);
                            int idgrpe = 0;
                            while(rs.next()){
                                idgrpe = rs.getInt("ID");
                            }


                            sql = "SELECT\n" +
                                    "	COUNT(*) AS TOTAL\n" +
                                    "FROM \n" +
                                    "	seance_groupe\n" +
                                    "JOIN\n" +
                                    "	seance ON seance.ID = seance_groupe.ID_SEANCE\n" +
                                    "WHERE \n" +
                                    "	seance_groupe.ID_GROUPE = " + idgrpe + " AND seance.DATE = \"" + dateT + "\" AND ((seance.HEURE_DEBUT < \"" + finT + "\"  AND seance.HEURE_DEBUT >= \"" + debutT + "\") OR (seance.HEURE_FIN <= \"" + finT + "\"  AND seance.HEURE_DEBUT > \"" + debutT + "\"))";
                            rs = stmt.executeQuery(sql);
                            int total = 2;
                            while(rs.next()){
                                total = rs.getInt("TOTAL");
                            }
                            if(total != 0)
                                groupeIndispo = true;
                                
                            
                            
                            if(groupeIndispo){
                                JOptionPane.showMessageDialog(null, "GROUP INDISPONIBLE POUR CE CRENEAU");
                            }else{
                                String ens = enseignant.getSelectedItem().toString();
                                String[] splitEnseignant = ens.split(" - ");
                                
                                sql = "SELECT\n" +
                                    "	enseignant.ID\n" +
                                    "FROM\n" +
                                    "	enseignant\n" +
                                    "JOIN \n" +
                                    "	utilisateur ON utilisateur.ID = enseignant.ID_UTILISATEUR\n" +
                                    "WHERE\n" +
                                    "	utilisateur.NOM = \"" + splitEnseignant[0] + "\" AND utilisateur.PRENOM = \"" + splitEnseignant[1] + "\"";
                                rs = stmt.executeQuery(sql);
                                int idens = 0;
                                while(rs.next()){
                                    idens = rs.getInt("ID");
                                }
                                boolean ensIndispo = false;
                                sql = "SELECT\n" +
                                        "	COUNT(*) AS TOTAL\n" +
                                        "FROM \n" +
                                        "	seance_enseignants\n" +
                                        "JOIN\n" +
                                        "	seance ON seance.ID = seance_enseignants.ID_SEANCE\n" +
                                        "WHERE \n" +
                                        "	seance_enseignants.ID_ENSEIGNANT = " + idens + " AND seance.DATE = \"" + dateT + "\" AND ((seance.HEURE_DEBUT < \"" + finT + "\"  AND seance.HEURE_DEBUT >= \"" + debutT + "\") OR (seance.HEURE_FIN <= \"" + finT + "\"  AND seance.HEURE_DEBUT > \"" + debutT + "\"))";
                                rs = stmt.executeQuery(sql);
                                total = 2;
                                while(rs.next()){
                                    total = rs.getInt("TOTAL");
                                }
                                if(total != 0)
                                    ensIndispo = true;
                                
                                if(ensIndispo){
                                    JOptionPane.showMessageDialog(null, "ENSEIGNANT INDISPONIBLE SUR CE CRENEAU");
                                }else{
                                    String sal = enseignant.getSelectedItem().toString();
                                    String[] splitSalle = sal.split(" - ");
                                    boolean salleIndispo = false;
                                    
                                    sql = "SELECT\n" +
                                            "	salle.ID\n" +
                                            "FROM\n" +
                                            "	salle\n" +
                                            "JOIN\n" +
                                            "	site ON salle.ID_SITE = site.ID\n" +
                                            "WHERE\n" +
                                            "	salle.NOM = \"" + splitSalle[1] + "\" AND site.NOM = \"" + splitSalle[0] + "\"";
                                    rs = stmt.executeQuery(sql);
                                    int idsalle = 0;
                                    while(rs.next()){
                                        idsalle = rs.getInt("ID");
                                    }
                                    sql = "SELECT\n" +
                                            "	COUNT(*) AS TOTAL\n" +
                                            "FROM \n" +
                                            "	seance_salles\n" +
                                            "JOIN\n" +
                                            "	seance ON seance.ID = seance_salles.ID_SEANCE\n" +
                                            "WHERE \n" +
                                            "	seance_salles.ID_SALLE = " + idsalle + " AND seance.DATE = \"" + dateT + "\" AND ((seance.HEURE_DEBUT < \"" + finT + "\"  AND seance.HEURE_DEBUT >= \"" + debutT + "\") OR (seance.HEURE_FIN <= \"" + finT + "\"  AND seance.HEURE_DEBUT > \"" + debutT + "\"))";
                                    rs = stmt.executeQuery(sql);
                                    total = 2;
                                    while(rs.next()){
                                        total = rs.getInt("TOTAL");
                                    }
                                    if(total != 0)
                                        salleIndispo = true;
                                    if(salleIndispo){
                                        JOptionPane.showMessageDialog(null, "SALLE INDISPONIBLE POUR CE CRENEAU");
                                    }else{
                                        sql = "UPDATE `seance` SET `HEURE_DEBUT` = '"+debutT+":00', `HEURE_FIN` = '"+finT+":00', `DATE='"+dateT+"' WHERE `seance`.`ID` = " + seance.getId();
                                        
                                        sql = "SELECT ID FROM cours WHERE NOM = \"" + nomCours.getSelectedItem().toString() + "\"";
                                        rs = stmt.executeQuery(sql);
                                        int idCours = 0;
                                        while(rs.next()){
                                            idCours = rs.getInt("ID");
                                        }
                                        
                                        sql = "SELECT ID FROM type_cours WHERE NOM = \"" + typeCours.getSelectedItem().toString() + "\"";
                                        rs = stmt.executeQuery(sql);
                                        int idType = 0;
                                        while(rs.next()){
                                            idType = rs.getInt("ID");
                                        }
                                        
                                        sql = "INSERT INTO `seance` (`ID`, `SEMAINE`, `DATE`, `HEURE_DEBUT`, `HEURE_FIN`, `ETAT`, `ID_COURS`, `ID_TYPE`) VALUES (NULL, '20', '" + dateT + "', '"+debutT+":00', '" + finT + ":00', '1', '" + idCours + "', '" + idType + "');";
                                        stmt.executeUpdate(sql);
                                        sql = "SELECT MAX(ID) AS MAX FROM seance";
                                        rs = stmt.executeQuery(sql);
                                        int seanceID = 0;
                                        while(rs.next()){
                                            seanceID = rs.getInt("MAX");
                                        }
                                        sql = "INSERT INTO `seance_enseignants` (`ID_SEANCE`, `ID_ENSEIGNANT`) VALUES ('" + seanceID + "', '" + idens + "');";
                                        stmt.executeUpdate(sql);
                                        
                                        sql = "INSERT INTO `seance_groupe` (`ID_SEANCE`, `ID_GROUPE`) VALUES ('" + seanceID + "', '" + idgrpe + "');";
                                        stmt.executeUpdate(sql);
                                        
                                        sql = "INSERT INTO `seance_salles` (`ID_SEANCE`, `ID_SALLE`) VALUES ('" + seanceID + "', '" + idsalle + "');";
                                        stmt.executeUpdate(sql);
                                        
                                        // UPDATE OK
                                        switch (type) {
                                            case 1:
                                                {
                                                    Seance[] seances = Funtions.seancesUtilisateur(id, 1, debut, debut.plusDays(6));
                                                    Seance[] seancesAnnule = Funtions.seancesUtilisateur(id, 2, debut, debut.plusDays(6));
                                                    new ListEdtAdmin(seancesAnnule, seances, debut, id, nom, type);
                                                    this.setVisible(false);
                                                    this.dispose();
                                                    break;
                                                }
                                            case 2:
                                                {
                                                    Seance[] seances = Funtions.seancesEnseignant(id, 1, debut, debut.plusDays(6));
                                                    Seance[] seancesAnnule = Funtions.seancesEnseignant(id, 2, debut, debut.plusDays(6));
                                                    new ListEdtAdmin(seancesAnnule, seances, debut, id, nom, type);
                                                    this.setVisible(false);
                                                    this.dispose();
                                                    break;
                                                }
                                            case 3:
                                                {
                                                    Seance[] seances = Funtions.seancesSalle(id, 1, debut, debut.plusDays(6));
                                                    Seance[] seancesAnnule = Funtions.seancesSalle(id, 2, debut, debut.plusDays(6));
                                                    new ListEdtAdmin(seancesAnnule, seances, debut, id, nom, type);
                                                    this.setVisible(false);
                                                    this.dispose();
                                                    break;
                                                }
                                            default:
                                                break;
                                        }
                                    }
                                       
                                }
                            }
                        }catch(SQLException se){
                            //Handle errors for JDBC
                            se.printStackTrace();
                        }catch(Exception es){
                            //Handle errors for Class.forName
                            es.printStackTrace();
                        }

                        try{
                            System.out.print("Closing connection...");
                            rs.close();
                            stmt.close();
                            conn.close();
                        }catch(SQLException se){
                            //Handle errors for JDBC
                            se.printStackTrace();
                        }catch(Exception es){
                            //Handle errors for Class.forName
                            es.printStackTrace();
                        }finally{
                            //finally block used to close resources
                            try{
                                if(stmt!=null)
                                    stmt.close();
                            }catch(SQLException se2){
                            }// nothing we can do
                            try{
                                if(conn!=null)
                                    conn.close();
                            }catch(SQLException se){
                                se.printStackTrace();
                            }//end finally try
                        }//end try   

                        
                    }
                }
            }   
        }
    }
}
