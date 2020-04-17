import java.awt.Color;
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


public class StockListPanel extends JPanel {
    
    private ArrayList<StockDetailButton> buttons;
    private GridBagConstraints gbConst;
    private JButton addStock;
    private GridBagLayout gbLayout;
    
    private MouseEventActions mouseControl;
    private JTextField newStockInput;
    private JPopupMenu popupMenu;
    private JMenuItem removeItem;
    private StockDetailButton focusButton;
    private StockCallBack addStockCallBack;
    
    
    public StockListPanel(ArrayList<Stock> s) {
        buttons = new ArrayList<>();
        mouseControl = new MouseEventActions();
        setBackground(Color.DARK_GRAY);
        setOpaque(true);
        
        gbLayout = new GridBagLayout();
        setLayout(gbLayout);
        
        // + / - sign to show the add stock text field.  button slides text field under it and then hides it again.
        addStock = new JButton("+");
        Font buttonFont = addStock.getFont();
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
        popupMenu.add(removeItem);
        
        gbConst = new GridBagConstraints();
        gbConst.gridy = GridBagConstraints.RELATIVE;
        gbConst.gridx = 1;
        gbConst.fill = GridBagConstraints.BOTH;
        add(addStock, gbConst);
        
        add(newStockInput, gbConst);
        
        for(Stock stock : s) {
            StockDetailButton button = new StockDetailButton(stock);
            button.addMouseListener(mouseControl);
            button.addActionListener(new ChangeStockAction());
            buttons.add(button);
            add(button, gbConst);
        }
        
        focusButton = buttons.get(0);
    }
    
    public ArrayList<StockDetailButton> getButtons() {
        return buttons;
    }
    
    public void setFocusButtonColor() {
        focusButton.setBackground(Color.WHITE);
        focusButton.setForeground(Color.ORANGE);
    }
    
    public void unsetFocusButtonColor() {
        focusButton.setBackground(Color.DARK_GRAY);
        focusButton.setForeground(Color.WHITE);
    }
    
    public void addStock(Stock s) {
        StockDetailButton newButton = new StockDetailButton(s);
        newButton.addMouseListener(mouseControl);
        buttons.add(newButton);
        add(newButton, gbConst);
    }
    
    public void removeStock(Stock s) {
        for(StockDetailButton button : buttons) {
            if(button.getStock().equals(s)) {
                buttons.remove(button);
                this.remove(button);
                revalidate();
            }
        }
        addStockCallBack.removeStock(s);
    }
    
    public void setAddStockTextAction(ActionListener action) {
        newStockInput.addActionListener(action);
    }
    
    public void setNewStockInputVisible() {
        if(!newStockInput.isVisible()) {
            newStockInput.setVisible(true);
            addStock.setText("-");
            revalidate();
        } else {
            newStockInput.setVisible(false);
            addStock.setText("+");
            revalidate();
        }
    }
    
    /*
    public void setStockChangeAction(ActionListener action) {
        for(JButton button : buttons) {
            button.addActionListener(action);
        }
    }
    */
    
    public void setNewStockAddAction(StockCallBack addStockCallBack) {
        this.addStockCallBack = addStockCallBack;
    }
    
    // =====================  ACTION LISTENER CLASSES  =========================
    
    private class EnterKeyAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.KEY_EVENT_MASK == 8) {
                JTextField comp = (JTextField) e.getSource();
                String newTicker = comp.getText().toUpperCase();
                
                comp.setForeground(Color.LIGHT_GRAY);
                comp.setText(" fetching ... ");
                revalidate();
                // baffled ... 
                addStockCallBack.addStock(newTicker); // calls back to BaseGUI to create new Stock object
                comp.setText("enter ticker");
            }
        }
    }
    
    private class StockSlideAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            setNewStockInputVisible();
            revalidate();
        }
    }
    
    private class RemoveStockAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Stock s = focusButton.getStock();
            removeStock(s);
            if(buttons.size() > 0) {
                focusButton = buttons.get(0);
                addStockCallBack.changeStock(focusButton.getStock());
                setFocusButtonColor();
            }
            
            revalidate();
        }
    }
    
    private class ChangeStockAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Stock s = ((StockDetailButton) e.getSource()).getStock();
            addStockCallBack.changeStock(s);
        }
        
    }
    
    private class MouseEventActions implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            Object source = e.getSource();
            if(source instanceof JTextField) {
                ((JTextField) source).setText("");
                ((JTextField) source).setForeground(Color.BLACK);
            }
            
            if(source instanceof StockDetailButton) {
                unsetFocusButtonColor();
                focusButton = (StockDetailButton) e.getSource();
                setFocusButtonColor();
                if(SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.show(focusButton, e.getX(), e.getY());
                }
            }
            
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getSource() instanceof JButton) {
                ((JButton) e.getSource()).setForeground(new Color(222, 180, 132, 100));
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(e.getSource() instanceof JButton) {
                ((JButton) e.getSource()).setForeground(Color.WHITE);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(e.getSource() instanceof JButton) {
                ((JButton)e.getSource()).setBackground(Color.GRAY);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(e.getSource() instanceof JButton) {
                ((JButton) e.getSource()).setBackground(Color.DARK_GRAY); 
            }
        }
    }

}
