/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author kovac
 */
public class Gui_Function {
    public static JLabel createJLabel(String TEXT, String FONT, int FONT_TYPE, int FONT_SIZE,
            int X, int Y){
        JLabel label1 = new JLabel(TEXT);
        label1.setFont(new java.awt.Font(FONT, FONT_TYPE, FONT_SIZE));
        Dimension size = label1.getPreferredSize();
        label1.setBounds(X, Y, size.width, size.height);
        label1.setForeground(Color.red);
        return label1;
    }
    
    public static JLabel createJLabel(String TEXT, Color color, String FONT, int FONT_TYPE, int FONT_SIZE,
            int X, int Y){
        JLabel label1 = new JLabel(TEXT);
        label1.setFont(new java.awt.Font(FONT, FONT_TYPE, FONT_SIZE));
        label1.setForeground(color);
        Dimension size = label1.getPreferredSize();
        label1.setBounds(X, Y, size.width, size.height);
        return label1;
    }
    
    
    public static JButton createJButton(ImageIcon IMAGE, String TEXT, int VERTICAL_POS,
            int HORIZONTAL_POS, Color COLOR, boolean OPAQUE, String FONT, int FONT_TYPE, int FONT_SIZE,
            int X, int Y){

        JButton button = new JButton(TEXT, IMAGE);
        button.setVerticalTextPosition(VERTICAL_POS); 
        button.setHorizontalTextPosition(HORIZONTAL_POS); 
        button.setBackground(COLOR);
        button.setOpaque(false);
        Dimension size = button.getPreferredSize();
        button.setFont(new Font(FONT, FONT_TYPE, FONT_SIZE));
        button.setBounds(X, Y, size.width, size.height);
        button.setBorder(null);
        button.setBorderPainted(false);
        return button;
       
    }

    
    public static JButton createJButton(String TEXT, Color COLOR, boolean OPAQUE, String FONT, int FONT_TYPE, int FONT_SIZE,
            int X, int Y){

        JButton button = new JButton(TEXT); 
        button.setBackground(COLOR);
        button.setOpaque(OPAQUE);
        Dimension size = button.getPreferredSize();
        button.setFont(new Font(FONT, FONT_TYPE, FONT_SIZE));
        button.setBounds(X, Y, size.width, size.height);
        return button;
       
    }
    
    public static JButton createJButton(String TEXT, Color COLOR, boolean OPAQUE, 
            String FONT, int FONT_TYPE, int FONT_SIZE){
        
        JButton button = new JButton(TEXT);
        button.setBackground(COLOR);
        button.setOpaque(OPAQUE);
        Dimension size = button.getPreferredSize();
        button.setFont(new Font(FONT, FONT_TYPE, FONT_SIZE));
        return button;
        
    }
}
