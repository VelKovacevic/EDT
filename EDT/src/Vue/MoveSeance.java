package Vue;

import Model.Enseignant;
import Model.Location;
import Model.Seance;
import Model.Groupe;
import Model.Funtions;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.*;
import javax.swing.*;

public class MoveSeance extends JFrame implements ActionListener{
    private final int SIZE_X = 300, SIZE_Y = 300;
    private JTextField textDate, textDebut, textFin;
    private JButton cancel, validate;
    private LocalDate debut; 
    private String nom;
    private int id, type;
    private Seance seance;
    
    public MoveSeance(Seance seance, LocalDate debut, int id, String nom, int type){
        this.seance = seance;
        this.debut = debut;
        this.id = id;
        this.nom = nom;
        this.type = type;
        
        this.setTitle("Modifier la date");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(SIZE_X, SIZE_Y);
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        textDate = new JTextField();
        textDate.setBounds(80, 60, 120, 25);
        panel.add(textDate);
        textDate.setText(seance.getDate().toString());
        
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
        textDebut.setText(seance.getDebut().getHour() + ":" + seance.getDebut().getMinute());
        
        textFin = new JTextField();
        textFin.setBounds(80, 160, 120, 25);
        panel.add(textFin);
        textFin.setText(seance.getFin().getHour() + ":" + seance.getFin().getMinute());
        
        cancel = new JButton("Annuler");
        cancel.addActionListener(this);
        cancel.setBounds(60, 200, cancel.getPreferredSize().width, 23);
        panel.add(cancel);
        
        validate = new JButton("Valider");
        validate.addActionListener(this);
        validate.setBounds(70 + cancel.getPreferredSize().width, 200, validate.getPreferredSize().width, 23);
        panel.add(validate);
        
        this.add(panel);
        
        // Afficher le frame
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
                            Groupe[] grps = seance.getGroupes();
                            if(grps != null){
                                for(int i=0; i<grps.length; ++i){
                                    String sql = "SELECT\n" +
                                            "	groupe.ID\n" +
                                            "FROM\n" +
                                            "	groupe\n" +
                                            "JOIN \n" +
                                            "	promotion ON groupe.ID_PROMOTION = promotion.ID\n" +
                                            "WHERE\n" +
                                            "	promotion.NOM = \"" + grps[i].getPromotion() + "\" AND groupe.NOM = \"" + grps[i].getNom() + "\"";
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
                                }
                            }
                            
                            if(groupeIndispo){
                                JOptionPane.showMessageDialog(null, "GROUP INDISPONIBLE POUR CE CRENEAU");
                            }else{
                                Enseignant[] ens = seance.getEnseignants();
                                boolean ensIndispo = false;
                                if(ens != null){
                                    for(int i=0; i<ens.length; ++i){
                                        String sql = "SELECT\n" +
                                                "	COUNT(*) AS TOTAL\n" +
                                                "FROM \n" +
                                                "	seance_enseignants\n" +
                                                "JOIN\n" +
                                                "	seance ON seance.ID = seance_enseignants.ID_SEANCE\n" +
                                                "WHERE \n" +
                                                "	seance_enseignants.ID_ENSEIGNANT = " + ens[i].getId() + " AND seance.DATE = \"" + dateT + "\" AND ((seance.HEURE_DEBUT < \"" + finT + "\"  AND seance.HEURE_DEBUT >= \"" + debutT + "\") OR (seance.HEURE_FIN <= \"" + finT + "\"  AND seance.HEURE_DEBUT > \"" + debutT + "\"))";
                                        rs = stmt.executeQuery(sql);
                                        int total = 2;
                                        while(rs.next()){
                                            total = rs.getInt("TOTAL");
                                        }
                                        if(total != 0)
                                            ensIndispo = true;
                                    }
                                }
                                if(ensIndispo){
                                    JOptionPane.showMessageDialog(null, "ENSEIGNANT INDISPONIBLE SUR CE CRENEAU");
                                }else{
                                    Location[] salle = seance.getLieu();
                                    boolean salleIndispo = false;
                                    if(salle != null){
                                        for(int i=0; i<salle.length; ++i){
                                            String sql = "SELECT\n" +
                                                    "	salle.ID\n" +
                                                    "FROM\n" +
                                                    "	salle\n" +
                                                    "JOIN\n" +
                                                    "	site ON salle.ID_SITE = site.ID\n" +
                                                    "WHERE\n" +
                                                    "	salle.NOM = \"" + salle[i].getSalle() + "\" AND site.NOM = \"" + salle[i].getSite() + "\"";
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
                                            int total = 2;
                                            while(rs.next()){
                                                total = rs.getInt("TOTAL");
                                            }
                                            if(total != 0)
                                                salleIndispo = true;
                                            if(salleIndispo){
                                                JOptionPane.showMessageDialog(null, "SALLE INDISPONIBLE POUR CE CRENEAU");
                                            }else{
                                                sql = "UPDATE `seance` SET `HEURE_DEBUT` = '"+debutT+":00', `HEURE_FIN` = '"+finT+":00', `DATE`='"+dateT+"' WHERE `seance`.`ID` = '" + seance.getId()+"'";
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
