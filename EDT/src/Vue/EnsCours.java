package Vue;

import java.awt.BorderLayout;
import javax.swing.*;


public class EnsCours extends JFrame {
    private String[] title = {"Date", "Début", "Fin", "Durée"};
    private final int SIZE_X = 600, SIZE_Y = 400;
    
    
    public EnsCours(Object[][] cours){
        this.setTitle("Résumé de cours");
        JTable coursTable = new JTable(cours, title){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        this.setTitle("Racpitulatif de cours");
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(SIZE_X, SIZE_Y);
        this.getContentPane().add(new JScrollPane(coursTable), BorderLayout.CENTER);
        
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
