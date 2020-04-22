import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;


/**
 * Panel to display above or below a chart showing the ticker and a series of buttons that will display different time periods for the stock data.
 * 
 * @author robertstanton
 *
 */
public class TableGUI extends JPanel{
    
    private final Color focusColor = new Color(255, 187, 0);
    private final Color outOfFocusColor = new Color(255, 221, 0);
    private final Color backGroundColor = new Color(238, 238, 238);
    private final Color borderColor = Color.BLACK;
    public Color[] colors;
    
    private ArrayList<JButton> buttons;
    private ArrayList<JButton> alerts;
    private JLabel stockLabel;
    private GridBagConstraints gridCont;
    private String ticker;
    private JButton focusButton;
    private JButton focusAlert;
    private Border dateAdjustBorder;
    
    /**
     * Construct the header table for a given stock ticker.
     * @param ticker
     */
    public TableGUI(String ticker) {
        this.ticker = ticker;
        buttons = new ArrayList<>();
        alerts = new ArrayList<>();
        gridCont = new GridBagConstraints();
        focusButton = new JButton();
        focusAlert = new JButton();
        dateAdjustBorder = BorderFactory.createLineBorder(borderColor);
        
        setLayout(new GridBagLayout());
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        this.setBackground(backGroundColor);
        
        stockLabel = new JLabel(ticker.toUpperCase(), JLabel.CENTER);
        stockLabel.setPreferredSize(new Dimension(160, 30));
        stockLabel.setForeground(focusColor);
        stockLabel.setBackground(backGroundColor); // color to match default button background
        stockLabel.setOpaque(true);
        stockLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        
        for(String dateRange : new String[] { "3M", "6M", "1Y", "5Y", "ALL"}) {
            
            JButton dateAdjust = new JButton(dateRange);
            dateAdjust.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
            dateAdjust.setForeground(outOfFocusColor);
            dateAdjust.setBorder(BorderFactory.createEmptyBorder());
            dateAdjust.setPreferredSize(new Dimension(80, 30));
            dateAdjust.setOpaque(true);
            dateAdjust.setHorizontalTextPosition(JLabel.CENTER);
            dateAdjust.setActionCommand(dateRange);
            dateAdjust.addMouseListener(new ButtonFormatActions());
            buttons.add(dateAdjust);
        }
        
     // Set Coloring scheme for Alert Buttons.
        Color fifty = new Color(134, 206, 136);
        Color hundred = new Color(255, 153, 132);
        Color twoHundred = Color.ORANGE;
        
        colors = new Color[] {fifty, hundred, twoHundred};
        int x = 0;
        for(String alertName : new String[] {"50d", "100d", "200d"}) {
            AlertButton alertButton = new AlertButton(alertName, colors[x]);
            alerts.add(alertButton);
            x++;
        }
        
        
        
        setFocus(buttons.get(3));
        createRow();
        createAlertRow();
    }
    
    /**
     * Sets the appearance of the time period in focus.
     * @param newFocus
     */
    public void setFocus(JButton newFocus) {
        focusButton.setBorder(BorderFactory.createEmptyBorder());
        focusButton.setForeground(outOfFocusColor);
        focusButton = newFocus;
        focusButton.setBorder(dateAdjustBorder);
        focusButton.setForeground(focusColor);
    }
    
    
    public void setFocusAlert(JButton newFocusAlert) {
        
    }

    /**
     * Adds buttons to the table view and aligns them next to the label.  Multiple tables could be make and stacked with the num param.
     * 
     * @param num determines which row to create.
     */
    public void createRow() {
        gridCont.weighty = 3.0;
        gridCont.anchor = GridBagConstraints.LINE_START;
        gridCont.gridy = 0;
        gridCont.weightx = 4.0;
        gridCont.fill = GridBagConstraints.BOTH;
        add(stockLabel, gridCont);
        
        gridCont.gridx = GridBagConstraints.RELATIVE;
        int x = 1;
        for(JButton item : buttons) {
            gridCont.weightx = 1.0;
            if(x == buttons.size()) {
                gridCont.anchor = GridBagConstraints.FIRST_LINE_END;
            }
            add(item, gridCont);
            x++;
        }
    }
    
    /**
     * Add buttons for each alert available to plot.
     */
    public void createAlertRow() {
        gridCont.weighty = 1;
        gridCont.weightx = 1;
        gridCont.gridy = 1;
        gridCont.gridx = 1;
        gridCont.fill = GridBagConstraints.BOTH;
        
         
        for(JButton button : alerts) {
            add(button, gridCont);
            gridCont.gridx = gridCont.gridx + 1;
        }
    }
    
    /**
     * Changes the label to display a new ticker and sets the focus to 5Y.
     * @param ticker
     */
    public void setStock(String ticker) {
        this.ticker = ticker;
        stockLabel.setText(ticker.toUpperCase());
        setFocus(buttons.get(3));
    }
    
    /**
     * Returns the ticker string.
     * @return
     */
    public String getStock() {
        return ticker;
    }
    
    /**
     * Returns a list of the buttons.
     * @return
     */
    public ArrayList<JButton> getButtons() {
        return buttons;
    }
    
    /**
     * Retusn list of alert buttons.
     * @return
     */
    public ArrayList<JButton> getAlerts() {
        return alerts;
    }
    
    // ========================================================================
    //                             Mouse Listener
    // ========================================================================
    
    /**
     * Formats the time adjust buttons for mouse actions.
     * @author robertstanton
     *
     */
    private class ButtonFormatActions implements MouseListener {

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
            Object source = e.getSource();
            if(source instanceof JButton) {
                if(!source.equals(focusButton)) {
                    ((JButton) source).setForeground(focusColor);
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Object source = e.getSource();
            if(source instanceof JButton) {
                if(!source.equals(focusButton)) {
                    ((JButton) source).setForeground(outOfFocusColor);
                }
            }
        }
    }
}
