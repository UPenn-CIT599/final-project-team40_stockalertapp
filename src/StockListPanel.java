import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Panel component containing a stacked group of buttons each representing a
 * Stock Object. The Panel allows for adding and removing single stocks as well
 * as notifying other compoenents of chnages to the target stock.
 * 
 * @author robertstanton
 *
 */
public class StockListPanel extends JPanel {

    private ArrayList<StockDetailButton> buttons;
    private GridBagConstraints gbConst;
    private JButton addStock;
    private GridBagLayout gbLayout;

    private MouseEventActions mouseControl;
    private JTextField newStockInput;
    private JPopupMenu popupMenu;
    private JMenuItem removeItem;
    private JMenuItem addAlertItem;
    private StockDetailButton focusButton;
    private StockCallBack addStockCallBack;
    
    
    /**
     * Constructs the left side panel of the application by creating a button for each stock.
     * 
     * @param s
     */
    public StockListPanel(ArrayList<Stock> s) {
        
        buttons = new ArrayList<>();
        mouseControl = new MouseEventActions();
        
        setBackground(Color.DARK_GRAY);
        setOpaque(true);

        gbLayout = new GridBagLayout();
        setLayout(gbLayout);

        // + / - sign to show the add stock text field. button slides text field under
        // it and then hides it again.
        addStock = new JButton("+");
        Font buttonFont = addStock.getFont();
        addStock.setPreferredSize(new Dimension(95, 30));
        addStock.setFont(new Font(buttonFont.getFontName(), buttonFont.getStyle(), (int) (buttonFont.getSize() * 1.5)));
        addStock.setBackground(Color.DARK_GRAY);
        addStock.setForeground(Color.WHITE);
        addStock.setOpaque(true);
        addStock.setBorderPainted(false);
        addStock.addMouseListener(mouseControl);
        addStock.addActionListener(new StockSlideAction());

        // text field to add a new ticker and launch a stock add action
        newStockInput = new JTextField("enter ticker");
        newStockInput.setForeground(Color.LIGHT_GRAY);
        newStockInput.addMouseListener(mouseControl);
        newStockInput.addActionListener(new EnterKeyAction());
        newStockInput.setVisible(false);

        // menu to remove a given stock
        popupMenu = new JPopupMenu();
        removeItem = new JMenuItem("Remove Stock");
        removeItem.setActionCommand("removeItem");
        removeItem.addActionListener(new RemoveStockAction());
        addAlertItem = new JMenuItem("Add Alert");
        addAlertItem.setActionCommand("addAlert");
        addAlertItem.addActionListener(new RemoveStockAction());
        popupMenu.add(removeItem);
        popupMenu.add(addAlertItem);

        gbConst = new GridBagConstraints();
        gbConst.gridy = GridBagConstraints.RELATIVE;
        gbConst.gridx = 1;
        gbConst.fill = GridBagConstraints.BOTH;
        add(addStock, gbConst);

        add(newStockInput, gbConst);
        
        if(s.size() > 0) {
            for (Stock stock : s) {
                StockDetailButton button = new StockDetailButton(stock);
                button.addMouseListener(mouseControl);
                button.addActionListener(new ChangeStockAction());
                buttons.add(button);
                add(button, gbConst);
            }

            focusButton = buttons.get(0);
        }
    }
    
    /**
     * Returns a list of available buttons.
     * @return
     */
    public ArrayList<StockDetailButton> getButtons() {
        return buttons;
    }
    
    /**
     * Changes the color scheme for the button in focus.
     */
    public void setFocusButtonColor() {
        focusButton.setBackground(Color.WHITE);
        focusButton.setForeground(Color.ORANGE);
    }

    /**
     * Changes the color scheme back to the default colors when button is no longer in focus.
     */
    public void unsetFocusButtonColor() {
        focusButton.setBackground(Color.DARK_GRAY);
        focusButton.setForeground(Color.WHITE);
    }

    /**
     * Add a new stock to the list of stocks and display the button.
     * @param s
     */
    public void addStock(Stock s) {
        StockDetailButton newButton = new StockDetailButton(s);
        newButton.addMouseListener(mouseControl);
        newButton.addActionListener(new ChangeStockAction());
        buttons.add(newButton);
        add(newButton, gbConst);
        if(focusButton instanceof StockDetailButton) {
            unsetFocusButtonColor();
        }
        focusButton = newButton;
        setFocusButtonColor();
    }

    /**
     * Removes a stock from view and stock list.  
     * @param s
     */
    public void removeStock(Stock s) {
        for (int i = 0; i< buttons.size(); i++) {
            StockDetailButton button = buttons.get(i);
            if (button.getStock().getTicker().equals(s.getTicker())) {
                buttons.remove(button);
                this.remove(button);
                revalidate();
            }
        }
        addStockCallBack.removeStock(s);
    }
    
    /**
     * Reset text input placeholder to enter ticker.
     */
    public void resetTickerInputField() {
        newStockInput.setForeground(Color.LIGHT_GRAY);
        newStockInput.setText("enter ticker");
    }

    /**
     * Slides text field for adding a new stock to visible or hidden.
     * 
     */
    public void setNewStockInputVisible() {
        if (!newStockInput.isVisible()) {
            newStockInput.setVisible(true);
            addStock.setText("-");
            revalidate();
        } else {
            newStockInput.setVisible(false);
            addStock.setText("+");
            revalidate();
        }
    }

    /**
     * Add StockCallBack implemented class for callback functions to the parent BaseGUI.
     * 
     * @param addStockCallBack
     */
    public void setNewStockAddAction(StockCallBack addStockCallBack) {
        this.addStockCallBack = addStockCallBack;
    }

    // ===================== ACTION LISTENER CLASSES =========================
    
    /**
     * Handles when text field has enter key action.
     * 
     * @author robertstanton
     *
     */
    private class EnterKeyAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.KEY_EVENT_MASK == 8) {
                JTextField comp = (JTextField) e.getSource();
                String newTicker = comp.getText().toUpperCase();

                comp.setForeground(Color.LIGHT_GRAY);
                comp.setText(" fetching ... ");
                revalidate();
                addStockCallBack.addStock(newTicker); // calls back to BaseGUI to create new Stock object
            }
        }
    }
    
    /**
     * Hide or shows the text field for entering a new Stock when the add button is hit.
     * @author robertstanton
     *
     */
    private class StockSlideAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            setNewStockInputVisible();
            revalidate();
        }
    }

    /**
     * Removes a Stock from the panel when a button is right clicked and removed.
     * @author robertstanton
     *
     */
    private class RemoveStockAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Stock s = focusButton.getStock();
            if(e.getActionCommand().equals("removeItem")) {
                removeStock(s);
                if (buttons.size() > 0) {
                    focusButton = buttons.get(0);
                    addStockCallBack.changeStock(focusButton.getStock());
                    setFocusButtonColor();
                }
            }
            
            if(e.getActionCommand().equals("addAlert")) {
                addStockCallBack.setNewAlert(s);
            }

            revalidate();
        }
    }

    /**
     * Fires changeStock call back from the StockCallBack interface.
     * @author robertstanton
     *
     */
    private class ChangeStockAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Stock s = ((StockDetailButton) e.getSource()).getStock();
            addStockCallBack.changeStock(s);
        }
    }
    
    /**
     * Adds mouse event functionality, resets coloring, invokes a right click popup menu, and clears the text field.
     * @author robertstanton
     *
     */
    private class MouseEventActions implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            Object source = e.getSource();
            if (source instanceof JTextField) {
                ((JTextField) source).setText("");
                ((JTextField) source).setForeground(Color.BLACK);
            }

            if (source instanceof StockDetailButton) {
                unsetFocusButtonColor();
                focusButton = (StockDetailButton) e.getSource();
                setFocusButtonColor();
                if (SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.show(focusButton, e.getX(), e.getY());
                }
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getSource() instanceof JButton) {
                ((JButton) e.getSource()).setForeground(new Color(222, 180, 132, 100));
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getSource() instanceof JButton) {
                ((JButton) e.getSource()).setForeground(Color.WHITE);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (e.getSource() instanceof JButton) {
                ((JButton) e.getSource()).setBackground(Color.GRAY);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (e.getSource() instanceof JButton) {
                ((JButton) e.getSource()).setBackground(Color.DARK_GRAY);
            }
        }
    }
}
