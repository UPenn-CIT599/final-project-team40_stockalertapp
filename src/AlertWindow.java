import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AlertWindow extends JPanel{
    
    private ArrayList<JButton> labelList;
    private Dimension dimension;
    
    public AlertWindow() {
        
        labelList = new ArrayList<>();
        
        dimension = new Dimension(400, 200);
        setPreferredSize(dimension);
        setLayout(new GridLayout(0, 1));
        
        setBackground(Color.ORANGE);
        setOpaque(true);
    }
    
    public void addAlert(String msg) {
        JButton alertButton = new JButton(msg);
        labelList.add(alertButton);
        
        alertButton.setBackground(Color.WHITE);
        alertButton.setOpaque(true);
        alertButton.setHorizontalTextPosition(JLabel.LEFT);
        add(alertButton);
    }
}
