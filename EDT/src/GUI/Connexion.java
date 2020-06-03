/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import edt.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.*;
import java.util.*;
import javax.swing.*;

public class Connexion extends JFrame implements ActionListener {

    private final static String DB_URL = "jdbc:mysql://localhost/edt";
    private final static String USER = "root", PASS = "";
    private final int SIZE_X = 600, SIZE_Y = 400;
    private JButton buttonConnexion = new JButton();
    private final JTextField textFieldEmail = new JTextField();
    private final JPasswordField textFieldPass = new JPasswordField();

    public Connexion() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Connexion");
        this.setSize(SIZE_X, SIZE_Y);

        JPanel panel = new JPanel();
        // Set resizable ????
        //JPanel r = new JPanel();
        panel.setLayout(null);
        int xAlignement = 250;
        // Add Email Label
        JLabel labelEmail = new JLabel("E-Mail");
        labelEmail.setFont(new java.awt.Font("Avenir", 0, 15));
        Dimension size = labelEmail.getPreferredSize();
        labelEmail.setBounds(xAlignement, 70, size.width, size.height);
        panel.add(labelEmail);

        // Add Pass label
        JLabel labelPass = new JLabel("Mot de passe");
        labelPass.setFont(new java.awt.Font("Avenir", 0, 15));
        size = labelPass.getPreferredSize();
        labelPass.setBounds(xAlignement, 140, size.width, size.height);
        panel.add(labelPass);

        // Add Email
        textFieldEmail.setBounds(xAlignement, 95, 100, 20);
        panel.add(textFieldEmail);

        // Add password
        textFieldPass.setBounds(xAlignement, 165, 100, 20);
        panel.add(textFieldPass);

        // Add button connexion
        buttonConnexion = new JButton("Connexion");
        buttonConnexion.setBackground(new Color(210, 210, 210));
        buttonConnexion.setOpaque(true);
        size = buttonConnexion.getPreferredSize();
        buttonConnexion.setFont(new Font("Avenir", 1, 11));
        buttonConnexion.setBounds(xAlignement, 190, size.width, size.height);
        buttonConnexion.addActionListener(this);
        buttonConnexion.setSize(100, 25); // Set size (WIDTH, HEIGH)
        panel.add(buttonConnexion);

        this.add(panel);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonConnexion) {
            String username = textFieldEmail.getText().toLowerCase();
            char[] pass = textFieldPass.getPassword();
            String password = new String(pass);
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("Connecting to database...");
                conn = DriverManager.getConnection(DB_URL, USER, PASS);

                //STEP 4: Execute a query
                System.out.println("Creating statement...");

                stmt = conn.createStatement();
            } catch (SQLException se) {
                //Handle errors for JDBC
                se.printStackTrace();
            } catch (Exception es) {
                //Handle errors for Class.forName
                es.printStackTrace();
            }

            try {
                String sql = "SELECT * FROM `utilisateur` WHERE `PASSWD` = '" + password + "' AND "
                        + "`EMAIL`= '" + username + "';";
                rs = stmt.executeQuery(sql);

                int selectID = 0, droit = 0;
                String email = "", nom = "", prenom = "";
                while (rs.next()) {
                    selectID = rs.getInt("ID");
                    droit = rs.getInt("DROIT");
                    email = rs.getString("EMAIL");
                    nom = rs.getString("NOM");
                    prenom = rs.getString("PRENOM");
                }

                if (selectID == 0) {
                    // Send message to say that the connexion is not established
                    JOptionPane.showMessageDialog(null, "Incorect E-Mail or Password...");
                } else {
                    LocalDate debut, fin, now = LocalDate.now();
                    debut = now.minusDays(Funtions.convertWeekOfDay(now.getDayOfWeek()) - 1);
                    fin = debut.plusDays(6);
                    Seance seances[];
                    switch (droit) {
                        case 3:
                            sql = "SELECT cours.NOM FROM enseignant INNER JOIN cours ON cours.ID = enseignant.ID_UTILISATEUR WHERE enseignant.ID_UTILISATEUR = " + selectID;
                            rs = stmt.executeQuery(sql);
                            ArrayList<String> cours = new ArrayList<>();
                            while (rs.next()) {
                                cours.add(rs.getString("NOM"));
                            }
                            Cache.utilisateur = new Enseignant(selectID, email, nom, prenom, cours);
                            seances = Funtions.seancesEnseignant(selectID, 1, debut, fin);
                            //               new Edt(800, 600, debut, seances);
                            this.setVisible(false);
                            break;
                        case 4:
                            sql = "SELECT groupe.NOM FROM etudiant INNER JOIN groupe ON groupe.ID = etudiant.ID_GROUPE WHERE etudiant.ID_UTILISATEUR =" + selectID;
                            rs = stmt.executeQuery(sql);
                            String groupe = "";
                            while (rs.next()) {
                                groupe = rs.getString("NOM");
                            }
                            Cache.utilisateur = new Etudiant(selectID, email, nom, prenom, groupe);
                            seances = Funtions.seancesUtilisateur(selectID, 1, debut, fin);
                            //             new Edt(800, 600, debut, seances);
                            break;
                        default:
                            Cache.utilisateur = new Utilisateur(selectID, email, nom, prenom, droit);
                            // A VOIR OU LES REDIRIGER
                            break;
                    }
                    this.setVisible(false);
                    this.dispose();
                }
            } catch (SQLException se) {
                //Handle errors for JDBC
                se.printStackTrace();
            } catch (Exception es) {
                //Handle errors for Class.forName
                es.printStackTrace();
            }

            try {
                System.out.print("Closing connection...");
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException se) {
                //Handle errors for JDBC
                se.printStackTrace();
            } catch (Exception es) {
                //Handle errors for Class.forName
                es.printStackTrace();
            } finally {
                //finally block used to close resources
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (SQLException se2) {
                }// nothing we can do
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }//end finally try
            }//end try   
        }

    }
}
