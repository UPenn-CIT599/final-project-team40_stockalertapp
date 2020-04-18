import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
        
        setBackground(Color.LIGHT_GRAY);
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
        alertButton.addMouseListener(new AlertButtonMouseActions());
        add(alertButton);
        revalidate();
    }
    
    /**
     * Removes labels from view 
     * 
     */
    public void clearAlerts() {
        for(JLabel label : labelList) {
            remove(label);
        }
        labelList = new ArrayList<>();
    }
    
    // =====================================================================
    //                         Mouse Listeners
    // =====================================================================
    
    private class AlertButtonMouseActions implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            Object source = e.getSource();
            if(source instanceof JLabel) {
                JLabel label = (JLabel) source;
                label.setBackground(new Color(253, 251, 234));
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Object source = e.getSource();
            if(source instanceof JLabel) {
                JLabel label = (JLabel) source;
                label.setBackground(Color.WHITE);
            }
        }
    }
}
