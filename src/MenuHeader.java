import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;


public class MenuHeader extends JMenuBar {
    private JMenu menu;
    private ArrayList<JMenuItem> menuItems;
    private Font headerFont;
    
    public MenuHeader() {
        setBackground(Color.DARK_GRAY);
        setOpaque(true);
        headerFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);
        
        menu = new JMenu("\u2261");
        menu.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 70));
        menu.setForeground(Color.BLUE);
        menuItems = new ArrayList<>();
        
        add(menu);
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container content = frame.getContentPane();
        MenuHeader menu = new MenuHeader();
        //frame.setJMenuBar(menu);
        content.setLayout(new BorderLayout());
        content.add(menu, BorderLayout.NORTH);
        
        frame.pack();
        frame.setVisible(true);
    }
    
}
