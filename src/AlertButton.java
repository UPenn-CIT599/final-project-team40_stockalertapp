import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

/**
 * Button class for storing data on an alert for use in plotting in the ChartGUI.
 * @author robertstanton
 *
 */
public class AlertButton extends JButton {
    
    private final Color backGroundColor = new Color(238, 238, 238);
    private final Color outOfFocusColor = new Color(250, 200, 100);
    private final Border outOFFocusBorder = BorderFactory.createEmptyBorder();
    private Border focusBorder;
    private Color focusColor;
    
    public boolean toggleOn;
    public String alertName;
    
    /**
     * Default constructor chains up to main constructor.
     * 
     */
    public AlertButton() {
        this("");
    }
    
    /**
     * Constructs button with a given name and allows main constructor to set other elements.
     * @param givenName
     */
    public AlertButton(String givenName) {
        this(givenName, Color.BLACK);
    }
    
    /**
     * Constructs a Button with the required instance Variables.
     * @param givenName
     * @param fColor
     */
    public AlertButton(String givenName, Color fColor) {
        alertName = givenName;
        toggleOn = false;
        focusColor = fColor;
        focusBorder = BorderFactory.createLineBorder(focusColor);
        this.setOpaque(true);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.addMouseListener(new AlertButtonFormatActions());
        this.setText(alertName);
        this.setForeground(outOfFocusColor);
    }
    
    /**
     * Changes foreground based on focus or use.
     */
    public void setColoring() {
        if(toggleOn) {
            this.setForeground(focusColor);
            this.setBorder(focusBorder);
        } else {
            this.setForeground(outOfFocusColor);
            this.setBorder(outOFFocusBorder);
        }
    }
    
    /**
     * Changes buttons on off state.
     */
    public void toggleOnOff() {
        toggleOn = !toggleOn;
        setColoring();
    }
    
    /**
     * Set Text Color of button.
     */
    public void setColor(Color aColor) {
        focusColor = aColor;
    }
    
    /**
     * Set Name of Alert.
     * @param aName
     */
    public void setAlertName(String aName) { 
        alertName = aName;
    }
    
    // ========================================================================
    //                             Mouse Listener
    // ========================================================================
    
    /**
     * Formats the alert buttons for mouse events.
     * @author robertstanton
     *
     */
    private class AlertButtonFormatActions implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            
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
            AlertButton source = (AlertButton) e.getSource();
            if(!source.toggleOn) {
                source.setForeground(focusColor);
                source.setBorder(focusBorder);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            AlertButton source = (AlertButton) e.getSource();
            if(!source.toggleOn) {
                source.setForeground(outOfFocusColor);
                source.setBorder(outOFFocusBorder); 
            }
        }
    }
}
