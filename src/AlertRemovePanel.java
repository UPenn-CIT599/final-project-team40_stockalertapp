import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;

/**
 * Dialog Popup for removing a save alert from a given stock.
 * @author robertstanton
 *
 */
public class AlertRemovePanel extends JDialog { 
    
    
    private JComboBox alertSelectionBox;
    private JButton removeAlertButton;
    private Stock targetStock;
    private Container contentPane;
    
    /**
     * Construct the core components and set boiler plate options.
     * @param parentFrame
     */
    public AlertRemovePanel(BaseGUI parentFrame) {
        super(parentFrame, "Remove Alert");
        
        this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
        this.setMinimumSize(new Dimension(300,100));
        
        contentPane = this.getContentPane();
        alertSelectionBox = new JComboBox();
        removeAlertButton = new JButton("Remove");
        removeAlertButton.addActionListener(new RemoveAlertAction());
        
    }
    
    /**
     * create the key elements of the dialog box and populate options where applicable.
     * @param s
     */
    public void createGUI(Stock s) {
        targetStock = s;
        if(s.getStoredAlerts().isEmpty()) {
            alertSelectionBox.removeAllItems();
        }else {
            alertSelectionBox.removeAllItems();
            for(String alertName : s.getStoredAlerts().keySet()) {
                alertSelectionBox.addItem(alertName);
            }
        }
        
        contentPane.add(alertSelectionBox, BorderLayout.CENTER);
        contentPane.add(removeAlertButton, BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    /**
     * Class for handling actions related to removal of an alert from a stock.
     * @author robertstanton
     *
     */
    private class RemoveAlertAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedItem = "";
            selectedItem = (String) alertSelectionBox.getSelectedItem();
            if(selectedItem.isEmpty()) {
                alertSelectionBox.setSelectedIndex(0);
            } else {
                targetStock.getStoredAlerts().remove(selectedItem);
            }
            dispose();
        }
    }
}
