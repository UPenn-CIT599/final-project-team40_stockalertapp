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

/** 
 * Panel is used for displaying text version of alerts based on settings for various stocks.
 * 
 * @author robertstanton
 *
 */
public class AlertWindow extends JPanel{
    
    private ArrayList<JLabel> labelList;
    private Dimension dimension;
    
    /**
     * Constructs basic panel to display labels with alert information;
     */
    public AlertWindow() {
        
        labelList = new ArrayList<>();
        
        dimension = new Dimension(400, 200);
        setPreferredSize(dimension);
        setLayout(new GridLayout(0, 1));
        
        setBackground(Color.ORANGE);
        setOpaque(true);
    }
    
    /** 
     * Adds a new Alert message to the panel.
     * 
     * @param msg
     */
    public void addAlert(String msg) {
        JLabel alertButton = new JLabel(msg);
        labelList.add(alertButton);
        
        alertButton.setBackground(Color.WHITE);
        alertButton.setOpaque(true);
        alertButton.setHorizontalTextPosition(JLabel.LEFT);
        add(alertButton);
        revalidate();
    }
}
