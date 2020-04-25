import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Dialog box for entering a new alert for a stock.
 * @author robertstanton
 *
 */
public class AlertPanel extends JDialog {
    
    private String indicator;
    private String level;
    private double triggerValue;
    private Stock targetStock;
    
    
    private String[] indicatorOptions;
    private String[] levelOptions;
    private Stock tgtStock;
    
    private JComboBox indicatorOptionsBox;
    private JComboBox levelOptionsBox;
    private JButton saveButton;
    private JFormattedTextField triggerValueBox;
    // private JLabel panelHeader;
    private JLabel indicatorLabel;
    private JLabel levelOptionLabel;
    private JLabel triggerValLabel;
    private Container contentPane;
    // private BaseGUI parentFrame;
    
    private NumberFormat triggerFormat;
    
    
    /**
     * Constructs a dialog window for users to enter alerts to a stock object.
     * @param parentFrame
     */
    public AlertPanel(BaseGUI parentFrame) {
        super(parentFrame, "Add Alert Options");
        
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setMinimumSize(new Dimension(400, 200));
        // this.parentFrame = parentFrame;
        indicatorOptions = new String[] {"SMA", "EMA", "RSI", "MACD", "OBV"};
        levelOptions = new String[] {"above", "below"};
        indicator = "";
        level = "";
        triggerValue = 0;
        
        // panelHeader = new JLabel("Add Alert Options");
        indicatorLabel = new JLabel("choose indicator");
        levelOptionLabel = new JLabel("select direction");
        triggerValLabel = new JLabel("enter value for indicator");
        saveButton = new JButton("Save Alert");
        saveButton.addActionListener(new StoreAlertDetails());
        
        indicatorOptionsBox = new JComboBox(indicatorOptions);
        levelOptionsBox = new JComboBox(levelOptions);
        triggerValueBox = new JFormattedTextField(triggerFormat);
        
        contentPane = this.getContentPane();
        contentPane.setLayout(new GridLayout(0, 2));
        
    }
    
    /**
     * adds components of Dialog window and displays.
     * @param s
     */
    public void createGUI(Stock s) {
        tgtStock = s;
        
        contentPane.add(indicatorLabel);
        contentPane.add(indicatorOptionsBox);
        
        contentPane.add(levelOptionLabel);
        contentPane.add(levelOptionsBox);
        
        contentPane.add(triggerValLabel);
        contentPane.add(triggerValueBox);
        
        contentPane.add(saveButton);
        
        showDialog();
    }
    
    /**
     * method to display 
     */
    public void showDialog() {
        this.setVisible(true);
    }
    
    private class StoreAlertDetails implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndicator = indicatorOptionsBox.getSelectedIndex();
            if(selectedIndicator > -1) {
                indicator = indicatorOptions[selectedIndicator];
            }
            
            int selectedLevel = levelOptionsBox.getSelectedIndex();
            if(selectedLevel > -1) {
                level = levelOptions[selectedLevel];
            }
            
            try {
                triggerValue = Double.parseDouble(triggerValueBox.getText());
            } catch(NumberFormatException nfe) {
                triggerValueBox.setText("");
            }
            
            if(!indicator.isEmpty() && !level.isEmpty() && !(triggerValue == 0)) {
                tgtStock.addAlert(tgtStock.getTicker(), indicator, level, triggerValue);
                dispose();
            } else {
                triggerValueBox.setText("");
            }
        }
        
    }
}
