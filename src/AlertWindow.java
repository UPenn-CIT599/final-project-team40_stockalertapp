import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel is used for displaying text version of alerts based on settings for
 * various stocks.
 * 
 * @author robertstanton
 *
 */
public class AlertWindow extends JPanel {

    private ArrayList<JLabel> labelList;
    private Dimension dimension;
    private GridBagConstraints content;

    /**
     * Constructs basic panel to display labels with alert information;
     */
    public AlertWindow() {

        labelList = new ArrayList<>();

        dimension = new Dimension(400, 200);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setBackground(Color.WHITE);
        setOpaque(true);
    }

    /**
     * Adds a new Alert message to the panel.
     * 
     * @param msg
     */
    public void addAlert(String msg) {
        JLabel alertLabel = new JLabel(msg) {
            // This is a joke. There is no reason i should have to override a method to get
            // a label to fill horizontally
            // while having a fixed height and aligning top to bottom.....

            @Override
            public Dimension getMaximumSize() {
                Dimension d = super.getMaximumSize();
                d.width = Integer.MAX_VALUE;
                return d;
            }
        };
        
        alertLabel.setBackground(Color.WHITE);
        alertLabel.setOpaque(true);
        alertLabel.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));
        alertLabel.setHorizontalTextPosition(JLabel.LEFT);
        alertLabel.addMouseListener(new AlertButtonMouseActions());

        labelList.add(alertLabel);
        int numAlerts = labelList.size();
        add(alertLabel);
        revalidate();
    }

    /**
     * Removes labels from view
     * 
     */
    public void clearAlerts() {
        for (JLabel label : labelList) {
            remove(label);
        }
        labelList = new ArrayList<>();
    }

    // =====================================================================
    // Mouse Listeners
    // =====================================================================
    
    /**
     * Formats button for mouse hovering.
     * @author robertstanton
     *
     */
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
            if (source instanceof JLabel) {
                JLabel label = (JLabel) source;
                label.setBackground(new Color(253, 251, 234));
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Object source = e.getSource();
            if (source instanceof JLabel) {
                JLabel label = (JLabel) source;
                label.setBackground(Color.WHITE);
            }
        }
    }
}
