package Vue;

import Model.Seance;
import Model.Funtions;
import Model.Cache;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.*;
import javax.swing.*;

public class PageDates extends JFrame implements ActionListener{
    private final int SIZE_X = 300, SIZE_Y = 220;
    private JTextField textDateDeb, textDateFin;
    private JButton cancel, validate;
    private LocalDate debut; 
    private String nom;
    private int id, type;
    private Seance[] seance, seancesAnnule;
    private JMenuBar mb = new JMenuBar();
    private JMenu submenu = new JMenu("Emploi du temps");
    private JMenu deco = new JMenu("Deconnexion");
    private JMenuItem vertical = new JMenuItem("EDT Vertical");
    private JMenuItem horizontal = new JMenuItem("EDT Horizontal");
    private JMenuItem liste = new JMenuItem("EDT Liste");
    private JMenuItem deconnexion = new JMenuItem("Deconnexion");
    private JMenu recap = new JMenu("Recapitulatif");
    private JMenuItem courS = new JMenuItem("Cours");
    private JMenu recherche =new JMenu("Recherche");
    private JMenuItem  rechercher= new JMenuItem("Rechercher");
    private JMenu admin = new JMenu("Admin");
    private JMenuItem listeEDT = new JMenuItem("EDT Liste");
    
    public PageDates(Seance[] seanceAnnule, Seance[] seance, LocalDate debut, int id, String nom, int type){
        this.seance = seance;
        this.debut = debut;
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.seancesAnnule = seancesAnnule;
        
        this.setTitle("Choix de dates");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(SIZE_X, SIZE_Y);
        vertical.addActionListener(this);
        horizontal.addActionListener(this);
        liste.addActionListener(this);
        deconnexion.addActionListener(this);
        
        submenu.add(vertical);
        submenu.add(horizontal);
        submenu.add(liste);
        deco.add(deconnexion);
        mb.add(submenu);
        if(type == 2){
            courS.addActionListener(this);
            recap.add(courS);
            mb.add(recap);
        }
        if(Cache.utilisateur.getDroit() == 1){
            listeEDT.addActionListener(this);
            admin.add(listeEDT);
            mb.add(admin);
        }
        if(Cache.utilisateur.getDroit() == 1 || Cache.utilisateur.getDroit() == 2){
            rechercher.addActionListener(this);
            recherche.add(rechercher);
            mb.add(rechercher);
        }
        mb.add(deco);
        this.setJMenuBar(mb);
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        
        JLabel lab1 = new JLabel("Date Début (YYYY-MM-DD)");
        lab1.setBounds(65, 27, 150,20);
        panel.add(lab1);
        
        textDateDeb = new JTextField();
        textDateDeb.setBounds(65, 50, 150, 25);
        panel.add(textDateDeb);
        
        JLabel lab2 = new JLabel("Date Fin (YYYY-MM-DD)");
        lab2.setBounds(65, 77, 150,20);
        panel.add(lab2);
        
        textDateDeb = new JTextField();
        textDateDeb.setBounds(65, 100, 150, 25);
        panel.add(textDateDeb);
        
        cancel = new JButton("Annuler");
        cancel.setBounds(60, 140, cancel.getPreferredSize().width, 23);
        panel.add(cancel);
        
        validate = new JButton("Valider");
        validate.setBounds(70 + cancel.getPreferredSize().width, 140, validate.getPreferredSize().width, 23);
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
            String splitDebut[], splitFin[], debutD, finD;
            debutD = textDateDeb.getText();
            finD =textDateFin.getText();
            splitDebut = debutD.split("-");
            splitFin = finD.split("-");
            if(splitDebut.length != 3 || splitFin.length != 3){
                JOptionPane.showMessageDialog(null, "Format de date non repecté.");
            }else{
                LocalDate deb = LocalDate.of(Integer.parseInt(splitDebut[0]), Integer.parseInt(splitDebut[1]), Integer.parseInt(splitDebut[2])), 
                        fin = LocalDate.of(Integer.parseInt(splitFin[0]), Integer.parseInt(splitFin[1]), Integer.parseInt(splitFin[2]));
                Seance[] seancesa = Funtions.seancesEnseignant(id, 1, deb, fin);
                new ListeCours(seancesAnnule, seancesa, debut, id, nom, type);
                this.setVisible(false);
                this.dispose();
            }
            
        }
    }
}
