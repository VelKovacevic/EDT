package Vue;

import Model.Seance;
import Model.Funtions;
import java.awt.event.*;
import java.sql.*;
import java.time.*;
import javax.swing.*;


public class AddEnseignant extends JFrame implements ActionListener{
    private final int SIZE_X = 300, SIZE_Y = 200;
//    
//        this.setTitle("Ajout d'enseignant");
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setSize(SIZE_X, SIZE_Y);
//        
//        
//        
//        
//        // Afficher le frame
//        this.setLocationRelativeTo(null);
//        this.setVisible(true);
    private JButton cancel, ajouter;
    private JTextField saisie;
    private LocalDate debut; 
    private String nom;
    private int id, type;
    private Seance seance;
    
    public AddEnseignant(Seance seance, LocalDate debut, int id, String nom, int type){
        this.seance = seance;
        this.debut = debut;
        this.id = id;
        this.nom = nom;
        this.type = type;
        
        this.setTitle("Ajout d'enseignant");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(SIZE_X, SIZE_Y);
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        JLabel label = new JLabel("Veuillez entrer le nom (nom prenom)");
        label.setBounds(40, 50, label.getPreferredSize().width, 20);
        panel.add(label);
        
        cancel = new JButton("Annuler");
        cancel.addActionListener(this);
        cancel.setBounds(60, 120, cancel.getPreferredSize().width, 20);
        panel.add(cancel);
        
        ajouter = new JButton("Ajouter");
        ajouter.addActionListener(this);
        ajouter.setBounds(70 + cancel.getPreferredSize().width, 120, ajouter.getPreferredSize().width, 20);
        panel.add(ajouter);
        
        saisie = new JTextField();
        saisie.setBounds(40, 80, label.getPreferredSize().width, 25);
        panel.add(saisie);
        
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
        }else if(e.getSource() == ajouter){
            
            
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
                String nomEntier = saisie.getText();
                String[] split = nomEntier.split(" ");
                if(split.length > 1){
                    String nom = split[0], prenom = split[split.length - 1];
                    String sql = "SELECT\n" +
                            "	enseignant.ID_UTILISATEUR\n" +
                            "FROM\n" +
                            "	utilisateur\n" +
                            "JOIN\n" +
                            "	enseignant ON utilisateur.ID = enseignant.ID_UTILISATEUR\n" +
                            "WHERE\n" +
                            "	utilisateur.NOM LIKE \"%"+ nom + "%\" AND utilisateur.PRENOM LIKE \"%" + prenom +"%\"";
                    
                    rs = stmt.executeQuery(sql);
                    int idEns = 0;
                    while(rs.next()){
                        idEns = rs.getInt("ID_UTILISATEUR");
                    }
                    if(idEns == 0){
                        JOptionPane.showMessageDialog(null, "Pas d'enseignant avec ce nom");
                    }else{
                        sql = "SELECT\n" +
                            "	COUNT(*) AS TOTAL\n" +
                            "FROM\n" +
                            "	seance_enseignants\n" +
                            "WHERE\n" +
                            "	seance_enseignants.ID_SEANCE = " + seance.getId() + " AND seance_enseignants.ID_ENSEIGNANT = " + idEns;
                        rs = stmt.executeQuery(sql);
                        int total = 1;
                        while(rs.next()){
                            total = rs.getInt("TOTAL");
                        }
                        if(total != 0){
                            JOptionPane.showMessageDialog(null, "Cet enseignant est deja affecter a cette seance");
                        }else{
                            boolean ensIndispo = false;
                            String finT = seance.getFin().getHour() + ":" + seance.getFin().getMinute()
                                    , debutT = seance.getDebut().getHour() + ":" + seance.getDebut().getMinute();
                            sql = "SELECT\n" +
                                    "	COUNT(*) AS TOTAL\n" +
                                    "FROM \n" +
                                    "	seance_enseignants\n" +
                                    "JOIN\n" +
                                    "	seance ON seance.ID = seance_enseignants.ID_SEANCE\n" +
                                    "WHERE \n" +
                                    "	seance_enseignants.ID_ENSEIGNANT = " + idEns + " AND seance.DATE = \"" + seance.getDate().toString() + "\" AND ((seance.HEURE_DEBUT < \"" + finT + "\"  AND seance.HEURE_DEBUT >= \"" + debutT + "\") OR (seance.HEURE_FIN <= \"" + finT + "\"  AND seance.HEURE_DEBUT > \"" + debutT + "\"))";
                            rs = stmt.executeQuery(sql);
                            total = 2;
                            while(rs.next()){
                                total = rs.getInt("TOTAL");
                            }
                            if(total != 0)
                                ensIndispo = true;
                            if(ensIndispo){
                                sql = "INSERT INTO `seance_enseignants` (`ID_SEANCE`, `ID_ENSEIGNANT`) VALUES ('" + seance.getId() + "', '" + idEns + "');";
                                stmt.executeUpdate(sql);
                                // BIEN AJOUTE !
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
