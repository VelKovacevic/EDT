package Vue;

import Model.Seance;
import Model.Funtions;
import java.awt.event.*;
import java.sql.*;
import java.time.*;
import java.util.*;
import javax.swing.*;

public class EditNom extends JFrame implements ActionListener{
    private final int SIZE_X = 300, SIZE_Y = 200;
    private JTextField nomCours;
    private JButton cancel, modifier;
    private JComboBox box, boxCours;
    private LocalDate debut; 
    private String nom;
    private int id, type;
    private Seance seance;
    
    public EditNom(Seance seance, LocalDate debut, int id, String nom, int type){
        this.seance = seance;
        this.debut = debut;
        this.id = id;
        this.nom = nom;
        this.type = type;
        
        this.setTitle("Changer de nom");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(SIZE_X, SIZE_Y);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        
        JLabel crs = new JLabel("Titre du cours");
        crs.setBounds(40, 35, crs.getPreferredSize().width, 20);
        panel.add(crs);
        
        JLabel typeC = new JLabel("Type");
        typeC.setBounds(170, 35, typeC.getPreferredSize().width, 20);
        panel.add(typeC);
        
        modifier = new JButton("Modifier");
        modifier.addActionListener(this);
        modifier.setBounds(55, 95, modifier.getPreferredSize().width, 25);
        panel.add(modifier);
        
        cancel = new JButton("Annuler");
        cancel.addActionListener(this);
        cancel.setBounds(65 + modifier.getPreferredSize().width, 95 , cancel.getPreferredSize().width, 25);
        panel.add(cancel);
        
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
            String sql = "SELECT NOM FROM type_cours;";
            rs = stmt.executeQuery(sql);
            ArrayList<String> typeCours = new ArrayList<>();

            while(rs.next()){
                typeCours.add(rs.getString("NOM"));
            }
            box = new JComboBox(typeCours.toArray());
            box.setBounds(170, 60, box.getPreferredSize().width, 25);
            panel.add(box);
            box.setSelectedItem(seance.getTypeCours());
            
            
            sql = "SELECT NOM FROM cours";
            rs = stmt.executeQuery(sql);
            ArrayList<String> nomCours = new ArrayList<>();
            while(rs.next()){
                nomCours.add(rs.getString("NOM"));
            }
            boxCours = new JComboBox(nomCours.toArray());
            boxCours.setBounds(40, 60, 120, 25);
            boxCours.setSelectedItem(seance.getCours());
            panel.add(boxCours);
            
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
        }else if(e.getSource() == modifier){
            
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
                String COURSTYPE = box.getItemAt(box.getSelectedIndex()).toString();
                String COURSNOM = boxCours.getItemAt(box.getSelectedIndex()).toString();
                String sql = "SELECT ID FROM cours WHERE cours.NOM = \"" + COURSNOM + "\"";
                rs = stmt.executeQuery(sql);
                int idCoursNom = 0;
                while(rs.next()){
                    idCoursNom = rs.getInt("ID");
                }
                
                sql = "SELECT ID FROM type_cours WHERE type_cours.NOM = \"" + COURSTYPE + "\"";
                rs = stmt.executeQuery(sql);
                int idCoursType = 0;
                while(rs.next()){
                    idCoursType = rs.getInt("ID");
                }
                
                sql = "UPDATE `seance` SET `ID_TYPE` = '" + idCoursType + "' , `ID_COURS` = '" + idCoursNom + "' WHERE `seance`.`ID` = " + seance.getId();
                stmt.executeUpdate(sql);
                // MAJ REUSSI !
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
